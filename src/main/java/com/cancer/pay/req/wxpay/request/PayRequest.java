package com.cancer.pay.req.wxpay.request;

import com.cancer.pay.enums.PayChannel;
import lombok.Data;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.req
 * @ClassName: PayRequest
 * @Author: yangshiqi
 * @Description:  支付请求
 * @Date: 2019/5/11 0011 10:23
 * @Version: 1.0
 */
@Data
public class PayRequest {
    /**
     * 订单id
     */
    private String  orderId;
    /**
     * 支付渠道
     */
    private PayChannel payChannel;

    /**
     * 支付金额(分)
     */
    private int  orderAmount;

    /**
     * 订单名称
     */
    private String  orderName;

    /**
     * openid
     */
    private String  openid;
    /**
     * 客户端访问Ip  外部H5支付时必传，需要真实Ip
     */
    private String spbillCreateIp;

}
