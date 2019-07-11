package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.exception.BizExceptionEnum;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.*;
import com.quick.shelf.modular.business.service.*;
import com.quick.shelf.modular.business.warpper.BSysUserWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @ClassName 用户管理控制层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/10 15:11
 * @Version 1.0
 */
@Api(value = "BSysUserController", tags = "BSysUserController")
@Controller
@RequestMapping(value = "/bSysUser")
public class BSysUserController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BSysUserController.class);

    private static final String PREFIX = "/modular/business/bSysUser/";

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BUserBasicInfoServer bUserBasicInfoServer;

    @Resource
    private BEmergencyContactInfoService bEmergencyContactInfoService;

    @Resource
    private BIdentityInfoService bIdentityInfoService;

    @Resource
    private BBankCardInfoService bBankCardInfoService;

    /**
     * 跳转到用户管理界面
     */
    @ApiOperation(value = "跳转到用户管理界面", notes = "跳转到用户管理界面", httpMethod = "GET")
    @RequestMapping(value = "/index")
    public String index() {
        return PREFIX + "bSysUser.html";
    }

    /**
     * 查询用户列表
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @ApiOperation(value = "查询用户列表", notes = "查询用户列表", httpMethod = "POST")
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

        logger.info("查询用户列表");

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> users = bSysUserService.selectBSysUsers(null, name, beginTime, endTime, deptId);
            Page wrapped = new BSysUserWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> users = bSysUserService.selectBSysUsers(dataScope, name, beginTime, endTime, deptId);
            Page wrapped = new BSysUserWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
    }

    /**
     * 跳转查看用户详情
     *
     * @return
     */
    @ApiOperation(value = "跳转查看用户详情", notes = "跳转查看用户详情", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户主键ID", name = "user_id", required = true, dataType = "Integer")
    })
    @Permission
    @RequestMapping(value = "/details")
    public String details(@RequestParam Integer userId, Model model) {
        if (ToolUtil.isEmpty(userId)) {
            throw new ServiceException(BizExceptionEnum.REQUEST_NULL);
        }
        /**
         * 用户主表 b_sys_user
         */
        BSysUser bSysUser = this.bSysUserService.getById(userId);
        model.addAttribute("bSysUser", BSysUserWrapper.deSensitization(bSysUser));

        /**
         * 用户基本信息 b_user_basic_info
         */
        BUserBasicInfo bUserBasicInfo = this.bUserBasicInfoServer.selectBUserBasicInfoByUserId(userId);
        model.addAttribute("bUserBasicInfo", bUserBasicInfo);

        /**
         * 用户联系人信息 b_emergency_contact_info
         */
        BEmergencyContactInfo bEmergencyContactInfo = this.bEmergencyContactInfoService.selectBEmergencyContactInfoByUserId(userId);
        model.addAttribute("bEmergencyContactInfo", BSysUserWrapper.deSensitization(bEmergencyContactInfo));

        /**
         *  用户认证身份证信息 b_identity_info
         */
        BIdentityInfo bIdentityInfo = this.bIdentityInfoService.selectBIdentityInfoByUserId(userId);
        model.addAttribute("bIdentityInfo",BSysUserWrapper.deSensitization(bIdentityInfo));

        /**
         * 用户所关联的银行卡信息 b_bank_card_info
         * 一对多的关系
         */
        List<BBankCardInfo> bBankCardInfos = this.bBankCardInfoService.selectBBankCardInfosByUserId(userId);
        model.addAttribute("bBankCardInfos",BSysUserWrapper.deSensitization(bBankCardInfos));
        return PREFIX + "user_details.html";
    }
}
