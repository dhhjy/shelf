package com.quick.shelf.modular.business.controller;

import com.alibaba.fastjson.JSONObject;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.modular.business.entity.BBankCardInfo;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BXinYanData;
import com.quick.shelf.modular.business.service.BBankCardInfoService;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.service.BXinYanDataService;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstant;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 新颜征信控制层
 *
 * @author zcn
 * @date 2019/07/12
 */
@Api(value = "新颜征信控制层")
@Controller
@RequestMapping("/xinYan")
public class XinYanController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BSysUserController.class);

    private static final String PREFIX = "/modular/business/xinYan/";

    @Resource
    private BXinYanDataService bXinYanDataService;

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BBankCardInfoService bBankCardInfoService;

    /**
     * 新颜全景雷达报告获取，获取原始数据，
     * 如果数据存在则直接返回，如果不存在则先调用接口查询
     * 存到数据库后在返回页面
     *
     * @param userId
     * @param model
     * @return
     */
    @BussinessLog(value = "获取全景雷达报告")
    @ApiOperation(value = "新颜全景雷达报告获取，获取原始数据", notes = "新颜全景雷达报告获取，获取原始数据", httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/getReDerData/{userId}")
    public String getReDerData(@PathVariable Integer userId, Model model) {
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(userId);
        BBankCardInfo bBankCardInfo = this.bBankCardInfoService.getBBankCardInfosByUserId(userId);
        String bankNumber = null;
        if (null != bBankCardInfo)
            bankNumber = bBankCardInfo.getCardNumber();
        logger.info("用户主键 {} 获取新颜雷达报告", userId);
        BXinYanData xinYanLd = this.bXinYanDataService.selectBXinYanDataByUserId(userId, XinYanConstantEnum.API_NAME_LD.getApiName());
        if (xinYanLd != null) {
            model.addAttribute("radarData", JSONObject.parseObject(xinYanLd.getDataValue()));
        } else if (bSysUser != null) {
            String base64Str = XinYanConstant.assembleEncryptParams(String.valueOf(userId), bSysUser.getIdCard(),
                    bSysUser.getName(), bSysUser.getPhoneNumber(), bankNumber);
            String result = XinYanConstant.getRaDerResult(base64Str);
            JSONObject jsonResult = JSONObject.parseObject(result);
            if (jsonResult.getString("errorCode") == null) {
                // 异步新增
                new Thread(() -> {
                    BXinYanData xinYanData = new BXinYanData();
                    // userId
                    xinYanData.setUserId(userId);
                    // 设置服务类型
                    String apiName = XinYanConstantEnum.API_NAME_LD.getApiName();
                    xinYanData.setType(apiName);
                    // 设置服务类型中文名称
                    xinYanData.setTypeText(XinYanConstant.compareApiName(apiName));
                    // 添加数据
                    xinYanData.setDataValue(result);
                    this.bXinYanDataService.insert(xinYanData);
                }).start();
            }
            model.addAttribute("radarData", JSONObject.parse(result));
        }
        model.addAttribute("bSysUser", bSysUser);
        return PREFIX + "xinYanReDer.html";
    }
}
