package com.quick.shelf.modular.system.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.core.util.RedisUtil;
import com.quick.shelf.core.util.sms.SendSmsConstant;
import com.quick.shelf.core.util.sms.SmsTemplateEnum;
import com.quick.shelf.core.util.sms.aliYun.SendAliSmsUtil;
import com.quick.shelf.core.util.sms.aliYun.AliYunSmsConst;
import com.quick.shelf.core.util.sms.jiGuang.JiGuangSmsConst;
import com.quick.shelf.core.util.sms.jiGuang.JiguangPushSmsUtil;
import com.quick.shelf.modular.system.entity.SmsMessage;
import com.quick.shelf.modular.system.mapper.ShortMessageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @ClassName 短信业务层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/19 0:11
 * @Version 1.0
 */
@Service
public class ShortMessageService extends ServiceImpl<ShortMessageMapper, SmsMessage> {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(ShortMessageService.class);

    @Resource
    private DictService dictService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 通过阿里云短信接口发送短信
     * 同时会统计短信接口
     *
     * @param phone 待发送的手机号码
     * @param type  发送短信的类型
     * @return
     */
    public String sendAliYunSms(String phone, String type, Long deptId) {
        // 通过工具类获得发送短信的模板编号
        String templateCode = AliYunSmsConst.getSmsTemplateCode(type);
        // 获取验证码
        String code = SendSmsConstant.getAuthCode();
        String result = SendAliSmsUtil.send(phone, templateCode, code);
        // 判断短信是否发送成功
        if (null != result && result.equals("OK")) {
            //将验证码放入缓存 60s * 5 有效期5分钟
            redisUtil.set(phone, code, 60 * 5);
            new Thread(() -> insertSmsMessage(phone, type, templateCode, SendAliSmsUtil.ALIYUN, deptId, code)).start();
            logger.info("发送成功！手机号：{} 通过阿里云平台发送：{}类型验证码，本次验证码：{}", phone, type, code);
            return "发送成功";
        } else {
            logger.info("发送失败！手机号：{} 通过阿里云平台发送：{}类型验证码", phone, type);
            return "发送失败";
        }
    }

    /**
     * 通过极光推送接口发送短信
     *
     * @param phone
     * @param type
     * @return
     */
    public String sendJiGuangSms(String phone, String type, Long deptId) {
        // 通过工具类获得发送短信的模板编号
        String templateCode = JiGuangSmsConst.getSmsTemplateCode(type);
        // 获取验证码
        String code = SendSmsConstant.getAuthCode();
        String result = JiguangPushSmsUtil.send(phone, templateCode, code);
        // 判断短信是否发送成功
        if (result.equals("OK")) {
            //将验证码放入缓存 60s * 5 有效期5分钟
            redisUtil.set(phone, code, 60 * 5);
            new Thread(() -> insertSmsMessage(phone, type, templateCode, JiguangPushSmsUtil.JIGUANG, deptId, code)).start();
            logger.info("发送成功！手机号：{} 通过阿里云平台发送：{}类型验证码，本次验证码：{}", phone, type, code);
            return "发送成功";
        } else {
            logger.info("发送失败！手机号：{} 通过阿里云平台发送：{}类型验证码", phone, type);
            return "发送失败";
        }
    }

    /**
     * 增加短信内容
     *
     * @param phone        本次短信的手机号
     * @param type         发送短信的类型
     * @param templateCode 发送短信的模板编号
     * @param code         发送的验证码
     */
    private void insertSmsMessage(String phone, String type, String templateCode, Integer operator, Long deptId, String code) {
        // 1-验证短信， 2-通知短信
        // 获得模板内容
        String content = SmsTemplateEnum.getContentByTempCode(templateCode);

        // 将本次操作记录到数据库中
        SmsMessage smsMessage = new SmsMessage();
        smsMessage.setPhone(phone);
        // 短信类型
        smsMessage.setSmsType(AliYunSmsConst.getSmsType(type));
        assert content != null;
//        if(operator == SendAliSmsUtil.ALIYUN)
//        smsMessage.setContent(content.replace("${code}", code));
//
//        if()

        smsMessage.setContent(content.replace("${code}", code).replace("{{code}}", code));
        // 短信平台
        smsMessage.setOperator(operator);
        // 短信模板编号
        smsMessage.setTemplateCode(templateCode);
        // 短信价格
        smsMessage.setUnitPrice(Double.valueOf(this.dictService.selectDictByName(templateCode).getCode()));
        // 注册到哪一家公司的短信
        smsMessage.setDeptId(deptId);
        // 短信发送时间
        smsMessage.setCreateTime(DateUtil.getCurrentDate());
        this.baseMapper.insert(smsMessage);
    }
}
