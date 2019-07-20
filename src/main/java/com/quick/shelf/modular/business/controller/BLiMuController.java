package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.core.common.annotion.CallBackLog;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.core.util.RedisUtil;
import com.quick.shelf.modular.business.entity.BLiMuData;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.service.BLiMuService;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.warpper.BLiMuWrapper;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantEnum;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantMethod;
import com.quick.shelf.modular.creditPort.liMu.LiMuResult;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstantMethod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @ClassName 立木征信控制层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 19:33
 * @Version 1.0
 */
@Api(value = "BLiMuController", tags = "BLiMuController")
@Controller
@RequestMapping(value = "/liMu")
public class BLiMuController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BLiMuController.class);

    private static final String PATH = "/modular/business/liMu/";

    @Resource
    private BLiMuService bLiMuService;

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private RedisUtil redisUtil;

    /**
     * 立木征信报告跳转页面
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return PATH + "index.html";
    }

    /**
     * 查询立木征信列表
     *
     * @author zcn
     * @Date 2018/12/24 22:43
     */
    @ApiOperation(value = "查询立木征信列表", notes = "查询立木征信列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "查询参数:姓名/账户/手机号", name = "name", dataType = "String"),
            @ApiImplicitParam(value = "时间查询", name = "timeLimit", dataType = "String"),
            @ApiImplicitParam(value = "部门主键", name = "deptId", dataType = "Long")
    })
    @RequestMapping("/list")
    @Permission
    @ResponseBody
    public Object list(@RequestParam(required = false) String name,
                       @RequestParam(required = false) String timeLimit,
                       @RequestParam(required = false) Long deptId) {

        logger.info("查询立木征信列表");

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> users = bLiMuService.selectBLiMuDatas(null, name, beginTime, endTime, deptId);
            Page wrapped = new BLiMuWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> users = bLiMuService.selectBLiMuDatas(dataScope, name, beginTime, endTime, deptId);
            Page wrapped = new BLiMuWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
    }

    /**
     * 获取立木运营商报告
     *
     * @return
     */
    @BussinessLog(value = "获取立木运营商报告")
    @ApiOperation(value = "获取立木运营商报告", notes = "获取立木运营商报告", httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @Permission
    @RequestMapping(value = "/getMobileReport/{userId}")
    public String getMobileReport(@PathVariable Integer userId, Model model) {
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_YYS.getApiName(), BusinessConst.PAGE_DATA.toString());
        model.addAttribute("mobileReport", BLiMuWrapper.replaceHtmlUrl(data.getDataValue()));
        return PATH + "liMu_mobileReport.html";
    }

    /**
     * 获取淘宝认证报告
     *
     * @return
     */
    @BussinessLog(value = "获取淘宝认证报告")
    @ApiOperation(value = "获取淘宝认证报告", notes = "获取淘宝认证报告", httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @Permission
    @RequestMapping(value = "/getTaoBaoReport/{userId}")
    public String getTaoBaoReport(@PathVariable Integer userId, Model model) {
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_TB.getApiName(), BusinessConst.PAGE_DATA.toString());
        model.addAttribute("taoBaoReport", BLiMuWrapper.replaceHtmlUrl(data.getDataValue()));
        return PATH + "liMu_taoBaoReport.html";
    }

    /**
     * 获取立方升级报告
     *
     * @return
     */
    @BussinessLog(value = "获取立方升级报告")
    @ApiOperation(value = "获取立方升级报告", notes = "获取立方升级报告", httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @Permission
    @RequestMapping(value = "/getLiFangUpgradeCheck/{userId}")
    public String getLiFangUpgradeCheck(@PathVariable Integer userId, Model model) {
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_LFSJ.getApiName(), BusinessConst.PAGE_DATA.toString());
        model.addAttribute("liFangUpgradeCheck", BLiMuWrapper.replaceHtmlUrl(data.getDataValue()));
        return PATH + "liMu_liFangUpgradeCheck.html";
    }

    /**
     * 获取立木设备指纹
     *
     * @return
     */
    @BussinessLog(value = "获取立木设备指纹")
    @ApiOperation(value = "获取立木设备指纹", notes = "获取立木设备指纹", httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @Permission
    @RequestMapping(value = "/getFingerprint/{userId}")
    public String getFingerprint(@PathVariable Integer userId, Model model) {
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_SBZW.getApiName(), BusinessConst.ORIGINAL_DATA.toString());
        model.addAttribute("fingerprint", JSONObject.parseObject(data.getDataValue()));
        return PATH + "liMu_fingerprint.html";
    }

    /**
     * 获取立木机审报告
     *
     * @return
     */
    @BussinessLog(value = "获取立木机审报告")
    @ApiOperation(value = "获取立木机审报告", notes = "获取立木机审报告", httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @Permission
    @RequestMapping(value = "/getMachineCheck/{userId}")
    public String getMachineCheck(@PathVariable Integer userId, Model model) {
        BSysUser bSysUser = bSysUserService.selectBSysUserByUserId(userId);
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_JS.getApiName(), BusinessConst.ORIGINAL_DATA.toString());
        model.addAttribute("machineCheck", JSONObject.parseObject(data.getDataValue()));
        model.addAttribute("bSysUser", bSysUser);
        return PATH + "liMu_machineCheck.html";
    }

    /**
     * 立木认证页面跳转页
     *
     * @param type  报告类型 （type 定义在 LiMuConstantEnum 枚举类）
     * @param token 用户token信息
     * @return
     */
    @BussinessLog(value = "立木认证页面跳转页")
    @ApiOperation(value = "立木认证页面跳转页", notes = "立木认证页面跳转页", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "报告类型", name = "type", required = true, dataType = "String"),
            @ApiImplicitParam(value = "用户Token信息", name = "token", required = true, dataType = "String")
    })
    @RequestMapping(value = "/toVerifyUrl/{type}/{userId}", method = RequestMethod.GET)
    @ResponseBody
    public String toVerifyUrl(@PathVariable("type") String type, @PathVariable("userId") String userId) {
        logger.info("用户：{} 获取了:{} 的认证页面", userId, type);
        // 查询用户信息
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(Integer.valueOf(userId));
        String signUrl = getProjectPath() + "/liMu/sign";
        String callBackUrl = getProjectPath() + "/liMu/callBack/" + type + "/" + userId;
        return LiMuConstantMethod.getLimuVerifyUrl(bSysUser, type, signUrl, callBackUrl);
    }

    /**
     * 立木数据回调通知地址
     */
    @CacheEvict(value = BusinessConst.CONSOLE_PORT, allEntries = true)
    @CallBackLog(value = "立木数据回调通知")
    @ApiOperation(value = "立木数据回调通知", notes = "立木数据回调通知", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "认证类型", name = "type", required = true, dataType = "String"),
            @ApiImplicitParam(value = "用户主键ID", name = "userId", required = true, dataType = "String")
    })
    @RequestMapping(value = "/callBack/{type}/{userId}")
    public void callBack(LiMuResult liMuResult, @PathVariable("userId") String userId, @PathVariable("type") String type) {
        logger.info("新颜征信回调返回了用户：{} 的{} 类型的认证数据", userId, type);
        // 设置缓存立木回调数据 单位秒：60 * 60 * 24 * 365
        redisUtil.set(XinYanConstantMethod.REDIS_KEY + DateUtil.getCurrentTimestampMs() + "-" + userId + "-" + liMuResult.getToken() + "-" + type,
                userId + "-" + liMuResult.getToken() + "-" + type + "-" + DateUtil.getCurrentDateString(), 60 * 60 * 24 * 365);

        if (liMuResult.getCode().equals(LiMuConstantMethod.SUCCESS_CODE)) {
            BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(Integer.valueOf(userId));
            // 设置多个判断是为了让每个接口进行自己独立的接口调用统计
            // 淘宝认证
            if (LiMuConstantEnum.API_NAME_TB.getApiName().equals(type)) {
                // 重点 返回的类型与定义的类型不一样
                liMuResult.setBizType(type);
                // 获取立木淘宝的原始数据 并 保存
                this.bLiMuService.liMuTBJsonData(liMuResult);
                // 获取立木淘宝的报告页面 并 保存
                this.bLiMuService.liMuTBPageData(liMuResult);
                // 更
            }
            // 运营商认证
            if (LiMuConstantEnum.API_NAME_YYS.getApiName().equals(type)) {
                // 获取立木运营商的原始数据 并 保存
                String result = this.bLiMuService.liMuYYSJsonData(liMuResult);
                JSONObject jsonObject = JSONObject.parseObject(result);
                // 防止没有身份证和用户姓名导致报错
                bSysUser.setName(jsonObject.getJSONObject("data").getJSONObject("basicInfo").getString("name"));
                bSysUser.setIdCard(jsonObject.getJSONObject("data").getJSONObject("basicInfo").getString("identityNo"));
                // 获取立木运营商的报告页面 并 保存
                this.bLiMuService.liMuYYSPageData(liMuResult);

                // 异步执行获取立方升级报告的方法
                // 获取运营商报告后在获取立方升级报告
                String lfsjToken = this.bLiMuService.liMuLfsjJsonData(bSysUser);
                lfsjToken = JSONObject.parseObject(lfsjToken).getString("token");
                // 获取完立木升级的原始报告后，在拿返回的token获取立木的页面报告
                if (null != lfsjToken && !lfsjToken.equals("error")) {
                    // 对象copy 不破坏原有数据的完整性
                    LiMuResult lm = new LiMuResult();
                    BeanUtils.copyProperties(liMuResult, lm);
                    lm.setBizType(LiMuConstantEnum.API_NAME_LFSJ.getApiName());
                    lm.setToken(lfsjToken);
                    this.bLiMuService.liMuLfsjPageData(lm);
                    // 改变用户立方升级状态
                    this.bLiMuService.changeUserStatus(bSysUser.getUserId(), LiMuConstantEnum.API_NAME_LFSJ.getApiName());
                }
            }
            if (LiMuConstantEnum.API_NAME_LFSJ.getApiName().equals(type)) {
                System.out.println(type);
            }
            // 指纹设备
            if (LiMuConstantEnum.API_NAME_SBZW.getApiName().equals(type)) {
                // 获取立木设备指纹的原始数据 并 保存
                this.bLiMuService.liMuSbzwJsonData(liMuResult);
            }
//            // 每次都执行获取立木的机审报告
            this.bLiMuService.liMuJsJsonData(liMuResult);

            // 改变用户状态
            this.bLiMuService.changeUserStatus(bSysUser.getUserId(), type);
        }
    }

    /**
     * 立木征信 签名方式
     * 签名地址格式：{signUrl }?message={H5 签名 message}&callback=signHandler
     * 签名算法：sha1(message+apiSecret)
     * 权限检查：已登录用
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/sign", produces = "application/x-javascript")
    @ResponseBody
    public String sign(HttpServletRequest request, HttpServletResponse response) {
        String message = request.getParameter("message");
        String callback = request.getParameter("callback");
        //签名
        String sign = DigestUtils.sha1Hex(message + LiMuConstantMethod.APISECRET);
        //返回字条串
        response.setHeader("Content-Type", "application/x-javascript;charset=UTF-8");
        return callback + "('" + sign + "')";
    }
}
