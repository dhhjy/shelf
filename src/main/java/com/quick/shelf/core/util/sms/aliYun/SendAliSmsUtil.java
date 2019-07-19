package com.quick.shelf.core.util.sms.aliYun;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.Data;

/**
 * 阿里云短信接口，发送工具类
 * 阿里云不支持营销短信
 * 阿里云短信接口只能发送 验证码短信、通知短信
 */
@Data
public class SendAliSmsUtil {

    //短信运用平台
    public static final Integer ALIYUN = 1;
    // 产品名称:云通信短信API产品,开发者无需替换
    private static final String PRODUCT = "Dysmsapi";
    // 产品域名,开发者无需替换
    private static final String DOMAIN = "dysmsapi.aliyuncs.com";

    private static final String ACTION = "SendSms";
    // 产品访问标识
    private static String Access_Key_Id = "LTAI6LXO8tEQNGB7";
    // 产品访问密匙
    private static String Access_Key_Secet = "H047SquqGa9uaIVNa06G6sy3qsvbQD";
    // 产品签名
    private static String Sign_Name = "鑫炜";
    // 发送短信的手机号码
    private static String Phone_Number;

    public static void main(String[] args){
        send("18699996820","SMS_141915142","1234");
    }

    /**
     * code值 不为空，发送验证码短信，code值为空发送通知短信，aliyun不支持发送营销短信
     * @param smsPortInfo   接口对象
     * @param phoneNumber   待发送的手机号码
     * @param templateCode  模板编号
     * @param code  验证码
     * @return
     * @throws ClientException
     */
    public static String send(String phoneNumber, String templateCode, String code){
        Phone_Number = phoneNumber;
        if (code != null && !"".equals(code))
            code = "{code:" + code + "}";
        try {
            return sendAuthCode(templateCode,code);
        } catch (ClientException e) {
            e.printStackTrace();
            return "发送失败";
        }
    }

    /**
     * 发送验证码短信方法,或发送通知短信
     * 当没有 paramCode 存在的时候，默认发送通知短信
     *
     * @param templateCode 模板编号
     * @param paramCode JSON格式的验证码 例:{"code":"12345"}
     * @return
     * @throws ClientException
     */
    private static String sendAuthCode( String templateCode, String paramCode) throws ClientException {
        // 组装请求对象-具体描述见控制台-文档部分内容
        CommonRequest request = buildCommonRequest();
        request.putQueryParameter("TemplateCode",templateCode);
        if(paramCode != null && !"".equals(paramCode))
            // 可选:模板中的变量替换JSON串,如模板内容为"尊敬的用户,您的验证码为${code}"时,此处的值为
            request.putQueryParameter("TemplateParam",paramCode);
        // hint 此处可能会抛出异常，注意catch
        return JSONObject.parseObject(InitClient().getCommonResponse(request).getData()).getString("Message");
    }

    /**
     * 初始化短信接口参数
     * @return
     */
    private static CommonRequest buildCommonRequest(){
        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain(DOMAIN);
        // 看不懂
        request.setVersion("2017-05-25");
        request.setAction(ACTION);
        // 必填:待发送手机号
        request.putQueryParameter("PhoneNumbers",Phone_Number);
        // 必填:短信签名-可在短信控制台中找到
        request.putQueryParameter("SignName",Sign_Name);
        return request;
    }

    /**
     * 初始化阿里云访问参数，设置过期时间
     * @return
     */
    private static IAcsClient InitClient(){
        // 可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        // 初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-beijing", Access_Key_Id, Access_Key_Secet);
        return new DefaultAcsClient(profile);
    }
}
