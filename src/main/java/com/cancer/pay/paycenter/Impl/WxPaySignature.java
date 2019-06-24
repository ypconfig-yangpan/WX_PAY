package com.cancer.pay.paycenter.Impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @ProjectName: pay
 * @Package: com.cancer.pay.paycenter.Impl.WxPaySignature
 * @ClassName: WxPaySignature
 * @Author: yangshiqi
 * @Description: ${description}
 * @Date: 2019/5/11 0011 10:59
 * @Version: 1.0
 */
public class WxPaySignature {
    /**
     * 签名
     * @param params
     * @param signKey
     * @return
     */
    public static String sign(Map<String, String> params, String signKey) {
        SortedMap<String, String> sortedMap = new TreeMap<>(params);

        StringBuilder toSign = new StringBuilder();
        for (String key : sortedMap.keySet()) {
            String value = params.get(key);
            if (StringUtils.isNotEmpty(value) && !"sign".equals(key) && !"key".equals(key)) {
                toSign.append(key).append("=").append(value).append("&");
            }
        }

        toSign.append("key=").append(signKey);

        return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
    }

    /**
     * 校验签名
     * @param params
     * @param signKey
     * @return
     */
    public static Boolean verify(Map<String, String> params, String signKey) {
        String sign = sign(params, signKey);
        return sign.equals(params.get("sign"));
    }

    /**
     * base64 编码
     */

    public static String Base64Encoder(String encoder){
        String encode = Base64.getEncoder().encodeToString(encoder.getBytes());
        return encode;
    }

    /**
     * base64 编码
     */

    public static byte[] Base64Decoder(String encoder){
        byte[] decode = Base64.getDecoder().decode(encoder);
        return decode;
    }

    // 反馈加密
    public static final String AES_ALGORITHM = "AES/ECB/PKCS7Padding";
//    public static final String AES_ALGORITHM = "AES/ECB/PKCS7Padding";
    public static final String UTF_8 = "UTF-8";
    /**
     * 对称解密数据 CFB
     *
     * @param content
     * @param md5Key
     * @return
     */
    public static String decryptAES(String content, String md5Key) throws Exception {


        SecretKeySpec key = new SecretKeySpec(md5Key.getBytes(), UTF_8);
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        Base64.Decoder decoder = Base64.getDecoder();
        String newData= new String(decoder.decode(content), "ISO-8859-1");
        byte[] bytes = newData.getBytes("ISO-8859-1");
        return new String(cipher.doFinal(bytes), UTF_8);
    }


}
