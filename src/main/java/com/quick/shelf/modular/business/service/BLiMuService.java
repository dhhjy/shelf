package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.annotion.PortLog;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.util.HttpClientUtils;
import com.quick.shelf.core.util.mobile.MobileUtil;
import com.quick.shelf.core.util.xinYanUtils.util.HttpUtils;
import com.quick.shelf.modular.business.entity.BLiMuData;
import com.quick.shelf.modular.business.entity.BMobileData;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.mapper.BLiMuMapper;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantEnum;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantMethod;
import com.quick.shelf.modular.creditPort.liMu.LiMuResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName 立木征信业务层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 20:19
 * @Version 1.0
 */
@Service
public class BLiMuService extends ServiceImpl<BLiMuMapper, BLiMuData> {
    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(BLiMuService.class);

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    @Resource
    private BMobileDataService bMobileDataService;

    /**
     * 获取立木报告列表
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return Page<Map < String, Object>>
     */
    public Page<Map<String, Object>> selectBLiMuDatas(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBLiMuDatas(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 根据用户的主键，以及征信报告类型去获取对应的立木征信数据
     * <p>立木征信报告为一对多的模式，所以会返回最新一份的征信报告</p>
     * 立木的报告分两种，有原始数据 0，有页面数据 1
     *
     * @param userId   立木征信中关联的用户主键
     * @param type     立木征信四类报告的类型
     * @param dataType 原始数据：0 页面报告：1
     * @return BXinYanData
     */
    public BLiMuData selectBLiMuDataByUserId(Integer userId, String type, String dataType) {
        BLiMuData bXinYanData = new BLiMuData();
        bXinYanData.setUserId(userId);
        bXinYanData.setType(type);
        bXinYanData.setDataType(dataType);
        return this.baseMapper.selectBLiMuDataByUserId(bXinYanData);
    }

    /**
     * 保存立木征信淘宝的原始数据
     *
     * @param liMuResult 立木回调结果对象
     */
    @PortLog(type = "taobaoReport", typeName = "淘宝")
    public String liMuTBJsonData(LiMuResult liMuResult) {
        String result = this.insertJsonData(liMuResult);
        JSONObject jsonResult = JSONObject.parseObject(result);
        jsonResult.put("userId", liMuResult.getUid());
        return jsonResult.toString();
    }

    /**
     * 保存立木征信淘宝的页面数据
     *
     * @param liMuResult 立木回调结果对象
     */
    @PortLog(type = "taobaoReportPage", typeName = "淘宝")
    public String liMuTBPageData(LiMuResult liMuResult) {
        this.insertPageData(liMuResult);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("userId", liMuResult.getUid());
        return jsonResult.toString();
    }

    /**
     * 保存立木征信运营商的原始数据
     *
     * @param liMuResult 立木回调结果对象
     */
    @PortLog(type = "mobileReport", typeName = "运营商")
    public String liMuYYSJsonData(LiMuResult liMuResult) {
        String result = this.insertJsonData(liMuResult);
        JSONObject jsonResult = JSONObject.parseObject(result);
        jsonResult.put("userId", liMuResult.getUid());

        // 在其他线程中进行通讯录的操作
        new Thread(() -> {
            List<BMobileData> phoneResultList = MobileUtil.getPhoneNumberByType
                    (liMuResult.getBizType(), Integer.valueOf(liMuResult.getUid()), result);

            assert phoneResultList != null;
            if (phoneResultList.size() > 0)
                //批量插入
                bMobileDataService.batchInsert(phoneResultList);
        }).start();

        return jsonResult.toString();
    }

    /**
     * 保存立木征信运营商的页面数据
     *
     * @param liMuResult 立木回调结果对象
     */
    @PortLog(type = "mobileReportPage", typeName = "运营商")
    public String liMuYYSPageData(LiMuResult liMuResult) {
        this.insertPageData(liMuResult);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("userId", liMuResult.getUid());
        return jsonResult.toString();
    }

    /**
     * 在取得运营商报告后，获取立方升级报告
     *
     * @param bSysUser 对象
     * @return String 该方法，在保存完立方升级报告后会返回本次操作的token
     */
    @PortLog(type = "lifangupgradecheck", typeName = "立方升级")
    public String liMuLfsjJsonData(BSysUser bSysUser) {
        // 创建接口参数集
        Map<String, Object> params = new HashMap<>();
        // 封装参数
        // (必填) 立方升级报告原始数据接口：api.identity.lifangupgradecheck
        params.put("method", "api.identity.lifangupgradecheck");
        // (必填) 必须通过 apiKey 才能访问 api
        params.put("apiKey", LiMuConstantMethod.APIKEY);
        // (必填) 调用的接口版本，立方升级报告版本 1.1.1 可以回传Token
        params.put("version", "1.1.1");
        /// (必填) 姓名
        params.put("name", bSysUser.getName());
        /// (必填) 身份证
        params.put("identityNo", bSysUser.getIdCard());
        /// (必填) 手机号
        params.put("mobile", bSysUser.getPhoneNumber());
        // (必填)  对所有请求参数加密，得到签名
        params.put("sign", LiMuConstantMethod.getSign(params));

        // 发起POST接口请求，并得到结果JSON
        String result = HttpUtils.doPost(LiMuConstantMethod.URL, params);
        // 将结果转换为JSONObejct格式，方便取参数
        JSONObject resultJson = JSON.parseObject(result);
        resultJson.put("userId", bSysUser.getUserId());
        logger.info(result);
        if (result != null && LiMuConstantMethod.SUCCESS_CODE.equals(resultJson.getString("code"))) {
            assemble(bSysUser.getUserId(), BusinessConst.ORIGINAL_DATA.toString(), LiMuConstantEnum.API_NAME_LFSJ.getApiName(),
                    result, resultJson.getString("token"));
            return resultJson.toString();
        } else {
            return resultJson.toString();
        }
    }

    /**
     * 获取立方升级的页面报告
     *
     * @param liMuResult
     */
    @PortLog(type = "lifangupgradecheckPage", typeName = "立方升级")
    public String liMuLfsjPageData(LiMuResult liMuResult) {
        insertPageData(liMuResult);
        JSONObject jsonResult = new JSONObject();
        jsonResult.put("userId", liMuResult.getUid());
        return jsonResult.toString();
    }

    /**
     * 获取立木的设备指纹原始数据
     *
     * @param liMuResult
     */
    @PortLog(type = "fingerprint", typeName = "设备指纹")
    public String liMuSbzwJsonData(LiMuResult liMuResult) {
        String result = insertJsonData(liMuResult);
        JSONObject jsonResult = JSONObject.parseObject(result);
        jsonResult.put("userId", liMuResult.getUid());
        return jsonResult.toString();
    }

    /**
     * 保存立木的机审报告
     *
     * @param liMuResult
     */
    @PortLog(type = "machinecheck", typeName = "机审报告")
    public String liMuJsJsonData(LiMuResult liMuResult) {
        BSysUserStatus bSysStatus = this.bSysUserStatusService.selectBSysUserStatusByUserId(Integer.valueOf(liMuResult.getUid()));

        Map<String, Object> params = new HashMap<>();
        params.put("method", "api.identity.machinecheck");//接口名称
        params.put("apiKey", LiMuConstantMethod.APIKEY);//API授权
        params.put("version", "1.0.0");//调用的接口版本

        if (BusinessConst.OK.equals(bSysStatus.getLimuMobileReportStatus())) {
            BLiMuData liMuData = selectBLiMuDataByUserId(Integer.valueOf(liMuResult.getUid()), LiMuConstantEnum.API_NAME_YYS.getApiName(), BusinessConst.ORIGINAL_DATA.toString());
            String mobileToken = liMuData.getToken();
            params.put("mrToken", mobileToken);//已成功的运营商信用报告业务标识 token
            logger.info("用户:{} 获取" + LiMuConstantEnum.API_NAME_YYS.getServerName() + "报告的Token:{}", liMuResult.getUid(), mobileToken);
        }
        if (BusinessConst.OK.equals(bSysStatus.getLimuTaobaoReportStatus())) {
            BLiMuData liMuData = selectBLiMuDataByUserId(Integer.valueOf(liMuResult.getUid()), LiMuConstantEnum.API_NAME_TB.getApiName(), BusinessConst.ORIGINAL_DATA.toString());
            String taobaoReportToken = liMuData.getToken();
            params.put("tbrToken", taobaoReportToken);//已成功的电商报告业务标识 token
            logger.info("用户:{} 获取" + LiMuConstantEnum.API_NAME_TB.getServerName() + "报告的Token:{}", liMuResult.getUid(), taobaoReportToken);
        }
        if (BusinessConst.OK.equals(bSysStatus.getLimuLifangUpgradeCheckStatus())) {
            BLiMuData liMuData = selectBLiMuDataByUserId(Integer.valueOf(liMuResult.getUid()), LiMuConstantEnum.API_NAME_LFSJ.getApiName(), BusinessConst.ORIGINAL_DATA.toString());
            String lifangUpgradeCheckToken = liMuData.getToken();
            params.put("lfuToken", lifangUpgradeCheckToken);//已成功的立方升级版业务标识 token
            logger.info("用户:{} 获取" + LiMuConstantEnum.API_NAME_LFSJ.getServerName() + "报告的Token:{}", liMuResult.getUid(), lifangUpgradeCheckToken);
        }
        if (BusinessConst.OK.equals(bSysStatus.getDeviceInfoStatus())) {
            BLiMuData liMuData = selectBLiMuDataByUserId(Integer.valueOf(liMuResult.getUid()), LiMuConstantEnum.API_NAME_SBZW.getApiName(), BusinessConst.ORIGINAL_DATA.toString());
            String fingerprintToken = liMuData.getToken();
            params.put("fpToken", fingerprintToken);//已成功的指纹设备业务标识 token
            logger.info("用户:{} 获取" + LiMuConstantEnum.API_NAME_SBZW.getServerName() + "报告的Token:{}", liMuResult.getUid(), fingerprintToken);
        }
        params.put("sign", LiMuConstantMethod.getSign(params));//请求参数签名
        logger.info("参数：" + params);
        String result = HttpUtils.doPost(LiMuConstantMethod.URL, params);
        JSONObject resultJson = JSON.parseObject(result);
        if (result != null && LiMuConstantMethod.SUCCESS_CODE.equals(resultJson.getString("code"))) {
            assemble(Integer.valueOf(liMuResult.getUid()), BusinessConst.ORIGINAL_DATA.toString(), LiMuConstantEnum.API_NAME_JS.getApiName(), result, resultJson.getString("token"));
        }
        resultJson.put("userId", liMuResult.getUid());
        return resultJson.toString();
    }

    /**
     * 保存原始数据通用方法封装
     *
     * @param liMuResult
     */
    private String insertJsonData(LiMuResult liMuResult) {
        Map<String, Object> params = LiMuConstantMethod.getJsonParams(liMuResult.getToken(), liMuResult.getBizType());

        // 发起POST接口请求，并得到结果JSON
        String result = HttpUtils.doPost(LiMuConstantMethod.URL, params);
        // 将结果转换为JSONObejct格式，方便取参数
        JSONObject resultJson = JSON.parseObject(result);
        // 判断接口返回的数据不为空，并且，code 参数 位 0000 时，则进行原始数据的保存操作
        if (result != null && LiMuConstantMethod.SUCCESS_CODE.equals(resultJson.getString("code"))) {
            assemble(Integer.valueOf(liMuResult.getUid()), BusinessConst.ORIGINAL_DATA.toString(), liMuResult.getBizType(),
                    result, liMuResult.getToken());
        }
        return result;
    }

    /**
     * 保存页面数据通用方法封装
     *
     * @param liMuResult
     */
    private void insertPageData(LiMuResult liMuResult) {
        Map<String, Object> params = LiMuConstantMethod.getPageParams(liMuResult.getToken(), liMuResult.getBizType());
        // 拼接get请求字符串
        String url = LiMuConstantMethod.REPORT_URL + "?apiKey=" + LiMuConstantMethod.APIKEY + "&version=" + LiMuConstantMethod.VERSION
                + "&token=" + liMuResult.getToken() + "&bizType=" + liMuResult.getBizType() + "&sign=" + LiMuConstantMethod.getSign(params);
        // 发起GET接口请求，并得到结果JSON
        String result = HttpClientUtils.get(url);
        if (!result.contains("error_div") && !result.contains("签名异常") && !result.contains("参数异常"))
            assemble(Integer.valueOf(liMuResult.getUid()), BusinessConst.PAGE_DATA.toString(), liMuResult.getBizType(),
                    result, liMuResult.getToken());
        else
            logger.info("获取报告页面时，报告页面出错，并未保存报告页面数据");
    }

    /**
     * 保存新颜原始数据（新颜只获取原始数据）
     *
     * @param bXinYanData
     */
    public void insert(BLiMuData bLiMuData) {
        // 创建时间 一对多
        bLiMuData.setCreateTime(new Date());
        this.baseMapper.insert(bLiMuData);
    }

    /**
     * 新增立木数据，立木数据报告是一对多的形式
     * 一个用户可以有多份相同的立木报告
     */
    public void assemble(Integer userId, String dataType, String apiName, String result, String token) {
        String tn = "原始数据";
        if (dataType.equals("1")) {
            tn = "页面报告";
        }
        logger.info("用户：{}，插入：{} 类型的：{}", userId, LiMuConstantMethod.compareApiName(apiName), tn);

        new Thread(() -> {
            BLiMuData bLiMuData = new BLiMuData();
            // 设置用户主键 userId
            bLiMuData.setUserId(userId);
            // 数据类型: 0-原始数据 1-报告页面(html)
            bLiMuData.setDataType(dataType);
            // 报告的类型（报告类型定义在 LiMuConstantEnum 枚举类中）
            bLiMuData.setType(apiName);
            // 设置报告类型的中文名
            bLiMuData.setTypeText(LiMuConstantMethod.compareApiName(apiName));
            // 设置数据
            bLiMuData.setDataValue(result);
            // 设置token
            bLiMuData.setToken(token);
            // 新增
            insert(bLiMuData);
        }).start();
    }

    /**
     * 跟据 用户的主键 跟 接口的类型去改变用户对应类型的认证状态
     *
     * @param userId
     * @param apiName
     */
    public void changeUserStatus(Integer userId, String apiName) {
        // 同时更新用户关联的状态表，更新雷达认证状态字段
        BSysUserStatus bSysUserStatus = new BSysUserStatus();
        bSysUserStatus.setUserId(userId);
        logger.info("用户：{}，改变：{}的状态！名称：{}", userId, LiMuConstantMethod.compareApiName(apiName), apiName);
        if (LiMuConstantEnum.API_NAME_TB.getApiName().equals(apiName))
            bSysUserStatus.setLimuTaobaoReportStatus(BusinessConst.OK);
        if (LiMuConstantEnum.API_NAME_YYS.getApiName().equals(apiName))
            bSysUserStatus.setLimuMobileReportStatus(BusinessConst.OK);
        if (LiMuConstantEnum.API_NAME_LFSJ.getApiName().equals(apiName))
            bSysUserStatus.setLimuLifangUpgradeCheckStatus(BusinessConst.OK);
        if (LiMuConstantEnum.API_NAME_SBZW.getApiName().equals(apiName))
            bSysUserStatus.setDeviceInfoStatus(BusinessConst.OK);
        this.bSysUserStatusService.updateByUserId(bSysUserStatus);
    }
}
