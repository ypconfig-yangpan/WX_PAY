package com.cancer.pay.paycenter.service;

import com.cancer.pay.config.SignType;
import com.cancer.pay.constants.WxPayConstants;
import com.cancer.pay.enums.PayChannel;
import com.cancer.pay.enums.PayResultEnum;
import com.cancer.pay.exception.PayException;
import com.cancer.pay.paycenter.Impl.WxPaySignature;
import com.cancer.pay.req.wxpay.request.OrderQueryRequest;
import com.cancer.pay.req.wxpay.request.PayRequest;
import com.cancer.pay.req.wxpay.request.RefundRequest;
import com.cancer.pay.req.wxpay.response.*;
import com.cancer.pay.req.wxpay.request.DownloadBillRequest;
import com.cancer.pay.utils.MoneyUtil;
import com.cancer.pay.utils.XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.paycenter.Impl
 * @ClassName: AbstractPayment
 * @Author: yangshiqi
 * @Description: 支付抽象类
 * @Date: 2019/5/11 0011 10:44
 * @Version: 1.0
 */
@Slf4j
public abstract  class AbstractPayment implements Payment {
    private static Map<PayChannel, AbstractPayment> payments = new ConcurrentHashMap<PayChannel, AbstractPayment>();
    @PostConstruct
    public void  init(){
        payments.put(this.getPayChannel(),this);
    }
    public abstract PayChannel getPayChannel();
    public abstract String getMchKey();

    /**
     * 支付
     * @param request
     * @return
     */
    @Override
    public PayResponse payment(PayRequest request) {
        AbstractPayment abstractPayment = payments.get(request.getPayChannel());
        if (abstractPayment ==null){
            throw  new PayException(PayResultEnum.PAY_TYPE_ERROR);
        }
        return  abstractPayment.pay(request);
    }
    public abstract PayResponse pay(PayRequest request);




    @Override
    public boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign) {
        return false;
    }

    @Override
    public PayResponse syncNotify(HttpServletRequest request) {
        return null;
    }


    /**
     * 退款通知
     * @param notifyData
     * @return
     */
    @Override
    public WxRefundNotifyDataResponse refundAsyncNotify(String notifyData) {

        //xml解析为对象
        WxRefundNotifyResponse refundNotifyResponse = (WxRefundNotifyResponse) XmlUtil.toObject(notifyData, WxRefundNotifyResponse.class);

        if(!refundNotifyResponse.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信退款异步通知】退款, returnCode != SUCCESS, returnMsg = " + refundNotifyResponse.getReturnMsg());
        }
        // 获取加密信息
        String reqInfo = refundNotifyResponse.getReqInfo();
        //AES解密
        String md5Hex = DigestUtils.md5Hex(getMchKey());
        String root = StringUtils.EMPTY;
        try {
            root = WxPaySignature.decryptAES(reqInfo, md5Hex);
        } catch (Exception e) {
            throw new RuntimeException("【微信退款异步通知】退款, 返回信息解密失败" +  e.getMessage());
        }
        if (StringUtils.isBlank(root)){
            throw new RuntimeException("【微信退款异步通知】退款异常, 返回信息为空" );
        }
        WxRefundNotifyDataResponse refundNotifyDataResponse = (WxRefundNotifyDataResponse)XmlUtil.toObject(root, WxRefundNotifyDataResponse.class);
        return refundNotifyDataResponse;
    }

    /**
     * 异步通知
     * @param notifyData
     * @return
     */
    @Override
    public PayResponse asyncNotify(String notifyData) {
        //签名校验
        if (!WxPaySignature.verify(XmlUtil.toMap(notifyData), getMchKey())) {
            log.error("【微信支付异步通知】签名验证失败, response={}", notifyData);
            throw new RuntimeException("【微信支付异步通知】签名验证失败");
        }

        //xml解析为对象
        WxPayAsyncResponse asyncResponse = (WxPayAsyncResponse) XmlUtil.toObject(notifyData, WxPayAsyncResponse.class);

        if(!asyncResponse.getReturnCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信支付异步通知】发起支付, returnCode != SUCCESS, returnMsg = " + asyncResponse.getReturnMsg());
        }
        //该订单已支付直接返回
        if (!asyncResponse.getResultCode().equals(WxPayConstants.SUCCESS)
                && asyncResponse.getErrCode().equals("ORDERPAID")) {
            return buildPayResponse(asyncResponse);
        }

        if (!asyncResponse.getResultCode().equals(WxPayConstants.SUCCESS)) {
            throw new RuntimeException("【微信支付异步通知】发起支付, resultCode != SUCCESS, err_code = " + asyncResponse.getErrCode() + " err_code_des=" + asyncResponse.getErrCodeDes());
        }

        return buildPayResponse(asyncResponse);
    }
    private PayResponse buildPayResponse(WxPayAsyncResponse response) {
        PayResponse payResponse = new PayResponse();
        payResponse.setOrderAmount(MoneyUtil.Fen2Yuan(response.getTotalFee()));
        payResponse.setOrderId(response.getOutTradeNo());
        payResponse.setOutTradeNo(response.getTransactionId());
        payResponse.setMwebUrl(response.getMwebUrl());
        return payResponse;
    }

    @Override
    public RefundResponse refund(RefundRequest request) {
        return applicationRefund(request);
    }
    /**
     * 商家退款
     * @param request
     * @return
     */
    public abstract RefundResponse applicationRefund(RefundRequest request);
    @Override
    public OrderQueryResponse query(OrderQueryRequest request) {
        return orderQuery(request);
    }

    /**
     * 订单查询
     * @param request
     * @return
     */
    public abstract OrderQueryResponse orderQuery(OrderQueryRequest request);
    /**
     * 对账
     * @param request
     * @return
     */
    @Override
    public String downloadBill(DownloadBillRequest request) {
        return null;
    }






}
