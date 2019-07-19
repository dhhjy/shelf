package com.quick.shelf.core.util.sms.jiGuang;

import com.quick.shelf.core.util.sms.aliYun.AliYunSmsConst;

public enum JiGuangSmsConst {
    // 用户注册
    SMS_JG_YHZC("register", "1"),
    // 登陆通知
    SMS_JG_DLTZ("loginNotice", "165591");

    private String type;             //短信类型
    private String templateCode;     //短信模板编号

    JiGuangSmsConst(String type, String templateCode) {
        this.type = type;
        this.templateCode = templateCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }


    /**
     * 比较type,返回相同type的短信模板
     *
     * @param apiName 服务标识
     * @return
     */
    public static String getSmsTemplateCode(String type) {
        if (null == type)
            return "无type";

        if (type.equals(JiGuangSmsConst.SMS_JG_YHZC.getType()))
            return JiGuangSmsConst.SMS_JG_YHZC.getTemplateCode();

        if (type.equals(JiGuangSmsConst.SMS_JG_DLTZ.getType()))
            return JiGuangSmsConst.SMS_JG_DLTZ.getTemplateCode();

        return null;
    }

    /**
     * 获取短信类型
     *
     * @return 1-验证 ；2-通知
     */
    public static Integer getSmsType(String type) {
        if (null == type)
            return 0;

        if (type.equals(JiGuangSmsConst.SMS_JG_YHZC.getType()))
            return 1;

        if (type.equals(JiGuangSmsConst.SMS_JG_DLTZ.getType()))
            return 2;

        return 0;
    }
}
