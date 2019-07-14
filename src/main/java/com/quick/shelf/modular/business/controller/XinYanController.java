package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.core.common.exception.BizExceptionEnum;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BXinYanData;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.service.BXinYanDataService;
import com.quick.shelf.modular.business.warpper.XinYanWrapper;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstant;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

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

    /**
     * 新颜征信报告页面跳转
     *
     * @return String
     */
    @RequestMapping(value = "/index")
    public String index() {
        return PREFIX + "/xinYan.html";
    }

    /**
     * 获取新颜征信报告列表
     */
    @ApiOperation(value = "获取新颜征信报告列表", notes = "获取新颜征信报告列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "姓名/账号/手机号", name = "name", dataType = "String"),
            @ApiImplicitParam(value = "开始时间/结束时间", name = "timeLimit", dataType = "String"),
            @ApiImplicitParam(value = "部门ID主键", name = "deptId", dataType = "String")
    })
    @RequestMapping(value = "/list")
    @ResponseBody
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String timeLimit,
                       @RequestParam(required = false) Long deptId) {

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> xinYanDatas = bXinYanDataService.selectBXinYanDatas(null, name, beginTime, endTime, deptId);
            Page wrapped = new XinYanWrapper(xinYanDatas).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> xinYanDatas = bXinYanDataService.selectBXinYanDatas(dataScope, name, beginTime, endTime, deptId);
            Page wrapped = new XinYanWrapper(xinYanDatas).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
    }

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
    @RequestMapping(value = "/getReDerData/{userId}", produces = "text/plain;charset=utf-8")
    public String getReDerData(@PathVariable Integer userId, Model model) {
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(userId);
        logger.info("用户主键 {} 获取新颜雷达报告", userId);
        BXinYanData xinYanLd = this.bXinYanDataService.selectBXinYanDataByUserId(userId, XinYanConstantEnum.API_NAME_LD.getApiName());
        if (xinYanLd != null) {
            model.addAttribute("radarData", JSONObject.parseObject(xinYanLd.getDataValue()));
        } else if (bSysUser != null) {
            String base64Str = XinYanConstant.assembleEncryptParams(String.valueOf(userId), bSysUser.getIdCard(),
                    bSysUser.getName(), bSysUser.getPhoneNumber(), null);
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
        } else {
            throw new ServiceException(BizExceptionEnum.USER_NOT_EXISTED);
        }
        model.addAttribute("bSysUser", bSysUser);
        return PREFIX + "xinYanReDer.html";
    }

    /**
     * 获取用户芝麻分数据（淘宝报告数据）
     * 新颜淘宝数据主要使用在芝麻分上，所以，
     * 前台页面的数据叫做芝麻分
     *
     * @return
     */
    @BussinessLog(value = "获取用户芝麻分报告（淘宝报告）")
    @ApiOperation(value = "获取用户芝麻分报告", notes = "获取用户芝麻分报告", httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/getTaoBaoWebReport/{userId}")
    public String getTaoBaoWebReport(@PathVariable Integer userId, Model model) {
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(userId);
        BXinYanData xinYanLd = this.bXinYanDataService.selectBXinYanDataByUserId(userId, XinYanConstantEnum.API_NAME_TB.getApiName());
        model.addAttribute("taoBaoWeb",  JSONObject.parseObject(xinYanLd.getDataValue()));
        model.addAttribute("bSysUser", bSysUser);
        return PREFIX + "xinYanTaoBaoWeb.html";
    }
}
