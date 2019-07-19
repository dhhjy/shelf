package com.quick.shelf.core.util.sms;


/**
 * 阿里短信模板枚举
 * 需要与 AliYunSmsConst 枚举类配合使用
 * 这些内容都定义在阿里云短信云平台中，
 * 修改云平台需要同步修改本枚举类
 *
 * @author zcn
 * @data 2019/07/19
 */
public enum SmsTemplateEnum {
    // 用户注册
    SMS_ALIYUN_CODE_YHZC("SMS_141915142", "验证码${code}，您正在注册成为新用户，感谢您的支持！"),
    // 极光推送
    SMS_JG_CODE_YHZC("1", "您的手机验证码：{{code}}，有效期5分钟，请勿泄露。如非本人操作，请忽略此短信。谢谢！"),
    //身份验证
    SMS_ALIYUN_CODE_SFYZ("SMS_141915145", "验证码${code}，您正在进行身份验证，打死不要告诉别人哦！"),
    // 修改密码
    SMS_ALIYUN_CODE_XGMM("SMS_141915141", "验证码${code}，您正在尝试修改登录密码，请妥善保管账户信息！"),
    // 登陆通知
    SMS_ALIYUN_CODE_DLTZ("SMS_141915143", "您正在登录APP，若非本人操作，请修改密码。"),
    // 注册通知
    SMS_ALIYUN_CODE_ZCTZ("SMS_167972980", "尊敬的用户,您已经注册成功,快去下载APP体验吧！"),
    // 极光注册通知
    SMS_JG_CODE_ZCTZ("165591", "尊敬的用户,您已经注册成功,快去下载APP体验吧！");

    private String templateCode;             //短信类型
    private String content;     //短信模板编号

    SmsTemplateEnum(String templateCode, String content) {
        this.templateCode = templateCode;
        this.content = content;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 比较templateCode  返回与模板编号一致的模板内容
     * 内容保存的目的是为了查看，留档
     *
     * @param apiName 服务标识
     * @return
     */
    public static String getContentByTempCode(String templateCode) {
        if (null == templateCode)
            return "无模板";

        if (templateCode.equals(SmsTemplateEnum.SMS_ALIYUN_CODE_YHZC.getTemplateCode()))
            return SmsTemplateEnum.SMS_ALIYUN_CODE_YHZC.getContent();

        if (templateCode.equals(SmsTemplateEnum.SMS_ALIYUN_CODE_SFYZ.getTemplateCode()))
            return SmsTemplateEnum.SMS_ALIYUN_CODE_SFYZ.getContent();

        if (templateCode.equals(SmsTemplateEnum.SMS_ALIYUN_CODE_XGMM.getTemplateCode()))
            return SmsTemplateEnum.SMS_ALIYUN_CODE_XGMM.getContent();

        if (templateCode.equals(SmsTemplateEnum.SMS_ALIYUN_CODE_DLTZ.getTemplateCode()))
            return SmsTemplateEnum.SMS_ALIYUN_CODE_DLTZ.getContent();

        if (templateCode.equals(SmsTemplateEnum.SMS_ALIYUN_CODE_ZCTZ.getTemplateCode()))
            return SmsTemplateEnum.SMS_ALIYUN_CODE_ZCTZ.getContent();

        if (templateCode.equals(SmsTemplateEnum.SMS_JG_CODE_YHZC.getTemplateCode()))
            return SmsTemplateEnum.SMS_JG_CODE_YHZC.getContent();

        if (templateCode.equals(SmsTemplateEnum.SMS_JG_CODE_ZCTZ.getTemplateCode()))
            return SmsTemplateEnum.SMS_JG_CODE_ZCTZ.getContent();

        return null;
    }
}
