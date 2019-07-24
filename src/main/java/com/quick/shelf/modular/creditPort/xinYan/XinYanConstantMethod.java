package com.quick.shelf.modular.creditPort.xinYan;

import com.quick.shelf.core.util.xinYanUtils.rsa.RsaCodingUtil;
import com.quick.shelf.core.util.xinYanUtils.util.HttpUtils;
import com.quick.shelf.core.util.xinYanUtils.util.MD5Utils;
import com.quick.shelf.core.util.xinYanUtils.util.SecurityUtil;
import com.quick.shelf.modular.business.entity.BSysUser;
import net.sf.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 新颜接口常量配置及常用方法
 */
public class XinYanConstantMethod {
    public static final Logger logger = LoggerFactory.getLogger(XinYanConstantMethod.class);

    // 立木回调数据，redis 缓存键
    public static final String REDIS_KEY = "xinYanCallBack::";
    // ApiUser
    public static final String ApiUser = "8150738569";
    // Access Key: 6613d600d19941a094753830bd6fc0af
    private static final String AccessKey = "R8077DZ2dmrDp90z";
    // H5页面导航标签隐藏
    private static final String Hide_Label = "headVisible=0";
    // 生产环境
    public static final String DEVELOP_URL = "https://qz.xinyan.com";
    // H5超链接地址说明
    // https://qz.xinyan.com/h5/{apiUser}/{apiEnc}/{timeMark}/{apiName}/{taskId}? {jumpUrl}=
    // 请求H5页面
    private static final String H5 = "/auth/";
    // 请求原始数据地址
    public static final String JSON_DATA_PATH = "/api/user/data";
    // 请求报告数据地址
    private static final String REPORT_PATH = "/api/user/data/report/json?";
    // 请求页面数据
    private static final String PAGE_PATH = "/#/carrierReport?";
    // 全景雷达
    private static final String REDAR_URL = "https://api.xinyan.com/product/radar/v3/report";
    // 终端号
    private static final String TERMIANL_CODE = "8150738569";
    // 版本号
    private static final String REDAR_VERSION = "1.3.0";
    // 私匙密码
    private static final String PRIVATE_KEY_PASSWORD = "654321";
    // 私匙文件
    private static final String PFX_PATH = "\\xinwei_pri.pfx";
    // 成功状态
    public static final String SUCCESS = "true";


    /**
     * 获取任务的H5回调页面(默认不显示H5的导航)
     * 通过回调地址 jumpUrl 直接 返回到APP的页面上进行授权后的页面展示
     *
     * @param userId          用作任务的唯一标识
     * @param apiName         XYConstantEnum枚举类中指定的服务标识 例子：XinYanConstantEnum.API_NAME_TB.getApiName()
     * @param jumpUrl         任务完成后的重定向地址,需要跳转到APP上的那个页面中
     * @param dataNotifyUrl   原始数据通知地址
     * @param reportNotifyUrl 报告数据(页面)通知地址
     * @return String
     */
    public static String getXinYanH5Url(String userId, String apiName, String dataNotifyUrl, String reportNotifyUrl) {
        // 时间戳
        String timeMark = XinYanConstantMethod.getTimeMark();
        String url =  XinYanConstantMethod.DEVELOP_URL + XinYanConstantMethod.H5 + XinYanConstantMethod.ApiUser + "/" +
                getApiEnc(timeMark, apiName, userId) + "/" + timeMark + "/" +
                apiName + "/" + userId + "?" + Hide_Label + "&dataNotifyUrl=" + dataNotifyUrl;

        if(null != reportNotifyUrl && !("").equals(reportNotifyUrl))
        {
            url += "&reportNotifyUrl=" + reportNotifyUrl;
        }

        return url;
    }

    /**
     * 获取原始数据征信报告时的URL地址
     *
     * @param token 任务查询凭证
     * @return
     */
    public static String getJsonDataUrl(String token) {
        return XinYanConstantMethod.DEVELOP_URL + XinYanConstantMethod.JSON_DATA_PATH + "?apiUser=" + XinYanConstantMethod.ApiUser
                + "&apiEnc=" + XinYanConstantMethod.getJsonDataApiEnc() + "&token=" + token;
    }

    /**
     * 获取报告(页面)征信报告时的URL地址
     *
     * @param token 任务查询凭证
     * @return
     */
    public static String getReportaUrl(String token) {
        return XinYanConstantMethod.DEVELOP_URL + XinYanConstantMethod.REPORT_PATH + "apiUser=" + XinYanConstantMethod.ApiUser
                + "&apiEnc=" + XinYanConstantMethod.getJsonDataApiEnc() + "&token=" + token;
    }

    /**
     * 获取报告(页面)征信报告时的URL地址
     *
     * @param token 任务查询凭证
     * @return
     */
    public static String getPageUrl(String token) {
        return XinYanConstantMethod.DEVELOP_URL + XinYanConstantMethod.PAGE_PATH + "apiUser=" + XinYanConstantMethod.ApiUser
                + "&apiEnc=" + XinYanConstantMethod.getJsonDataApiEnc() + "&token=" + token;
    }

    /**
     * 获取当前时间戳
     *
     * @return long
     */
    private static String getTimeMark() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        return formatter.format(currentTime);
    }

    /**
     * 被加密参数有 ApiUser + timeMark + apiName + taskId + AccessKey
     * 获取参数被MD5加密后的字符串
     *
     * @param timeMark 时间戳
     * @param apiName  接口标识
     * @param taskId   任务标识，唯一不可重复的。建议使用userId
     * @return String MD5参数加密后的字符串
     */
    private static String getApiEnc(String timeMark, String apiName, String taskId) {
        String plaintext = XinYanConstantMethod.ApiUser + timeMark + apiName + taskId + XinYanConstantMethod.AccessKey;
        return DigestUtils.md5Hex(plaintext);
    }

    /**
     * 被加密参数 apiUser + apiKey
     * 获取请求原始数据时 apiEnc 的MD5加密后的字符串
     *
     * @return String MD5参数加密后的字符串
     */
    public static String getJsonDataApiEnc() {
        return DigestUtils.md5Hex(XinYanConstantMethod.ApiUser + XinYanConstantMethod.AccessKey);
    }

    /**
     * 比较apiName,返回相同apiName的 服务中文名称
     *
     * @param apiName 服务标识
     * @return
     */
    public static String compareApiName(String apiName) {
        if (null == apiName)
            return "无ApiName";

        if (apiName.equals(XinYanConstantEnum.API_NAME_TB.getApiName()))
            return XinYanConstantEnum.API_NAME_TB.getServerName();

        if (apiName.equals(XinYanConstantEnum.API_NAME_YYS.getApiName()))
            return XinYanConstantEnum.API_NAME_YYS.getServerName();

        if (apiName.equals(XinYanConstantEnum.API_NAME_ZHIMAFEN.getApiName()))
            return XinYanConstantEnum.API_NAME_ZHIMAFEN.getServerName();

        if (apiName.equals(XinYanConstantEnum.API_NAME_LD.getApiName()))
            return XinYanConstantEnum.API_NAME_LD.getServerName();

        return null;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        File pfxfile = new File(XinYanConstantMethod.PFX_PATH);
//        if (!pfxfile.exists()) {
//            log("私钥文件不存在！");
//            throw new RuntimeException("私钥文件不存在！");
//        }
//        String userId = SendSmsConstant.getAuthCode();
//        System.out.println(userId);
//        String base64Str = assembleEncryptParams(userId,"1311811993051518", "张常宁", "18699996820", null);
//        System.out.println(getRaDerResult(base64Str));
        String jumpUrl = "";
        // 原始数据回调
        String callbackJson = "2539z803m9.qicp.vip:12886" + "/app/xinYan/callbackJson";
        System.out.println(getXinYanH5Url("101",XinYanConstantEnum.API_NAME_YYS.getApiName(),callbackJson,null));
    }

    /**
     * 加密全景雷达所需要的参数
     *
     * @param base64str  加密参数的base64位编码字符串
     * @return
     */
    public static String getRaDerResult(String base64str, BSysUser bSysUser) {
        Map<String, String> headers = new HashMap<>();

        // 私匙文件
        Resource resource = new ClassPathResource(XinYanConstantMethod.PFX_PATH);
        File pfxfile = null;
        try {
            pfxfile = resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert pfxfile != null;
        if (!pfxfile.exists()) {
            logger.info("私钥文件不存在！");
            throw new RuntimeException("私钥文件不存在！");
        }

        String data_content = RsaCodingUtil.encryptByPriPfxFile(base64str, XinYanConstantMethod.PFX_PATH, XinYanConstantMethod.PRIVATE_KEY_PASSWORD);// 加密数据
        logger.info("加密串:{}", data_content);
        Map<String, Object> params = new HashMap<>();
        params.put("member_id", XinYanConstantMethod.ApiUser);
        params.put("terminal_id", XinYanConstantMethod.TERMIANL_CODE);
        params.put("data_type", "json");
        params.put("data_content", data_content);
        String PostString = HttpUtils.doPostByForm(XinYanConstantMethod.REDAR_URL, headers, params);

        // 将查询结果转成json对象，把用户主键存入到json对象 在返回
        com.alibaba.fastjson.JSONObject jsonResult = com.alibaba.fastjson.JSONObject.parseObject(PostString);
        jsonResult.put("userId", bSysUser.getUserId());
        logger.info("请求返回:{}",  PostString);

        if (PostString.isEmpty()) {// 判断参数是否为空
            logger.info("返回数据为空");
            throw new RuntimeException("返回数据为空");
        }else
        {
            return jsonResult.toString();
        }
    }

    /**
     * 组装全景雷达加密参数
     *
     * @param userId        用户主键 不可为空
     * @param id_no         身份证 不可为空
     * @param id_name       姓名 不可为空
     * @param phone_no      手机号
     * @param bankcard_no   银行卡号
     * @return Base64Encode base64位编码后的字符串
     */
    public static String assembleEncryptParams(String userId,String id_no, String id_name, String phone_no, String bankcard_no){
        Map<Object, Object> ArrayData = new HashMap<>();
        // 商户号
        ArrayData.put("member_id", XinYanConstantMethod.ApiUser);
        // 终端号
        ArrayData.put("terminal_id", XinYanConstantMethod.TERMIANL_CODE);
        // 时间戳 格式：yyyyMMddHHmmss
        ArrayData.put("trade_date", getTimeMark());
        // 商户请求订单号 唯一
        ArrayData.put("trans_id", userId + new Random().nextInt(10000));
        // MD5加密后的身份证号
        ArrayData.put("id_no", MD5Utils.encryptMD5(id_no.trim()));
        // MD5加密后的姓名
        ArrayData.put("id_name", MD5Utils.encryptMD5(id_name.trim()));
        if (null != phone_no && !"".equals(phone_no))
            ArrayData.put("phone_no", MD5Utils.encryptMD5(phone_no.trim()));
        if (null != bankcard_no && !"".equals(bankcard_no))
            ArrayData.put("bankcard_no", MD5Utils.encryptMD5(bankcard_no.trim()));
        ArrayData.put("versions", XinYanConstantMethod.REDAR_VERSION);
        ArrayData.put("encrypt_type", "MD5");// MD5：标准32位小写(推荐) SHA256：标准64位

        // 1.先对身份信息 进行MD5加密
        // 2.将业务参数 map ArrayData 进行转JSON处理
        // 3.将JSON字符串进行转 BASE64 编码处理
        // 4.去掉转编码后的空格、换行符
        // 返回！！！
        String base64str = null;
        try {
            base64str = SecurityUtil.Base64Encode(JSONObject.fromObject(ArrayData).toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        assert base64str != null;
        base64str = base64str.replaceAll("\r\n", "");//重要 避免出现换行空格符
        return base64str;
    }
}
