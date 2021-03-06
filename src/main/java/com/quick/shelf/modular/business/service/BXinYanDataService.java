package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.annotion.PortLog;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.util.HttpClientUtil;
import com.quick.shelf.core.util.mobile.MobileUtil;
import com.quick.shelf.modular.business.entity.BMobileData;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.entity.BXinYanData;
import com.quick.shelf.modular.business.mapper.BXinYanDataMapper;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantEnum;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantMethod;
import com.quick.shelf.modular.creditPort.xinYan.XinYanDataResult;
import com.quick.shelf.modular.creditPort.xinYan.XinYanResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 用户关联的新颜征信业务层
 * 'b_xinyan_data'
 */
@Service
public class BXinYanDataService extends ServiceImpl<BXinYanDataMapper, BXinYanData> {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BXinYanDataService.class);

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    @Resource
    private BMobileDataService bMobileDataService;

    /**
     * 根据用户的主键，以及征信报告类型去获取对应的新颜征信数据
     * <p>新颜征信报告为一对多的模式，所以会返回最新一份的征信报告</p>
     * 新颜的报告只有原始数据，所以将默认为 0
     *
     * @param userId   用户主键
     * @param type     报告类型
     * @param dataType 0-原始数据，1-报告数据
     * @return BXinYanData
     */
    public BXinYanData selectBXinYanDataByUserId(Integer userId, String type, Integer dataType) {
        BXinYanData bXinYanData = new BXinYanData();
        bXinYanData.setUserId(userId);
        bXinYanData.setType(type);
        bXinYanData.setDataType(dataType);
        return this.baseMapper.selectBXinYanDataByUserId(bXinYanData);
    }

    /**
     * 保存新颜原始数据（新颜只获取原始数据）
     *
     * @param bXinYanData
     */
    public void insert(BXinYanData bXinYanData) {
        // 创建时间 一对多
        bXinYanData.setCreateTime(new Date());
        this.baseMapper.insert(bXinYanData);
    }

    /**
     * 获取新颜报告列表
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return Page<Map < String, Object>>
     */
    public Page<Map<String, Object>> selectBXinYanDatas(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBXinYanDatas(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 通过接口获取新颜雷达报告
     *
     * @param userId
     * @param bSysUser
     * @return
     */
    @PortLog(type = "radar", typeName = "全景雷达")
    public String getReDerData(Integer userId, BSysUser bSysUser) {
        String base64Str = XinYanConstantMethod.assembleEncryptParams(String.valueOf(userId), bSysUser.getIdCard(),
                bSysUser.getName(), bSysUser.getPhoneNumber(), null);
        return XinYanConstantMethod.getRaDerResult(base64Str, bSysUser);
    }

    /**
     * 保存新颜淘宝的原始数据
     * 此方法主要用于AOP日志记录接口调用的类型
     * 不可简化，否则AOP统计失效
     *
     * @param xyResult 新颜回调结果对象
     */
    @PortLog(type = "taobaopay", typeName = "淘宝支付宝聚合")
    public String xinYanJHJsonData(XinYanResult xyResult) {
        return this.getXinYanJsonData(xyResult);
    }

    /**
     * 保存新颜淘宝的报告数据
     * 原始数据和报告数据都是收一份的钱
     *
     * @param xyResult 新颜回调结果对象
     */
    public void xinYanTBReportData(XinYanResult xyResult) {
        this.getXinYanReportData(xyResult);
    }

    /**
     * 保存新颜运营商原始数据
     * 此方法主要用于AOP日志记录接口调用的类型
     * 不可简化，否则AOP统计失效
     *
     * @param xyResult 新颜回调结果对象
     */
    @PortLog(type = "carrier", typeName = "运营商")
    public String xinYanYYSJsonData(XinYanResult xyResult) {
        return this.getXinYanJsonData(xyResult);
    }

    /**
     * 保存新颜运营商报告数据
     * 原始数据和报告数据都是收一份的钱
     *
     * @param xyResult 新颜回调结果对象
     */
    public void xinYanYYSReportData(XinYanResult xyResult) {
        String result = this.getXinYanReportData(xyResult);
        // 当为运营商报告时，分析用户6个月的用户信息进行通讯录保存
        new Thread(() -> {
            List<BMobileData> list = MobileUtil.getXinYanCommunicationList(Integer.valueOf(xyResult.getTaskId()), result);
            assert list != null;
            if (list.size() > 0)
                //批量插入
                this.bMobileDataService.batchInsert(list);
        }).start();
    }

    private String getXinYanJsonData(XinYanResult xyResult) {
        // 获取新颜原始数据查询的结果
        String result = HttpClientUtil.doGet(XinYanConstantMethod.DEVELOP_URL + XinYanConstantMethod.JSON_DATA_PATH, XinYanConstantMethod.getParams(xyResult.getToken()));
        XinYanDataResult xinYanDataResult = JSONObject.parseObject(result, XinYanDataResult.class);

        // 添加
        assert xinYanDataResult != null;
        assemble(Integer.valueOf(xyResult.getTaskId()), String.valueOf(xinYanDataResult.getDetail()), xyResult.getApiName(), BusinessConst.ORIGINAL_DATA);

        JSONObject jsonResult = JSONObject.parseObject(result);
        jsonResult.put("userId", xyResult.getTaskId());
        logger.info("用户：{} 认证 {} 的原始数据返回结果为：{}", xyResult.getTaskId(), XinYanConstantMethod.compareApiName(xyResult.getApiName()), jsonResult.toString());
        //一定要返回本次的result，需要做后置日志的接口调用保存
        //会通过返回result进行查找并保存日志
        return jsonResult.toString();
    }

    private String getXinYanReportData(XinYanResult xyResult) {
        // 获取新颜报告数据查询的结果
        String result = HttpClientUtil.doGet(XinYanConstantMethod.DEVELOP_URL + XinYanConstantMethod.REPORT_PATH, XinYanConstantMethod.getParams(xyResult.getToken()));
        XinYanDataResult xinYanDataResult = JSONObject.parseObject(result, XinYanDataResult.class);

        // 添加
        assert xinYanDataResult != null;
        assemble(Integer.valueOf(xyResult.getTaskId()), String.valueOf(xinYanDataResult.getDetail()), xyResult.getApiName(), BusinessConst.PAGE_DATA);

        JSONObject jsonResult = JSONObject.parseObject(result);
        jsonResult.put("userId", xyResult.getTaskId());
        logger.info("用户：{} 认证 {} 的报告数据返回结果为：{}", xyResult.getTaskId(), XinYanConstantMethod.compareApiName(xyResult.getApiName()), jsonResult.toString());
        return jsonResult.toString();
    }

    /**
     * 组装用户数据，进行新增操作
     */
    public void assemble(Integer userId, String result, String apiName, Integer dataType) {
        new Thread(() -> {
            BXinYanData xinYanData = new BXinYanData();
            // userId
            xinYanData.setUserId(userId);
            // 设置服务类型
            xinYanData.setType(apiName);
            // 设置服务类型中文名称
            xinYanData.setTypeText(XinYanConstantMethod.compareApiName(apiName));
            // 添加数据
            xinYanData.setDataValue(result);
            // 保存token
            xinYanData.setToken(JSONObject.parseObject(result).getString("token"));
            // 数据类型
            xinYanData.setDataType(dataType);
            insert(xinYanData);

            // 用户状态只需要在为原始数据的时候更新一次就可以了
            if (dataType.equals(BusinessConst.ORIGINAL_DATA)) {
                // 同时更新用户关联的状态表，更新雷达认证状态字段
                BSysUserStatus bSysUserStatus = new BSysUserStatus();
                bSysUserStatus.setUserId(userId);

                if (XinYanConstantEnum.API_NAME_LD.getApiName().equals(apiName))
                    bSysUserStatus.setXinyanRadarStatus(BusinessConst.OK);
                if (XinYanConstantEnum.API_NAME_YYS.getApiName().equals(apiName))
                    bSysUserStatus.setXinyanMobileStatus(BusinessConst.OK);
                if (XinYanConstantEnum.API_NAME_JH.getApiName().equals(apiName)) {
                    bSysUserStatus.setXinyanTaobaoStatus(BusinessConst.OK);
                }

                this.bSysUserStatusService.updateByUserId(bSysUserStatus);
            }
        }).start();
    }
}
