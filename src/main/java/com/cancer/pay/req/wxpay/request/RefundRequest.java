package com.cancer.pay.req.wxpay.request;

import com.cancer.pay.enums.PayChannel;
import lombok.Data;

/**
 * 支付时请求参数
 */
@Data
public class RefundRequest {

    /**
     * 支付方式.
     */
    private PayChannel payChannel;

    /**
     * 订单号.
     */
    private String orderId;

    /**
     * 订单金额.
     */
    private int orderAmount;
}
