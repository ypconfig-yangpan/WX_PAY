package com.cancer.pay.atest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.atest
 * @ClassName: API
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/15 0015 21:12
 * @Version: 1.0
 */
public interface API {
    @FormUrlEncoded
    @POST("/test")
    Call<String>   test(@Field("request") String request);
}
