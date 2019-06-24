package com.cancer.pay.utils;

/**
 * @ProjectName: orderId 流水id
 */
public class OrderIdUtil {


    public static String createOrderId(){
        SnowFlake snowFlake = new SnowFlake(0, 1);
        long id = snowFlake.nextId();
        return  String.valueOf(id);
    }
}
