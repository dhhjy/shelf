package com.quick.shelf.core.util.sms;

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

    // 产品访问标识
    private static String Access_Key_Id;
    // 产品访问密匙
    private static String Access_Key_Secet;
    // 发送短信的手机号码
    private static String Phone_Number;


    public static String send(SmsPortInfo smsPortInfo, String phoneNumber, String templateCode, String param) {
        Map<String,String> code = new HashMap<>();
        Access_Key_Id = smsPortInfo.getAccess_key_id();
        Access_Key_Secet = smsPortInfo.getAccess_key_secet();
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

