package com.cancer.pay.req.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 同步返回参数
 */
@Data
@Root(name = "root", strict = false)
public class WxRefundNotifyDataResponse {
    //微信订单号
    @Element(name = "transaction_id")
    private String transactionId;

    @Element(name = "out_trade_no")
    private String outTradeNo;

    @Element(name = "refund_id")
    private String refundId;

    @Element(name = "out_refund_no")
    private String outRefundNo;

    @Element(name = "total_fee")
    private String totalFee;

    @Element(name = "settlement_total_fee")
    private String settlementTotalFee;

    @Element(name = "refund_fee")
    private String refundFee;

    @Element(name = "settlement_refund_fee")
    private String settlementRefundFee;

    @Element(name = "refund_status")
    private String refundStatus;

    @Element(name = "success_time")
    private String successTime;

    @Element(name = "refund_recv_accout")
    private String refundRecvAccout;

    @Element(name = "refund_account")
    private String refundAccount;

    @Element(name = "refund_request_source")
    private String refundRequestSource;

}
