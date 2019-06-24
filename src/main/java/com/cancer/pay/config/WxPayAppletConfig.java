package com.cancer.pay.config;

import lombok.Data;
import org.apache.http.ssl.SSLContexts;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.ClassPathResource;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;


/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.config
 * @ClassName: WxPayAppletConfig
 * @Author: yangshiqi
 * @Description:
 * @Date: 2019/5/11 0011 14:53
 * @Version: 1.0
 */
@Data
@ConfigurationProperties(prefix="wx.pay.applet")
public class WxPayAppletConfig  {

    /**
     * 小程序appId
     */
    private String appId = "";
    /**
     * 小程序appSecret
     */
    private String appSecret ="";

    /**交易类型
     * 小程序取值如下：JSAPI，详细说明见参数规定
     */
    private String tradeType = "JSAPI";

    /**
     * 商户号
     */
    private String mchId = "";

    /**
     * 商户密钥
     */
    private String mchKey="";


    /**
     * 支付完成后的异步通知地址.
     */
    protected String notifyUrl ="";

    /**
     * 支付完成后的同步返回地址.
     */
    protected String returnUrl ="";

    /**
     * 商户证书路径
     */
    private String keyPath;
    /**
     * 证书内容
     */
    private SSLContext sslContext;

    public SSLContext initSSL() {
        InputStream inputStream = null;
        try {
            ClassPathResource classPathResource = new ClassPathResource(this.keyPath);
            inputStream = classPathResource.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException("[微信证书加载失败!]" + e.getMessage());
        }

        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            char[] partnerId2charArray = mchId.toCharArray();
            keyStore.load(inputStream, partnerId2charArray);
            this.sslContext = SSLContexts.custom().loadKeyMaterial(keyStore, partnerId2charArray).build();
            return this.sslContext;
        } catch (Exception e) {
            throw new RuntimeException("[证书内容错误!]" + e.getMessage());
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static void main(String[] args) throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("keyStore/apiclient_cert.p12");
        InputStream inputStream = classPathResource.getInputStream();
    }
}
