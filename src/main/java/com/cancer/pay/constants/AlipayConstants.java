package com.cancer.pay.constants;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.constants
 * @ClassName: AlipayConstants
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/12 0012 18:35
 * @Version: 1.0
 */
public class AlipayConstants {

    /** 请求处理成功. */
    String SUCCESS            = "success";

    /** 请求处理失败. */
    String FAIL               = "fail";

    /** 支付宝新网关.  */
    String ALIPAY_GATEWAY_NEW = "https://mapi.wxpay.com/gateway.do";


    /** 支付宝网关
    String ALIPAY_GATEWAY_OPEN     = "https://openapi.wxpay.com/gateway.do";

    /** 支付宝返回码 - 成功. */
    String RESPONSE_CODE_SUCCESS = "10000";

    /** 支付宝消息验证地址. */
    String ALIPAY_VERIFY_URL = "https://mapi.wxpay.com/gateway.do?service=notify_verify&";
}
