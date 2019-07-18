package com.quick.shelf.core.common.annotion;

import java.lang.annotation.*;

/**
 * 回调注解，用于记录回调系统内的回调函数执行情况
 *
 * @author zcn
 * @date 2019/07/19
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface CallBackLog {
    /**
     * 回调作用名称
     */
    String value() default "";
}
