package com.cancer.pay.utils;

import java.math.BigDecimal;

/**
 * 金额转换
 */
public class MoneyUtil {

    /**
     * 元转分
     * @param yuan
     * @return
     */
    public static Integer Yuan2Fen(Double yuan) {
        return new BigDecimal(String.valueOf(yuan)).movePointRight(2).intValue();
    }

    /**
     * 分转元
     * @param fen
     * @return
     */
    public static Double Fen2Yuan(Integer fen) {
        return new BigDecimal(fen).movePointLeft(2).doubleValue();
    }
}
