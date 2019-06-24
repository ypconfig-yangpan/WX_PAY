package com.cancer.pay.paycenter.Impl;

import com.cancer.pay.config.WxPayAppletConfig;
import com.cancer.pay.constants.WxPayConstants;
import com.cancer.pay.enums.OrderStatusEnum;
import com.cancer.pay.enums.PayChannel;
import com.cancer.pay.paycenter.service.AbstractPayment;
import com.cancer.pay.paycenter.service.WxPayApi;
import com.cancer.pay.req.wxpay.request.*;
import com.cancer.pay.req.wxpay.response.*;
import com.cancer.pay.req.wxpay.response.PayResponse;
import com.cancer.pay.utils.MapUtil;
import com.cancer.pay.utils.MoneyUtil;
import com.cancer.pay.utils.RandomUtil;
import com.cancer.pay.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.paycenter.Impl
 * @ClassName: WXPayment
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/11 0011 10:59
 * @Version: 1.0
 */
@Service
@Slf4j
public class WXPayment extends AbstractPayment {


    public PayChannel getPayChannel() {
        return PayChannel.WXPAY_APPLET;
    }

    @Override
    public String getMchKey() {
        return wxPayAppletConfig.getMchKey();
    }

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(WxPayConstants.WXPAY_GATEWAY)
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .client(new OkHttpClient.Builder()
                    .addInterceptor((new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY)))
                    .build()
            )
            .build();

    // todo 最后改成配置文件
    private WxPayAppletConfig  wxPayAppletConfig =   new WxPayAppletConfig();

    @Override
    public PayResponse pay(PayRequest request) {

        // 微信统一支付请求
        WxPayUnifiedorderRequest wxRequest = new WxPayUnifiedorderRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setTotalFee(request.getOrderAmount());
        wxRequest.setBody(request.getOrderName());
        wxRequest.setOpenid(request.getOpenid());
        // todo 以后放到抽象类直接赋值
        wxRequest.setTradeType("JSAPI");
        wxRequest.setAppid(wxPayAppletConfig.getAppId());
        wxRequest.setMchId(wxPayAppletConfig.getMchId());
        wxRequest.setNotifyUrl(wxPayAppletConfig.getNotifyUrl());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSpbillCreateIp(request.getSpbillCreateIp() == null || request.getSpbillCreateIp().isEmpty() ? "8.8.8.8" : request.getSpbillCreateIp());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayAppletConfig.getMchKey()));

        OkHttpClient okHttpClient = new OkHttpClient()
                .newBuilder()
                .sslSocketFactory(wxPayAppletConfig.getSslContext().getSocketFactory())
                .addInterceptor((new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY)))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WxPayConstants.WXPAY_GATEWAY)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .client(okHttpClient)
                .build();

        String xml = XmlUtil.toString(wxRequest);
        System.out.println(xml);

        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"),xml);
        Call<WxPaySyncResponse> call = retrofit.create(WxPayApi.class).unifiedorder(body);
        Response<WxPaySyncResponse> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信统一支付】发起支付, 网络异常");
        }
        WxPaySyncResponse response = retrofitResponse.body();

        if(!response.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信统一支付】发起支付, returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }
        if (!response.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信统一支付】发起支付, resultCode != SUCCESS, err_code = " + response.getErrCode() + " err_code_des=" + response.getErrCodeDes());
        }

        return buildPayResponse(response);
    }


    @Override
    public OrderQueryResponse orderQuery(OrderQueryRequest request) {
        WxOrderQueryRequest wxRequest = new WxOrderQueryRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setTransactionId(request.getOutOrderId());

        wxRequest.setAppid(wxPayAppletConfig.getAppId());
        wxRequest.setMchId(wxPayAppletConfig.getMchId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayAppletConfig.getMchKey()));
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"), XmlUtil.toString(wxRequest));

        Call<WxOrderQueryResponse> call = retrofit.create(WxPayApi.class).orderquery(body);
        Response<WxOrderQueryResponse> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信订单查询】网络异常");
        }
        WxOrderQueryResponse response = retrofitResponse.body();
        if(!response.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信订单查询】returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }
        if (!response.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信订单查询】resultCode != SUCCESS, err_code = " + response.getErrCode() + ", err_code_des=" + response.getErrCodeDes());
        }
        return OrderQueryResponse.builder()
                .orderStatusEnum(OrderStatusEnum.findByName(response.getTradeState()))
                .resultMsg(response.getTradeStateDesc() == null ? "" : response.getTradeStateDesc())
                .build();
    }

    /**
     * 返回给小程序的参数,小程序直接支付
     * @param response
     * @return
     */
    private PayResponse buildPayResponse(WxPaySyncResponse response) {
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = RandomUtil.getRandomStr();
        String packAge = "prepay_id=" + response.getPrepayId();
        String signType = "MD5";

        //先构造要签名的map
        Map<String, String> map = new HashMap<>();
        map.put("appId", response.getAppid());
        map.put("timeStamp", timeStamp);
        map.put("nonceStr", nonceStr);
        map.put("package", packAge);
        map.put("signType", signType);

        PayResponse payResponse = new PayResponse();
        payResponse.setAppId(response.getAppid());
        payResponse.setTimeStamp(timeStamp);
        payResponse.setNonceStr(nonceStr);
        payResponse.setPackAge(packAge);
        payResponse.setSignType(signType);
        payResponse.setPaySign(WxPaySignature.sign(map, wxPayAppletConfig.getMchKey()));
        payResponse.setMwebUrl(response.getMwebUrl());

        return payResponse;
    }


    @Override
    public RefundResponse applicationRefund(RefundRequest request) {
        WxPayRefundRequest wxRequest = new WxPayRefundRequest();
        wxRequest.setOutTradeNo(request.getOrderId());
        wxRequest.setOutRefundNo(request.getOrderId());
        wxRequest.setTotalFee(request.getOrderAmount());
        wxRequest.setRefundFee(request.getOrderAmount());

        wxRequest.setAppid(wxPayAppletConfig.getAppId());
        wxRequest.setMchId(wxPayAppletConfig.getMchId());
        wxRequest.setNonceStr(RandomUtil.getRandomStr());
        wxRequest.setSign(WxPaySignature.sign(MapUtil.buildMap(wxRequest), wxPayAppletConfig.getMchKey()));

        String xml = XmlUtil.toString(wxRequest);
        RequestBody body = RequestBody.create(MediaType.parse("application/xml; charset=utf-8"),xml);
        Call<WxRefundResponse> call = retrofit.create(WxPayApi.class).refund(body);
        Response<WxRefundResponse> retrofitResponse  = null;
        try{
            retrofitResponse = call.execute();
        }catch (IOException e) {
            e.printStackTrace();
        }
        if (!retrofitResponse.isSuccessful()) {
            throw new RuntimeException("【微信退款】发起退款, 网络异常");
        }
        WxRefundResponse response = retrofitResponse.body();

        if(!response.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信退款】发起退款, returnCode != SUCCESS, returnMsg = " + response.getReturnMsg());
        }
        if (!response.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信退款】发起退款, resultCode != SUCCESS, err_code = " + response.getErrCode() + " err_code_des=" + response.getErrCodeDes());
        }

        return buildRefundResponse(response);
    }
    private RefundResponse buildRefundResponse(WxRefundResponse response) {
        RefundResponse refundResponse = new RefundResponse();
        refundResponse.setOrderId(response.getOutTradeNo());
        refundResponse.setOrderAmount(response.getTotalFee());
        refundResponse.setOutTradeNo(response.getTransactionId());
        refundResponse.setRefundId(response.getOutRefundNo());
        refundResponse.setOutRefundNo(response.getRefundId());
        return refundResponse;
    }


}
