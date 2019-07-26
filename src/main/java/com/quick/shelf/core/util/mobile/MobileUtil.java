package com.quick.shelf.core.util.mobile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.core.util.bqs.AppConstants;
import com.quick.shelf.modular.business.entity.BMobileData;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantEnum;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantEnum;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机工具类
 * 主要解决：从各类征信报告原始数据中，抽取用户的通讯记录，
 * 并且将各个通讯记录中的电话进行筛选
 * 筛选条件为：移动，联通，电信（其他类型电话不使用，不保存）
 */
public class MobileUtil {
    private static final Logger logger = LoggerFactory.getLogger(MobileUtil.class);
    private static final String isChinaMobile = "^134[0-8]\\d{7}$|^(?:13[5-9]|14[78]|15[0-27-9]|172|178|1703|1705|1706|18[2-478]|198)\\d{7,8}$"; //移动
    private static final String isChinaUnion = "^(?:13[0-2]|14[56]|15[56]|171|17[56]|166|1704|1707|1708|1709|18[56])\\d{7,8}$"; //联通
    private static final String isChinaTelcom = "^(?:133|149|153|1700|1701|1702|177|17[34]|18[019]|199)\\d{7,8}$"; // 电信

    /**
     * 分类型获取手机号码
     *
     * @param data JSONObject 类型的原始数据信息
     * @return Map<String, Object> 区分运营商后的 结果集
     * @pararm type 那种类型的运营商报告数据 （立木，白骑士，新颜）
     */
    public static List<BMobileData> getPhoneNumberByType(String type, Integer userId, String data) {
        // 立木通讯录
        if (null != type && type.equals(LiMuConstantEnum.API_NAME_YYS.getApiName())) {
            JSONObject resultJson = JSON.parseObject(data);
            JSONArray jsonArray = getLiMuCommunicationList(resultJson);
            return getLiMuContactAnalysis(userId, jsonArray);
        }
        // 白骑士通讯录
        if (null != type && type.equals(AppConstants.bqsMnoReportPage.getType())) {
            List<Node> trs = getBqsCommunicationList(data);
            assert trs != null;
            return getBqsContactAnalysis(userId, trs);
        }
        // 新颜通讯录
        if (null != type && type.equals(XinYanConstantEnum.API_NAME_YYS.getApiName())) {
            return getXinYanCommunicationList(userId, data);
        }
        return null;
    }

    /**
     * 获取立木通讯录列表
     *
     * @return JSONObject
     */
    private static JSONArray getLiMuCommunicationList(JSONObject jsonObject) {
        JSONObject jsonData = jsonObject.getJSONObject("data");
        return jsonData.getJSONArray("contactAnalysis");
    }

    /**
     * 新颜通讯录HTML数据
     */
    public static List<BMobileData> getXinYanCommunicationList(Integer userId, String data) {
        List<BMobileData> result = new ArrayList<>();
        JSONObject jsonObject = JSONObject.parseObject(data);
        JSONArray arr = jsonObject.getJSONObject("detail").getJSONObject("friend_circle").getJSONArray("call_contact_detail");
        logger.info("电话总数：{}", arr.size());

        for (Object ar : arr) {
            JSONObject jsonData = (JSONObject) JSONObject.toJSON(ar);
            String phone = jsonData.getString("peer_num");
            //1. 判断是否为手机号，为手机号才继续执行
            if (isMobile(phone)) {
                BMobileData BMobileData = new BMobileData();
                BMobileData.setUserId(userId);
                //2. 区分运营商
                BMobileData.setTMobile(getT_Mobile(phone));
                BMobileData.setStatus(0);
                BMobileData.setCallNum(phone);
                BMobileData.setIsHitRiskList("0");
                // 归属地
                String attribution = jsonData.getString("city");
                if (null == attribution || attribution.equals(""))
                    BMobileData.setAttribution("未知");
                else
                    BMobileData.setAttribution(attribution);
                BMobileData.setCallCnt(Integer.parseInt(jsonData.getString("call_num_6m")));
                BMobileData.setCallTime(jsonData.getString("call_time_6m"));
                BMobileData.setCallingCnt(Integer.parseInt(jsonData.getString("dial_num_6m")));
                BMobileData.setCallingTime(jsonData.getString("dial_time_6m"));
                BMobileData.setCalledCnt(Integer.parseInt(jsonData.getString("dialed_num_6m")));
                BMobileData.setCalledTime(jsonData.getString("dialed_time_6m"));
                BMobileData.setLastStart(DateUtil.getStringToDate(jsonData.getString("last_call_time")));
                BMobileData.setLastTime("/");
                BMobileData.setCreateTime(new Date());
                result.add(BMobileData);
            }
        }

        logger.info("过滤后的电话总数：{},过滤电话：{}个", result.size(), arr.size() - result.size());
        return result;
    }

    /**
     * 获取白骑士页面HTML数据，
     * 本方法将HTML通过 jsoup 工具类转为DOC对象操作
     */
    public static List<Node> getBqsCommunicationList(String data) {
        // 压缩HTML代码，去掉空格
        try {
            data = HtmlCompressor.compress(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Document doc = Jsoup.parse(data);
        // 取得body
        Element body = doc.body();
        // 取得符合条件的div 目测有3个
        Elements divs = body.getElementsByClass("items detail");

        Element div;

        if (divs.size() > 1)
            div = divs.get(divs.size() - 1);
        else
            return null;

        // 取得所有表格，方法返回多个表格
        Elements tables = div.getElementsByTag("table");

        Element table;

        if (tables.size() > 0)
            table = tables.get(0);
        else
            return null;

        // 表格内容
        Node tbody = table.childNode(2);
        // 获得每一行的数据
        return tbody.childNodes();
    }

    /**
     * 获取立木组装后的数据
     *
     * @param userId 用户主键
     * @param data   JSONArray 格式数据
     * @return
     */
    private static List<BMobileData> getLiMuContactAnalysis(Integer userId, JSONArray data) {
        logger.info("电话总数：{}", data.size());
        List<BMobileData> result = new ArrayList<>();
        for (Object d : data) {
            JSONObject jsonData = (JSONObject) JSONObject.toJSON(d);
            String phone = jsonData.getString("callNum");
            //1. 判断是否为手机号，为手机号才继续执行
            if (isMobile(phone)) {
                BMobileData BMobileData = new BMobileData();
                BMobileData.setUserId(userId);
                //2. 区分运营商
                BMobileData.setTMobile(getT_Mobile(phone));
                BMobileData.setStatus(0);
                BMobileData.setCallNum(phone);
                BMobileData.setIsHitRiskList(jsonData.getString("isHitRiskList"));
                // 归属地
                String attribution = jsonData.getString("attribution");
                if (null == attribution || attribution.equals(""))
                    BMobileData.setAttribution("未知");
                else
                    BMobileData.setAttribution(attribution);
                BMobileData.setCallCnt(Integer.parseInt(jsonData.getString("callCnt")));
                BMobileData.setCallTime(jsonData.getString("callTime"));
                BMobileData.setCallingCnt(Integer.parseInt(jsonData.getString("callingCnt")));
                BMobileData.setCallingTime(jsonData.getString("callingTime"));
                BMobileData.setCalledCnt(Integer.parseInt(jsonData.getString("calledCnt")));
                BMobileData.setCalledTime(jsonData.getString("calledTime"));
                BMobileData.setLastStart(DateUtil.getStringToDate(jsonData.getString("lastStart")));
                BMobileData.setLastTime(jsonData.getString("lastTime"));
                BMobileData.setCreateTime(new Date());
                result.add(BMobileData);
            }
        }
        logger.info("过滤后的电话总数：{},过滤电话：{}个", result.size(), data.size() - result.size());
        return result;
    }

    /**
     * 获取白骑士通讯录组装后的对象
     *
     * @param userId 主键ID
     * @param
     * @return List<BMobileData>
     */
    public static List<BMobileData> getBqsContactAnalysis(Integer userId, List<Node> trs) {
        List<BMobileData> result = new ArrayList<>();
        logger.info("电话总数：{}", trs.size());
        // 遍历行数据，组装JSON
        for (Node tr : trs) {
            List<Node> tds = tr.childNodes();
            // 拿到号码
            String phone = tds.get(0).childNode(0).childNode(0).toString();
            assert phone != null;

            //1. 判断是否为手机号，为手机号才继续执行
            if (isMobile(phone)) {
                BMobileData BMobileData = new BMobileData();
                BMobileData.setUserId(userId);
                //2. 区分运营商
                BMobileData.setTMobile(getT_Mobile(phone));
                BMobileData.setStatus(0);
                BMobileData.setCallNum(phone);
                // 白骑士中没有这个选项只能全部默认为0了，是否命中风险名单（1：命中0：未命中）
                BMobileData.setIsHitRiskList("0");
                // 归属地
                BMobileData.setAttribution(tds.get(2).childNode(0).toString());
                // 通话次数和通话时间
                String callCntAndCallTime = tds.get(5).childNode(0).toString().replace(" ", "");
                BMobileData.setCallCnt(Integer.valueOf(callCntAndCallTime.substring(0, callCntAndCallTime.indexOf("/"))));
                BMobileData.setCallTime(callCntAndCallTime.substring(callCntAndCallTime.indexOf("/") + 1));
                // 主叫次数和主叫时间
                String callingCntAndCallingTime = tds.get(6).childNode(0).toString().replace(" ", "");
                ;
                BMobileData.setCallingCnt(Integer.valueOf(callingCntAndCallingTime.substring(0, callingCntAndCallingTime.indexOf("/"))));
                BMobileData.setCallingTime(callingCntAndCallingTime.substring(callingCntAndCallingTime.indexOf("/") + 1));
                // 被叫次数和被叫时间
                String calledCntAndCaalledTime = tds.get(7).childNode(0).toString().replace(" ", "");
                ;
                BMobileData.setCalledCnt(Integer.valueOf(calledCntAndCaalledTime.substring(0, calledCntAndCaalledTime.indexOf("/"))));
                BMobileData.setCalledTime(calledCntAndCaalledTime.substring(calledCntAndCaalledTime.indexOf("/") + 1));
                BMobileData.setLastStart(new Date(Long.valueOf(tds.get(4).childNode(0).toString())));
                BMobileData.setCreateTime(new Date());
                result.add(BMobileData);
            }
        }
        logger.info("过滤后的电话总数：{},过滤电话：{}个", result.size(), trs.size() - result.size());
        return result;
    }

    /**
     * 判断是否是手机号
     *
     * @param str 手机号
     * @return
     */
    public static boolean isMobile(String str) {
        if (null == str || str.equals(""))
            return false;

        Pattern p;
        Matcher m;
        boolean b = false;
        String s2 = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";// 验证手机号
        if (StringUtils.isNotBlank(str)) {
            p = Pattern.compile(s2);
            m = p.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 获取运营商类型根据手机号
     *
     * @param phone 手机号
     * @return 0-移动；1-联通；2-电信；3-未知
     */
    private static Integer getT_Mobile(String phone) {
        Matcher cmcc = Pattern.compile(isChinaMobile).matcher(phone);   //移动
        Matcher cucc = Pattern.compile(isChinaUnion).matcher(phone);    //联通
        Matcher ctcc = Pattern.compile(isChinaTelcom).matcher(phone);   //电信
        if (cmcc.matches()) {
            return 0;
        } else if (cucc.matches()) {
            return 1;
        } else if (ctcc.matches()) {
            return 2;
        } else {
            return 3;
        }
    }
}
