package com.blue.rchina.utils;

import java.net.URLEncoder;

/**
 * Created by cj on 2017/10/31.
 */

public class SignUtils {

    public static String regularKey = "temp123456_bpt2_rong";
    public static String App_ID = "uc1b58cea86aa423gx";

    public static String sign(String data, String key) {
        String encode = "";
        byte[] b = HMACSHA1.getHmacSHA1(data,key);
        String s = new BASE64Encoder().encode(b);

        try {
            encode = URLEncoder.encode(s, "UTF-8");
        } catch (Exception ex) {
            System.out.print(ex);
        }
        return encode;
    }
}
