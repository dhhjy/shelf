package com.quick.shelf.core.util;

import java.util.Collection;

/**
 * Created by LinDexing on 2017/6/3 0003.
 */
public class CheckUtil {
    public static boolean isNull(Object obj) {
        boolean validate = false;
        if(obj == null) {
            validate = true;
        }

        return validate;
    }

    public static boolean isNotNull(Object obj) {
        boolean validate = false;
        if(obj != null) {
            validate = true;
        }

        return validate;
    }

    public static boolean isBlank(Object obj) {
        boolean validate = false;
        if(obj == null || obj.toString().trim().equals("")) {
            validate = true;
        }

        return validate;
    }

    public static boolean isNotBlank(Object obj) {
        boolean validate = false;
        if(obj != null && !obj.toString().trim().equals("")) {
            validate = true;
        }

        return validate;
    }

    public static <E> boolean isEmpty(Collection<E> obj) {
        boolean validate = false;
        if(obj == null || obj.size() <= 0) {
            validate = true;
        }

        return validate;
    }

    public static <E> boolean isNotEmpty(Collection<E> obj) {
        boolean validate = false;
        if(obj != null && obj.size() > 0) {
            validate = true;
        }

        return validate;
    }

    public static boolean isNotEmptyArray(Object[] obj) {
        boolean validate = false;
        if(obj != null && obj.length > 0) {
            validate = true;
        }

        return validate;
    }

    public static boolean isEmptyArray(Object[] obj) {
        boolean validate = false;
        if(obj == null || obj.length <= 0) {
            validate = true;
        }

        return validate;
    }

    public static boolean isUnsignedInt(Integer obj) {
        boolean validate = false;
        if(null != obj && obj >= 0) {
            validate = true;
        }

        return validate;
    }

    public static boolean isUnsignedShort(Short obj) {
        boolean validate = false;
        if(null != obj && obj >= 0) {
            validate = true;
        }

        return validate;
    }

    public static boolean isUnsignedLong(Long obj) {
        boolean validate = false;
        if(null != obj && obj >= 0L) {
            validate = true;
        }

        return validate;
    }
}
