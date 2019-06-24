package com.cancer.pay.paycenter.service;

import com.cancer.pay.config.SignType;
import com.cancer.pay.req.wxpay.request.OrderQueryRequest;
import com.cancer.pay.req.wxpay.request.PayRequest;
import com.cancer.pay.req.wxpay.response.OrderQueryResponse;
import com.cancer.pay.req.wxpay.request.RefundRequest;
import com.cancer.pay.req.wxpay.response.RefundResponse;
import com.cancer.pay.req.wxpay.request.DownloadBillRequest;
import com.cancer.pay.req.wxpay.response.PayResponse;
import com.cancer.pay.req.wxpay.response.WxRefundNotifyDataResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.paycenter
 * @ClassName: Payment
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/11 0011 10:22
 * @Version: 1.0
 */
public interface Payment {

    /**
     * 支付接口
     * @param request
     * @return
     */
    PayResponse payment(PayRequest request);

    /**
     * 验证支付结果. 包括同步和异步.
     *
     * @param toBeVerifiedParamMap 待验证的支付结果参数.
     * @param signType             签名方式.
     * @param sign                 签名.
     * @return 验证结果.
     */
    boolean verify(Map<String, String> toBeVerifiedParamMap, SignType signType, String sign);

    /**
     * 同步回调
     * @param request
     * @return
     */
    PayResponse syncNotify(HttpServletRequest request);

    /**
     * 异步回调
     * @param notifyData
     * @return
     */
    PayResponse asyncNotify(String notifyData);

    /**
     * 退款
     * @param request
     * @return
     */
    RefundResponse refund(RefundRequest request);
    /**
     * 微信退款通知
     * @param notifyData
     * @return
     */
    WxRefundNotifyDataResponse refundAsyncNotify(String notifyData);

    /**
     * 查询订单
     * @param request
     * @return
     */
    OrderQueryResponse query(OrderQueryRequest request);


    /**
     * 下载对账单
     * @param request
     * @return
     */
    String downloadBill(DownloadBillRequest request);

}
