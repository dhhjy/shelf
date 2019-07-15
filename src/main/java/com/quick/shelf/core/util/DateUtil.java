package com.quick.shelf.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by LinDexing on 2017/6/11 0011.
 */
public class DateUtil {
    /**
     * 每天的时间毫秒数
     */
    private static final long ONE_DATE_TIME_MILES  = 24 * 60 * 60 * 1000;

    /**
     * 获取当前的时间
     * @return
     */
    public static Date getCurrentDate(){
        return  new Date();
    }

    /**
     * 获取当前时间的字符串
     */
    public static String getCurrentDateString(){
        return formatDateToTimeString(getCurrentDate());
    }

    /**
     * 获取 N 天以后的日期
     * @param days 天数
     * @return Date
     */
    public static Date afterDate(Integer days){
        return  new Date(DateUtil.getCurrentDate().getTime() + days * ONE_DATE_TIME_MILES);
    }

    /**
     * 获取某一天（startDate）的，N天后的日期
     * @param startDate
     * @param days 天数
     * @return Date
     */
    public static Date afterDate(Date startDate,Integer days){
        return  new Date(startDate.getTime() + days * ONE_DATE_TIME_MILES);
    }

    /**
     * 获取 N 天以前的日期
     * @param days 天数
     * @return
     */
    public static Date beforeDate(Integer dates){
    	return  new Date(DateUtil.getCurrentDate().getTime() - dates * ONE_DATE_TIME_MILES);
    }

    /**
     * 获取某一天(startDate) 的 N天前日期
     * @param startDate
     * @param dates
     * @return
     */
    public static Date beforeDate(Date startDate,Integer dates){
    	return  new Date(startDate.getTime() - dates * ONE_DATE_TIME_MILES);
    }

    /**
     * Date 类型时间 格式化为 日期字符串 yyyy-MM-dd
     *
     * @param date
     * @return String
     */
    public static String formatDateToDateString(Date date ){
        if(CheckUtil.isNull(date)){
            return null;
        }else{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return dateFormat.format(date);
        }
    }
    
    /**
     * Date 类型时间 格式化为 时间字符串 yyyy-MM-dd hh:mm:ss
     */
    public static String formatDateToTimeString(Date date) {
    	 if(CheckUtil.isNull(date)){
             return null;
         }else{
             SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
             return dateFormat.format(date);
         }
    }
    
    /** 
    * @Description: yyyy年MM月dd日
    * @param 
    * @return 
    */
    public static String formatDateToChineseString(Date date ){
    	if(CheckUtil.isNull(date)){
    		return null;
    	}else{
    		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日");
    		return dateFormat.format(date);
    	}
    }
    
    /**
     * 将时间格式化为 YYYYMMDDH24MISS
     * @author         : LinDexing
     * @date           : 2017/7/23 0023 22:57
     * @param date
     * @return
     */
    public static String formatDateToLianpayString(Date date ) {
        if (CheckUtil.isNull(date)) {
            return null;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("YYYYMMddHHmmss");
            return dateFormat.format(date);
        }
    }

    /**
     * 获得两个时间的期间隔
     * @author         : LinDexing
     * @date           : 2017/8/21 0021 14:31
     * @param start
     * @param end
     * @return
     */
    public static int calculateDuration(Date start,Date end){
        return (int)((start.getTime() - end.getTime()) / ONE_DATE_TIME_MILES);
    }

    /**
     * 获取当前时间str，格式yyyyMMddHHmmss
     * @return
     * @author guoyx
     */
    public static String getNowTime(String tyle)
    {
        SimpleDateFormat dataFormat = new SimpleDateFormat(tyle);
        Date date = new Date();
        return dataFormat.format(date);
    }

    /**
     * 获取当前时间戳，单位毫秒
     * @return
     */
    public static long getCurrentTimestampMs() {
        return System.currentTimeMillis();
    }

    public static long timestampToDate() {
        return System.currentTimeMillis()/1000;

    }

    /**
     * 时间字符串 转 Date
     * @param dateStr yyyy-MM-dd hh:mm:ss 格式
     * @return
     */
    public static Date getStringToDate(String dateStr){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            return simpleDateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
