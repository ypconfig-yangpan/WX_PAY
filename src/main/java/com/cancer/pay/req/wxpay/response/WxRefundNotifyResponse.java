package com.cancer.pay.req.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * 微信退款结果异步通知
 */
@Data
@Root(name = "xml", strict = false)
public class WxRefundNotifyResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    /** 以下字段在return_code为SUCCESS的时候有返回. */
    @Element(name = "appid", required = false)
    private String appid;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "req_info", required = false)
    private String reqInfo;



}
