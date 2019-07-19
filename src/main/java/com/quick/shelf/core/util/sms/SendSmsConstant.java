package com.quick.shelf.core.util.sms;

/**
 * 短信发送枚举，默认使用阿里云短信的参数
 */
public enum SendSmsConstant {
    AUTH_CODE("用户注册验证码", "TEMPLATE_CODE_7"),
    AUTHENTICATION_CODE("身份验证验证码", "TEMPLATE_CODE_8"),
    CHANGE_PASSWORD_CODE("修改密码验证码", "TEMPLATE_CODE_17"),
    LOGIN_INFORM("登录通知", "TEMPLATE_CODE_9"),
    REGISTER_INFORM("注册通知", "TEMPLATE_CODE_15");
    // 成员变量
    private String name;
    private String option;
    // 构造方法
    SendSmsConstant(String name, String option) {
        this.name = name;
        this.option = option;
    }

    /**
     * 由于短信接口识别第一位为0的验证码会失效，所以，此方法
     * 不返回任何包含0的验证码
     */
    public static String getAuthCode() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 6; i++) {
            int n = (int) ((Math.random() * 9) + 1);
            sb.append(n);
        }
        return sb.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getOption() {
        return this.option;
    }
}