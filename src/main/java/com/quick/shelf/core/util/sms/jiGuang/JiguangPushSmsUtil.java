package com.quick.shelf.core.util.sms.jiGuang;

import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jsms.api.JSMSClient;
import cn.jsms.api.common.model.SMSPayload;
import com.quick.shelf.core.util.sms.entity.SmsPortInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * 极光推送
 */
public class JiguangPushSmsUtil {

    //极光推送短信平台
    public static final Integer JIGUANG = 2;
    // 产品访问标识
    private static final String Access_Key_Id = "92937259e7869f9efd7790b3";
    // 产品访问密匙
    private static final String Access_Key_Secet = "0d376c5083e5464f2587a603";
    // 发送短信的手机号码
    private static String Phone_Number;

    public static String send(String phoneNumber, String templateCode, String param) {
        Map<String,String> code = new HashMap<>();
        Phone_Number = phoneNumber;
        if(param != null && !"".equals(param))
        {
            code.put("code",param);
        }
        return sendAuthCode(Integer.valueOf(templateCode),code);
    }

    /**
     * 极光推送发送验证码短信方法
     * @param templateCode 模板编号
     * @param code Map格式的验证码
     */
    private static String sendAuthCode( Integer templateCode,Map<String, String> code) {
        JSMSClient jsmsClient = new JSMSClient(Access_Key_Secet, Access_Key_Id);
        SMSPayload smsPayload = SMSPayload.newBuilder()
                .setMobildNumber(Phone_Number)
                .setTempId(templateCode)
                .setTempPara(code)
                .build();//new SMSPayload(mobileNumber,tempId,ttl,code,voiceLang,temp_para);
        try {
            jsmsClient.sendTemplateSMS(smsPayload);
            return "OK";
        } catch (APIConnectionException | APIRequestException e) {
            e.printStackTrace();
            return "no";
        }
    }
}

