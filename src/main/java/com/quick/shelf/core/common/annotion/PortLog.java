package com.quick.shelf.core.common.annotion;

import java.lang.annotation.*;

/**
 * 标记需要做接口统计的方法
 *
 * @author zcn
 * @date 2019/07/15
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PortLog {
    /**
     * 类型标识
     */
    String type() default "";

    /**
     * 类型标识中文名称
     */
    String typeName() default "";
}
