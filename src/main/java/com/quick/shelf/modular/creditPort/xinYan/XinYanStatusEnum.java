package com.quick.shelf.modular.creditPort.xinYan;


/**
 * 新颜查询结果状态枚举
 */
public enum XinYanStatusEnum {
    SUCCESS("获取用户数据成功","20000"),
    PARAMETER_NULL("必填参数不能为空","19999"),
    APIENC_FALSE("验签(apiEnc)不正确","19991"),
    TASK_NOT_EXIST("任务不存在","49999");


    private String msg;
    private String code;

    XinYanStatusEnum(String msg, String code){
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
