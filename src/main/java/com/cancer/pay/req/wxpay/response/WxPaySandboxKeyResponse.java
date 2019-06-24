package com.cancer.pay.req.wxpay.response;

import lombok.Data;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Data
@Root(name = "xml", strict = false)
public class WxPaySandboxKeyResponse {

    @Element(name = "return_code")
    private String returnCode;

    @Element(name = "return_msg", required = false)
    private String returnMsg;

    @Element(name = "mch_id", required = false)
    private String mchId;

    @Element(name = "sandbox_signkey", required = false)
    private String sandboxSignkey;
}
