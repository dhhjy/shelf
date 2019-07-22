package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.core.common.annotion.CallBackLog;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.exception.BizExceptionEnum;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.core.util.RedisUtil;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BXinYanData;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.service.BXinYanDataService;
import com.quick.shelf.modular.business.warpper.BXinYanWrapper;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantEnum;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantMethod;
import com.quick.shelf.modular.creditPort.xinYan.XinYanResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Random;

/**
 * 新颜征信控制层
 *
 * @author zcn
 * @date 2019/07/12
 */
@Api(value = "新颜征信控制层")
@Controller
@RequestMapping("/xinYan")
public class BXinYanController extends BaseController {
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
    private RedisUtil redisUtil;

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
    @Permission
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String timeLimit,
                       @RequestParam(required = false) Long deptId) {

        logger.info("查询新颜征信列表");

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
            Page wrapped = new BXinYanWrapper(xinYanDatas).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> xinYanDatas = bXinYanDataService.selectBXinYanDatas(dataScope, name, beginTime, endTime, deptId);
            Page wrapped = new BXinYanWrapper(xinYanDatas).wrap();
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
    @Permission
    @RequestMapping(value = "/getReDerData/{userId}", produces = "text/plain;charset=utf-8")
    public String getReDerData(@PathVariable Integer userId, Model model) {
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(userId);
        logger.info("用户主键 {} 获取新颜雷达报告", userId);
        BXinYanData xinYanLd = this.bXinYanDataService.selectBXinYanDataByUserId(userId, XinYanConstantEnum.API_NAME_LD.getApiName());
        if (xinYanLd != null) {
            model.addAttribute("radarData", JSONObject.parseObject(xinYanLd.getDataValue()));
        } else if (bSysUser != null) {
            String result = this.bXinYanDataService.getReDerData(userId, bSysUser);
            JSONObject jsonResult = JSONObject.parseObject(result);
            if (jsonResult.getString("errorCode") == null) {
                // 当接口返回正常 即没有 errorCode时，进行
                // 新增操作，新增时同时修改用户相关的 状态 'b_sys_user_status'
                this.bXinYanDataService.assemble(userId, result, XinYanConstantEnum.API_NAME_LD.getApiName());
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
        logger.info("用户：{} 获取用户芝麻分报告", userId);
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(userId);
        BXinYanData zmf = this.bXinYanDataService.selectBXinYanDataByUserId(userId, XinYanConstantEnum.API_NAME_TB.getApiName());
        model.addAttribute("taoBaoWeb", null == zmf ? null : JSONObject.parseObject(zmf.getDataValue()));
        model.addAttribute("bSysUser", bSysUser);
        return PREFIX + "xinYanTaoBaoWeb.html";
    }

    /**
     * 新颜认证页面跳转页
     *
     * @param userId 在此处被用作任务ID 每次执行查询时的 taskId = userId
     * @param type   征信的类型 定义在 XinYanConstantEnum 类中
     */
    @BussinessLog(value = "新颜认证页面跳转页")
    @ApiOperation(value = "新颜认证页面跳转页", notes = "新颜认证页面跳转页", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "报告类型", name = "type", required = true, dataType = "String"),
            @ApiImplicitParam(value = "用户主键，用户主键也做每次任务的任务ID", name = "userId", required = true, dataType = "String")
    })
    @RequestMapping(value = "/index/{type}/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public String index(@PathVariable String userId, @PathVariable String type) {
        logger.info("用户：{} 获取了:{} 的认证页面", userId, type);
        //  例：19466 + suffix 如果有一份报告 16466 + "_111"
        String suffix = "_" + new Random().nextInt(10000);
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(Integer.valueOf(userId));
        // 原始数据回调
        String callbackJson = getProjectPath() + "/xinYan/callbackJson/" + userId;
        // 正式接入时使用下面这个即可
        return XinYanConstantMethod.getXinYanH5Url(bSysUser.getUserId() + suffix, type, callbackJson, null);
    }

    /**
     * 查询结果通知地址
     * 新颜原始数据回调
     *
     * @return
     */
    @CacheEvict(value = BusinessConst.CONSOLE_PORT, allEntries = true)
    @CallBackLog(value = "新颜原始数据回调地址")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户主键ID", name = "userId", required = true, dataType = "String")
    })
    @RequestMapping(value = "/callbackJson/{userId}", produces = "application/json", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void callbackJson(@RequestBody XinYanResult xyResult, @PathVariable("userId") Integer userId) {
        logger.info("新颜征信回调返回了用户：{} 的{} 类型的认证数据", userId, xyResult.getApiName());
        // 设置缓存立木回调数据 单位秒：60 * 60 * 24 * 365
        redisUtil.set(XinYanConstantMethod.REDIS_KEY + DateUtil.getCurrentTimestampMs() + "-" + userId + "-" + xyResult.getToken() + "-" + xyResult.getApiName(),
                userId + "-" + xyResult.getToken() + "-" + xyResult.getApiName() + "-" + DateUtil.getCurrentDateString(), 60 * 60 * 24 * 365);
        xyResult.setTaskId(String.valueOf(userId));
        // 请求成功后进行查询数据并且保存的操作
        if (XinYanConstantMethod.SUCCESS.equals(xyResult.getSuccess())) {
            // 设置多个选项的目的是为了记录日志，并且通过AOP的方式
            // 统计接口统计
            if (XinYanConstantEnum.API_NAME_TB.getApiName().equals(xyResult.getApiName()))
                // 保存新颜芝麻分(淘宝)的原始数据
                this.bXinYanDataService.xinYanTBJsonData(xyResult);
        }
    }
}
