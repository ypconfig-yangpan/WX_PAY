package com.cancer.pay.req.wxpay.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.net.URI;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.resp
 * @ClassName: PayResponse
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/11 0011 10:23
 * @Version: 1.0
 */
@Data
public class PayResponse {

    private String prePayParams;

    private URI payUri;

    /** 以下字段仅在微信h5支付返回. */
    private String appId;

    private String timeStamp;

    private String nonceStr;

    @JsonProperty("package")
    private String packAge;

    private String signType;

    private String paySign;

    /** 以下字段在微信异步通知下返回. */
    private Double orderAmount;

    private String orderId;

    //第三方支付的流水号
    private String outTradeNo;

    /** 以下支付是h5支付返回*/
    private String mwebUrl;
}
