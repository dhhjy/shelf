package com.quick.shelf.modular.creditPort.liMu;

import com.quick.shelf.modular.business.entity.BSysUser;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * @ClassName 立木征信接口常量方法
 * @Description TODO
 * @Author ken
 * @Date 2019/7/18 12:36
 * @Version 1.0
 */
public class LiMuConstantMethod {

    //API-key
    public final static String APIKEY = "1807072515702764";

    //API Secret
    public final static String APISECRET = "Lmj70EsvZECxjDThLaXv11QjCvZ7a1JF";

    /**
     * 立木回调日志
     */
    public static final Object LIMU_CALLBACK_PARAM_KEY = "LIMU_CALLBACK_PARAM_KEY:";
    public static final Object LIMU_CALLBACK_PARAM_RESULT = "LIMU_CALLBACK_PARAM_RESULT:";

    /**
     * 正式地址 - 原始数据
     */
    public static String URL = "https://api.limuzhengxin.com/api/gateway";

    /**
     * 正式地址 - 报告页面
     */
    public static String REPORT_URL = "http://page.limuzhengxin.com/page/web/task";

    /**
     * 通用立木征信版本号
     */
    public static String VERSION = "1.2.0";

    /**
     * 原始数据接口
     */
    public static String apiMethod = "api.common.getReport";

    /**
     * 立木 H5
     */
    public final static String H5_URL = "http://m.limuzhengxin.com/h5/index.html#/";

    /**
     * 成功状态码
     */
    public static final String SUCCESS_CODE = "0000";

    /**
     * 获取立木验证网页
     *
     * @param bSysUser    需要进行认证的用户对象
     * @param apiName     需要认证的接口（接口定义在 LiMuConstantEnum 枚举类）
     * @param signUrl     签名URL地址
     * @param callBackUrl 回调通知地址
     * @param successUrl
     * @return
     */
    public static String getLimuVerifyUrl(BSysUser bSysUser, String apiName, String signUrl, String callBackUrl) {
        return LiMuConstantMethod.H5_URL + apiName + "?apiKey=" + LiMuConstantMethod.APIKEY + "&uid=" + bSysUser.getUserId() + "&mobilePhone=" + bSysUser.getPhoneNumber()
                + "&signUrl=" + signUrl + "&callBackUrl=" + callBackUrl + "&isWxHeadHide=1";
    }

    /**
     * 立木认证原始报告时所需要的参数封装
     */
    public static Map<String,Object> getJsonParams(String token, String type) {
        Map<String,Object> params = new HashMap<>();
        // 设置请求参数
        // (必填) 原始数据接口：api.common.getResult 报告接口：api.common.getReport
        params.put("method", apiMethod);
        // (必填) 必须通过 apiKey 才能访问 api
        params.put("apiKey", LiMuConstantMethod.APIKEY);
        // (必填) 调用的接口版本
        params.put("version", LiMuConstantMethod.VERSION);
        // (必填) 查询唯一标识
        params.put("token", token);
        //（必填）业务类型
        params.put("bizType", type);
        //（非必填）当位运营商报告时，可以开启立木分选项
        if (type.equals(LiMuConstantEnum.API_NAME_YYS.getApiName())) {
            params.put("score", "01");//启动立木分
        }
        // (必填)  对所有请求参数加密，得到签名
        params.put("sign", LiMuConstantMethod.getSign(params));
        return params;
    }

    /**
     * 立木认证报告页面时所需要的参数封装
     */
    public static Map<String,Object> getPageParams(String token, String type) {
        Map<String,Object> params = new HashMap<>();
        // 设置请求参数
        // (必填) 必须通过 apiKey 才能访问 api
        params.put("apiKey", LiMuConstantMethod.APIKEY);
        // (必填) 调用的接口版本
        params.put("version", LiMuConstantMethod.VERSION);
        // (必填) 查询唯一标识
        params.put("token", token);
        //（必填）业务类型
        params.put("bizType", type);
        return params;
    }

    /**
     * 签名转化
     *
     * @param reqParam
     * @return
     */
    public static String getSign(Map<String,Object> params) {

        StringBuffer sbb = new StringBuffer();
        int index = 1;
        Set set = params.keySet();
        for(Iterator iter = set.iterator(); iter.hasNext();)
        {
            String key = (String)iter.next();
            String value = (String)params.get(key);
            sbb.append(key).append("=").append(value);
            if (set.size() != index) {
                sbb.append("&");
            }
            index++;
        }

//        for (BasicNameValuePair nameValuePair : reqParam) {
//            sbb.append(nameValuePair.getName() + "=" + nameValuePair.getValue());
//            if (reqParam.size() != index) {
//                sbb.append("&");
//            }
//            index++;
//        }
        String sign = sbb.toString();

        Map<String, String> paramMap = new HashMap<String, String>();
        String ret;
        if (!StringUtils.isEmpty(sign)) {
            String[] arr = sign.split("&");
            for (int i = 0; i < arr.length; i++) {
                String tmp = arr[i];
                if (-1 != tmp.indexOf("=")) {
                    paramMap.put(tmp.substring(0, tmp.indexOf("=")), tmp.substring(tmp.indexOf("=") + 1, tmp.length()));
                }

            }
        }
        List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>(paramMap.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, String>>() {
            public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                int ret = 0;
                ret = o1.getKey().compareTo(o2.getKey());
                if (ret > 0) {
                    ret = 1;
                } else {
                    ret = -1;
                }
                return ret;
            }
        });

        StringBuilder sbTmp = new StringBuilder();
        for (Map.Entry<String, String> mapping : list) {
            if (!"sign".equals(mapping.getKey())) {
                sbTmp.append(mapping.getKey()).append("=").append(mapping.getValue()).append("&");
            }
        }
        sbTmp.setLength(sbTmp.lastIndexOf("&"));
        sbTmp.append(LiMuConstantMethod.APISECRET);
        ret = SHA1(sbTmp.toString());

        return ret;
    }

    /**
     * SHA1加密
     *
     * @param decript
     * @return
     */
    private static String SHA1(String decript) {
        String ret = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(decript.getBytes(StandardCharsets.UTF_8));
            byte[] messageDigest = digest.digest();
            StringBuilder stringBuilder = new StringBuilder();
            if (messageDigest == null || messageDigest.length <= 0) {
                return null;
            }
            for (byte b : messageDigest) {
                int v = b & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ret;
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

        if (apiName.equals(LiMuConstantEnum.API_NAME_TB.getApiName()))
            return LiMuConstantEnum.API_NAME_TB.getServerName();

        if (apiName.equals(LiMuConstantEnum.API_NAME_YYS.getApiName()))
            return LiMuConstantEnum.API_NAME_YYS.getServerName();

        if (apiName.equals(LiMuConstantEnum.API_NAME_LFSJ.getApiName()))
            return LiMuConstantEnum.API_NAME_LFSJ.getServerName();

        if (apiName.equals(LiMuConstantEnum.API_NAME_SBZW.getApiName()))
            return LiMuConstantEnum.API_NAME_SBZW.getServerName();

        if (apiName.equals(LiMuConstantEnum.API_NAME_JS.getApiName()))
            return LiMuConstantEnum.API_NAME_JS.getServerName();

        return null;
    }
}
