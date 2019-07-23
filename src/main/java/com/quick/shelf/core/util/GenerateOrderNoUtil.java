package com.quick.shelf.core.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 生成订单号 工具类
 */
public class GenerateOrderNoUtil {
    private static final String PRE = "XW";

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 生成订单号
     *
     * @param pre 订单号前缀
     * @return
     */
    public static String gens(Long museId) {

        //生成
        return GenerateOrderNoUtil.PRE + sdf.format(new Date()) + (1 + (int) (Math.random() * 10000)) + museId;
    }

    /**
     * 生成订单号
     *
     * @param pre 订单号前缀
     * @return
     */
    public static String gen( Long museId) {
        //生成
        return GenerateOrderNoUtil.PRE + ((int) ((Math.random() * 9 + 1) * 10000)) + museId + (System.currentTimeMillis() / 1000);
    }

    public static void main(String[] args) {
        System.out.println(gens( 530L));
        System.out.println(gen( 530L));
        System.out.println(gens( 530L).length());
        System.out.println(gen( 530L).length());
    }
}