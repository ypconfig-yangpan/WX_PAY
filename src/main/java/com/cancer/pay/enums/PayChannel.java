package com.cancer.pay.enums;

import com.cancer.pay.exception.PayException;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.enums
 * @ClassName: PayChannel
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/7 0007 21:01
 * @Version: 1.0
 */
public enum PayChannel {

    ALIPAY_APP("alipay_app", "支付宝app"),

    ALIPAY_PC("alipay_pc", "支付宝pc"),

    ALIPAY_WAP("alipay_wap", "支付宝wap"),

    WXPAY_H5("wxpay_h5", "微信公众账号支付"),

    WXPAY_APPLET("applet", "微信小程序支付"),
    ;

    private String code;

    private String name;

    PayChannel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public static PayChannel getByCode(String code) {
        for (PayChannel bestPayTypeEnum : PayChannel.values()) {
            if (bestPayTypeEnum.getCode().equals(code)) {
                return bestPayTypeEnum;
            }
        }
        throw new PayException(PayResultEnum.PAY_TYPE_ERROR);
    }
}
