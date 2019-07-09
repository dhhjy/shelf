package com.quick.shelf.core.log;

import com.alibaba.druid.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * nginx 反向代理之后，获取真实IP
 */
public class LogIp {
    public static String getIpAddr(HttpServletRequest request) {
        String ip;
        int index;
        try {
            ip = request.getHeader("x-forwarded-for");
            // Proxy-Client-IP 这个一般是经过apache http服务器的请求才会有，用apache http做代理时一般会加上Proxy-Client-IP请求头，而WL-Proxy-Client-IP是他的weblogic插件加上的头。
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
            }
            if (StringUtils.isEmpty(ip)) {
                return "";
            }
            index = ip.indexOf(",");

            if (index != -1) {
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } catch (Exception e) {
            return "";
        }
    }
}
