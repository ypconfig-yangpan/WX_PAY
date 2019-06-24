package com.cancer.pay.req.wxpay.request;

import lombok.Data;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.req.wxpay.request
 * @ClassName: DownloadBillRequest
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/12 0012 19:03
 * @Version: 1.0
 */
@Data
public class DownloadBillRequest {

    //对账日期
    private String billDate;
}
