package com.cancer.pay.atest;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.atest
 * @ClassName: Test
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/15 0015 21:16
 * @Version: 1.0
 */
public class Test {
    public static void main(String[] args) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl("http://localhost:8080/")
                .build();

        API api = retrofit.create(API.class);

        Call<String> test = api.test("你好啊 ");
        Response<String> execute = test.execute();

        boolean successful = execute.isSuccessful();
        System.out.println(successful);
        String message = execute.message();
        System.out.println(execute.body());
    }
}
