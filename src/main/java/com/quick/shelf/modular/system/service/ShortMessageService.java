package com.quick.shelf.modular.system.service;

import com.quick.shelf.core.util.sms.SendSmsConstant;
import com.quick.shelf.core.util.sms.aliYun.SendAliSmsUtil;
import com.quick.shelf.core.util.sms.aliYun.SmsAliYunConst;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * @ClassName 短信业务层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/19 0:11
 * @Version 1.0
 */
@Service
public class ShortMessageService {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(ShortMessageService.class);

    /**
     * 通过阿里云短信接口发送短信
     *
     * @param phone 待发送的手机号码
     * @param type  发送短信的类型
     * @return
     */
    public String sendAliYunSms(String phone, String type) {
        type = SmsAliYunConst.compareType(type);
        // 获取验证码
        String code = SendSmsConstant.getAuthCode();
        String result = SendAliSmsUtil.send(phone, type, code);
        logger.info("手机号：{} 通过阿里云平台发送：{}类型验证码，本次验证码：{}", phone, type, code);
        return result;
    }

    /**
     * 通过极光推送接口发送短信
     *
     * @param phone
     * @param type
     * @return
     */
    public String sendJiGuangSms(String phone, String type) {
        return null;
    }
}
