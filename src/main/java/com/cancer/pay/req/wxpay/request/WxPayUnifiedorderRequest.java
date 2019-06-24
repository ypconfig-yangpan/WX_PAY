package com.cancer.pay.req.wxpay.request;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(name = "xml", strict = false)
public class WxPayUnifiedorderRequest {

    @Element(name = "appid")
    private String appid;

    @Element(name = "mch_id")
    private String mchId;

    @Element(name = "nonce_str")
    private String nonceStr;

    @Element(name = "sign")
    private String sign;

    @Element(name = "attach", required = false)
    private String attach;

    @Element(name = "body", required = false)
    private String body;

    @Element(name = "detail", required = false)
    private String detail;

    /**
     * 用户标识
     * trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。openid如何获取，可参考【获取openid】。
     */
    @Element(name = "openid", required=false)
    private String openid;

    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*且在同一个商户号下唯一。详见商户订单号
     * String(32)
     */
    @Element(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 终端IP  支持IPV4和IPV6两种格式的IP地址。调用微信支付API的机器IP
     */
    @Element(name = "spbill_create_ip")
    private String spbillCreateIp;
    /**
     * 订单总金额，单位为分，详见支付金额
     */
    @Element(name = "total_fee")
    private Integer totalFee;

    /**
     * 通知地址	notify_url
     * 异步接收微信支付结果通知的回调地址，通知url必须为外网可访问的url，不能携带参数。
     */
    @Element(name = "notify_url")
    private String notifyUrl;

    /**
     * 交易类型
     */
    @Element(name = "trade_type")
    private String tradeType;

}
