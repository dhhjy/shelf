package com.quick.shelf.core.util;

import cn.stylefeng.roses.core.util.SpringContextHolder;
import com.quick.shelf.config.properties.ShelfProperties;

/**
 * 验证码工具类
 */
public class KaptchaUtil {

    /**
     * 获取验证码开关
     */
    public static Boolean getKaptchaOnOff() {
        return SpringContextHolder.getBean(ShelfProperties.class).getKaptchaOpen();
    }
}