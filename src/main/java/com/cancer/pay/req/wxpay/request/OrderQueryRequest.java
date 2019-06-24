package com.cancer.pay.req.wxpay.request;

import com.cancer.pay.enums.PayChannel;
import lombok.Data;

/**
 * 支付订单查询
 */
@Data
public class OrderQueryRequest {

    /**
     * 支付方式.
     */
    private PayChannel payChannel;

    /**
     * 订单号(orderId 和 outOrderId 二选一，两个都传以outOrderId为准)
     */
    private String orderId = "";

    /**
     * 外部订单号(例如微信生成的)
     */
    private String outOrderId = "";
}
