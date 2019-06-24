package com.cancer.pay.enums;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.enums
 * @ClassName: OrderStatusEnum
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/12 0012 22:42
 * @Version: 1.0
 */
public enum  OrderStatusEnum {
    SUCCESS("支付成功"),

    REFUND("转入退款"),

    NOTPAY("未支付"),

    CLOSED("已关闭"),

    REVOKED("已撤销（刷卡支付）"),

    USERPAYING("用户支付中"),

    PAYERROR("支付失败"),

    UNKNOW("未知状态"),
            ;

    /**
     * 描述 微信退款后有内容
     */
    private String desc;

    OrderStatusEnum(String desc) {
        this.desc = desc;
    }

    public static OrderStatusEnum findByName(String name) {
        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {
            if (name.toLowerCase().equals(orderStatusEnum.name().toLowerCase())) {
                return orderStatusEnum;
            }
        }
        return UNKNOW;
    }
}
