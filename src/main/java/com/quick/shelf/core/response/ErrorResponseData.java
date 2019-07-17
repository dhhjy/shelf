package com.quick.shelf.core.response;

/**
 * @ClassName ErrorResponseData
 * @Description TODO
 * @Author ken
 * @Date 2019/7/13 11:24
 * @Version 1.0
 */

public class ErrorResponseData extends ResponseData {
    private String exceptionClazz;

    public ErrorResponseData(String message) {
        super(false, ResponseData.DEFAULT_ERROR_CODE, message, (Object)null);
    }

    public ErrorResponseData(Integer code, String message) {
        super(false, code, message, (Object)null);
    }

    public ErrorResponseData(Integer code, String message, Object object) {
        super(false, code, message, object);
    }

    public String getExceptionClazz() {
        return this.exceptionClazz;
    }

    public void setExceptionClazz(final String exceptionClazz) {
        this.exceptionClazz = exceptionClazz;
    }
}