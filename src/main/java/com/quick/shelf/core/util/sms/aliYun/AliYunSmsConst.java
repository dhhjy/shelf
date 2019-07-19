package com.quick.shelf.core.util.sms.aliYun;

/**
 * @ClassName AliYunSmsConst
 * @Description TODO
 * @Author ken
 * @Date 2019/7/19 0:17
 * @Version 1.0
 */
public enum AliYunSmsConst {
    // 用户注册
    SMS_ALIYUN_YHZC("register", "SMS_141915142"),
    //身份验证
    SMS_ALIYUN_SFYZ("auth", "SMS_141915145"),
    // 修改密码
    SMS_ALIYUN_XGMM("changePassword", "SMS_141915141"),
    // 登陆通知
    SMS_ALIYUN_DLTZ("loginNotice", "SMS_141915143"),
    // 注册通知
    SMS_ALIYUN_ZCTZ("registerNotice", "SMS_167972980");

    private String type;             //短信类型
    private String templateCode;     //短信模板编号

    AliYunSmsConst(String type, String templateCode) {
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

        if (type.equals(AliYunSmsConst.SMS_ALIYUN_YHZC.getType()))
            return AliYunSmsConst.SMS_ALIYUN_YHZC.getTemplateCode();

        if (type.equals(AliYunSmsConst.SMS_ALIYUN_SFYZ.getType()))
            return AliYunSmsConst.SMS_ALIYUN_SFYZ.getTemplateCode();

        if (type.equals(AliYunSmsConst.SMS_ALIYUN_XGMM.getType()))
            return AliYunSmsConst.SMS_ALIYUN_XGMM.getTemplateCode();

        if (type.equals(AliYunSmsConst.SMS_ALIYUN_DLTZ.getType()))
            return AliYunSmsConst.SMS_ALIYUN_DLTZ.getTemplateCode();

        if (type.equals(AliYunSmsConst.SMS_ALIYUN_ZCTZ.getType()))
            return AliYunSmsConst.SMS_ALIYUN_ZCTZ.getTemplateCode();

        return null;
    }

    /**
     * 获取短信类型
     * @return 1-验证 ；2-通知
     */
    public static Integer getSmsType(String type){
        if (null == type)
            return 0;

        if (type.equals(AliYunSmsConst.SMS_ALIYUN_YHZC.getType())
                || type.equals(AliYunSmsConst.SMS_ALIYUN_SFYZ.getType())
                || type.equals(AliYunSmsConst.SMS_ALIYUN_XGMM.getType()))
            return 1;

        if (type.equals(AliYunSmsConst.SMS_ALIYUN_DLTZ.getType())
                || type.equals(AliYunSmsConst.SMS_ALIYUN_ZCTZ.getType()))
            return 2;

        return 0;
    }
}
