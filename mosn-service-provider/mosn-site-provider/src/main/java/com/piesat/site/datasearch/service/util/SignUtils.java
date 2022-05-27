package com.piesat.site.datasearch.service.util;

import java.security.MessageDigest;
import java.util.*;

/**
 * sign标签工具类
 */
public class SignUtils {

    /**
     * 对检索参数按键值key进行排序，拼接为参数字符串，然后进行MD5加密生成sign
     * @param params 检索参数
     * @return
     */
    public static String getSign(Map<String, Object> params) {
        String sign = "";
        String paramString = "";
        //业务请求参数拼接
        Collection<String> keyset = params.keySet();
        List<String> list = new ArrayList<String>(keyset);
        Collections.sort(list);//对key键值按字典升序排序
        String key = "";
        for (int i = 0; i < list.size(); i++) {
            key = list.get(i);
            if (key != null && !key.trim().equalsIgnoreCase("")) {
                if (key.equalsIgnoreCase("params")) {
                    paramString += params.get(key).toString().trim();
                    paramString += "&";
                } else {
                    paramString += key.trim() + "=" + String.valueOf(params.get(key)).trim();
                    paramString += "&";
                }
            }
        }
        if (!paramString.equals("")) {
            paramString = paramString.substring(0, paramString.length() - 1);
        }
        //进行MD5运算
        MessageDigest md5;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            byte[] byteArray = md5.digest(paramString.getBytes("utf-8"));

            StringBuffer md5StrBuff = new StringBuffer();
            //将加密后的byte数组转换为十六进制的字符串,否则的话生成的字符串会乱码
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
            sign = md5StrBuff.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }
}
