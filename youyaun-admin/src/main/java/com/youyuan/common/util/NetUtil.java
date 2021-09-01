package com.youyuan.common.util;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
*
* @author yizhong.liao
* @createTime 2021/9/1 16:41
*/
public class NetUtil {

    private static Logger logger = LoggerFactory.getLogger(NetUtil.class);

    private static final String UNKNOWN = "unknown";
    private static final String COLON = ":";


    public static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (StringUtil.isEmpty(ip)) {
            String[] ips = StringUtil.splitStr(ip, ",");
            if (ips.length > 1) {
                logger.warn("NetUtil.getIpAddress find more than 1 ip address, original ip string : " + ip);
                ip = ips[0];
            }
            if (ip.contains(COLON)) {
                ip = ip.substring(0,ip.indexOf(COLON));
            }
        }
        return ip;
    }

}
