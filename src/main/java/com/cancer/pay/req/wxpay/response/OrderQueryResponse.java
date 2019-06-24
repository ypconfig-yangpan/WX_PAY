package com.cancer.pay.req.wxpay.response;

import com.cancer.pay.enums.OrderStatusEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 订单查询结果
 */
@Data
@Builder
public class OrderQueryResponse {

    /**
     * 订单状态
     */
    private OrderStatusEnum orderStatusEnum;

    /**
     * 错误原因
     */
    private String resultMsg;
}
