package com.quick.shelf.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName RealIPUtil 当使用ngix时，无法获取真实的IP地址，通过此类解决
 * @Description TODO
 * @Author ken
 * @Date 2019/7/14 11:47
 * @Version 1.0
 */
public class RealIPUtil {
    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(RealIPUtil.class);

    /**
     * 获取真实IP地址
     *
     * @param request
     * @return
     */
    public static String getRemoteHost(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        logger.info("getIpAddress(HttpServletRequest) - X-Forwarded-For - String ip="
                + ip);
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
                logger.info("getIpAddress(HttpServletRequest) - Proxy-Client-IP - String ip="
                        + ip);
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
                logger.info("getIpAddress(HttpServletRequest) - WL-Proxy-Client-IP - String ip="
                        + ip);
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
                logger.info("getIpAddress(HttpServletRequest) - HTTP_CLIENT_IP - String ip="
                        + ip);
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
                logger.info("getIpAddress(HttpServletRequest) - HTTP_X_FORWARDED_FOR - String ip="
                        + ip);
            }
            if (ip == null || ip.length() == 0
                    || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                logger.info("getIpAddress(HttpServletRequest) - getRemoteAddr - String ip="
                        + ip);
            }
        } else if (ip.length() > 15) {
            String[] ips = ip.split(",");
            for (String s : ips) {
                if (!("unknown".equalsIgnoreCase(s))) {
                    ip = s;
                    break;
                }
            }
        }
        return ip;
    }
}
