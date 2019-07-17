package com.quick.shelf.core.util;

import cz.mallat.uasparser.OnlineUpdater;
import cz.mallat.uasparser.UASparser;
import cz.mallat.uasparser.UserAgentInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *	UserAgent获取和解析工具类,jdk1.7及以上版本支持
 */
public class UserAgentUtil {
    private static final Logger LOG = LoggerFactory.getLogger(UserAgentUtil.class);
    private static UASparser uasParser = null;
    public static String android="Android";
    public static String iphone="iPhone";
    public static String ipad="iPad";
    public static final String noDevice="未知设备";

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    private static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i"
            +"|windows (phone|ce)|blackberry"
            +"|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            +"|laystation portable)|nokia|fennec|htc[-_]"
            +"|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    private static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser"
            +"|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";

    //移动设备正则匹配：手机端、平板
    private static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    private static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    static {
        try {
            uasParser = new UASparser(OnlineUpdater.getVendoredInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 检测是否是移动设备访问
     *
     * @Title: check
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     */
    public static boolean checkMobileOrPC(HttpServletRequest request){
        String userAgent=request.getHeader("user-agent").toLowerCase();
        System.out.println("==========================终端判断开始========================================");
        System.out.println("设备检测："+userAgent);
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        boolean result=false;
        if(matcherPhone.find() || matcherTable.find()){
            result = true;
        }
        System.out.println("===========================终端判断结束=======================================");
        return result;
    }

    /**
     * 检测是否是移动设备访问
     *
     * @Title: check
     * @param userAgent 浏览器标识
     * @return true:移动设备接入，false:pc端接入
     */
    public static boolean checkMobileOrPC(String userAgent){
        System.out.println("设备检测："+userAgent);
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if(matcherPhone.find() || matcherTable.find()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * 设备类型     Smartphone/Personal computer
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getDeviceType(HttpServletRequest request) throws IOException{
        String userAgent=request.getHeader("user-agent").toLowerCase();
        UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
        LOG.debug("设备类型：" + userAgentInfo.getDeviceType());
        return userAgentInfo.getDeviceType();
    }

    /**
     * 操作系统名称    Android/Windows/iOS
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getOsName(HttpServletRequest request) throws IOException{
        String userAgent=request.getHeader("user-agent").toLowerCase();
        UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
        LOG.debug("操作系统详细名称：" + userAgentInfo.getOsName());
        return userAgentInfo.getOsName();
    }

    /**
     * 浏览器名称和版本      Chrome Mobile 53.0.2785.146/Chrome 63.0.3239.84/Android browser 4.0
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getUaName(HttpServletRequest request) throws IOException{
        String userAgent=request.getHeader("user-agent").toLowerCase();
        UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
        LOG.debug("浏览器名称和版本：" + userAgentInfo.getUaName());
        return userAgentInfo.getUaName();
    }

    /**
     * 检查是否是微信浏览器
     * @param request
     * @return
     */
    public static boolean isWeChatBrowser(HttpServletRequest request){
        String userAgent=request.getHeader("user-agent").toLowerCase();
        return userAgent.toLowerCase().indexOf("micromessenger")>-1?true:false;
    }

    /**
     * 获取手机型号     ios和ipad不判断具体型号
     * @param request
     * @return
     */
    public static String getMobileModel(HttpServletRequest request){
        String userAgent=request.getHeader("user-agent").toLowerCase();
        //获取手机型号开始**************************************************************************
        // 获取Android手机型号
        Pattern pattern = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");
        Matcher matcher = pattern.matcher(userAgent);
        String mobileModel = null;
        if (matcher.find()) {
            mobileModel = matcher.group(1).trim();
        }

        // 判断iphone和ipad
        if (userAgent.indexOf("iphone") > 0) {
            mobileModel = "iphone";
        }
        if (userAgent.indexOf("ipad") > 0) {
            mobileModel = "ipad";
        }
        //获取手机型号结束**************************************************************************
        return mobileModel;
    }

    /**
     * 浏览器类型       Mobile Browser/Browser
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getBrowerType(HttpServletRequest request) throws IOException{
        String userAgent=request.getHeader("user-agent").toLowerCase();
        UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
        LOG.debug("类型：" + userAgentInfo.getType());
        return userAgentInfo.getType();
    }

    /**
     * 浏览器版本  Chrome Mobile 53.0.2785.146/Chrome 63.0.3239.84
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getBrowserVersionInfo(HttpServletRequest request) throws IOException{
        String userAgent=request.getHeader("user-agent").toLowerCase();
        UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
        LOG.debug("浏览器版本：" + userAgentInfo.getBrowserVersionInfo());
        return userAgentInfo.getBrowserVersionInfo();
    }

    /**
     * 获取移动用户操作系统
     * @param userAgent
     * @return
     */
    public static String getMobileOS(HttpServletRequest request){
        String userAgent=request.getHeader("user-agent").toLowerCase();
        if (userAgent.contains(android)) {
            return android;
        }else if (userAgent.contains(iphone)){
            return iphone;
        }else if (userAgent.contains(ipad)){
            return ipad;
        }else {
            return "others";
        }
    }

    /**
     * 操作系统家族
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getOsFamily(HttpServletRequest request) throws IOException{
        String userAgent=request.getHeader("user-agent").toLowerCase();
        UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
        LOG.debug("操作系统家族：" + userAgentInfo.getOsFamily());
        return userAgentInfo.getOsFamily();
    }

    /**
     * 浏览器名称
     * @param userAgent
     * @return
     * @throws IOException
     */
    public static String getUaFamily(HttpServletRequest request) throws IOException{
        String userAgent=request.getHeader("user-agent").toLowerCase();
        UserAgentInfo userAgentInfo = UserAgentUtil.uasParser.parse(userAgent);
        LOG.debug("浏览器名称：" + userAgentInfo.getUaFamily());
        return userAgentInfo.getUaFamily();
    }

}
