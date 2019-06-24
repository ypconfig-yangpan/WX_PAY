package com.cancer.pay.exception;


import com.cancer.pay.enums.PayResultEnum;

/**
 * 支付中心异常
 */
public class PayException extends RuntimeException {

    private Integer code;

    public PayException(PayResultEnum resultEnum) {
        super(resultEnum.getMsg());
        code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }
}
