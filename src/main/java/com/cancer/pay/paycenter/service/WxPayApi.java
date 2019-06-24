package com.cancer.pay.paycenter.service;

import com.cancer.pay.req.wxpay.response.WxOrderQueryResponse;
import com.cancer.pay.req.wxpay.response.WxPaySandboxKeyResponse;
import com.cancer.pay.req.wxpay.response.WxPaySyncResponse;
import com.cancer.pay.req.wxpay.response.WxRefundResponse;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.paycenter.service
 * @ClassName: WxPayApi
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/11 0011 10:44
 * @Version: 1.0
 */
public interface WxPayApi {

    /**
     * 统一下单
     * @param body
     * @return
     */
    @POST("/pay/unifiedorder")
    Call<WxPaySyncResponse> unifiedorder(@Body RequestBody body);

    /**
     * 申请退款
     * @param body
     * @return
     */
    @POST("/secapi/pay/refund")
    Call<WxRefundResponse> refund(@Body RequestBody body);

    /**
     * 申请沙箱密钥
     * @param body
     * @return
     */
    @POST("/sandboxnew/pay/getsignkey")
    Call<WxPaySandboxKeyResponse> getsignkey(@Body RequestBody body);

    /**
     * 订单查询
     * @param body
     * @return
     */
    @POST("/pay/orderquery")
    Call<WxOrderQueryResponse> orderquery(@Body RequestBody body);

    @POST("/pay/downloadbill")
    Call<ResponseBody> downloadBill(@Body RequestBody body);
}
