package com.youyuan.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;


/**
* 字符工具类
* @author yizhong.liao
* @createTime 2021/8/31 8:46
*/
@Slf4j
public class StringUtil {

    public static final String RANDOM_CODE_NUMERIC = "1";
    public static final String RANDOM_CODE_ALPHABETIC = "2";
    public static final String RANDOM_CODE_ALPHANUMERIC = "3";

    public static boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public static boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    public static String getStringValue(Object o) {
        return getStringValue(o, "");
    }

    public static String getStringValue(Object o, String defValueForNull) {
        if (null == o) {
            return defValueForNull;
        }
        return String.valueOf(o);
    }

    public static String genRandomCode(int len, String type){
        if (type.equals(RANDOM_CODE_NUMERIC)) {
            return RandomStringUtils.randomNumeric(len);
        } else if (type.equals(RANDOM_CODE_ALPHABETIC)) {
            return RandomStringUtils.randomAlphabetic(len);
        } else {
            return RandomStringUtils.randomAlphanumeric(len);
        }
    }

    public static String genUUID() {
        return UUID.randomUUID().toString();
    }

    public static String addLefPad(String str, int size, String padStr){
        return StringUtils.leftPad(str, size, padStr);
    }

    public static String removeLeftPad(String str, String padStr) {
        int len = str.length();
        int index = 0;
        char strs[] = str.toCharArray();
        for (int i = 0; i < len; i++) {
            if (!padStr.equals(String.valueOf(strs[i]))) {
                index = i;
                break;
            }
        }
        return str.substring(index);
    }

    public static String[] splitStr(String str, String token) {
        return StringUtils.split(str, token);
    }

    public static List<String> splitAsList(String str, String token){
        String[] array = splitStr(str, token);
        if (null == array) {
            return null;
        } else {
            return new ArrayList<>(Arrays.asList(array));
        }
    }

    public static String joinList(List<String> strList, String token){
        if (null == strList || strList.isEmpty()) {
            return "";
        }
        if (isEmpty(token)) {
            token = "";
        }
        return StringUtils.join(strList.toArray(), token);
    }

    public static String hashBy256(String str) {
        String s = Integer.toHexString(Math.abs(str.hashCode()) % 256).toUpperCase();
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }

    public static String urlEncode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("StringUtil.urlEncode exception occurs.", e);
        }
        return str;
    }

    public static String urlDecode(String str) {
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("StringUtil.urlDecode exception occurs.", e);
        }
        return str;
    }

    public static boolean checkMobileNumber(String mobileNo){
        if(isEmpty(mobileNo)){
            return false;
        }
        return Pattern.compile("^(1[3456789][0-9]\\d{8})$").matcher(mobileNo).matches();
    }

    public static void main(String... args) {
        String text = "16681586291";
        System.out.println(checkMobileNumber(text));
    }

}
