package com.quick.shelf.modular.h5.controller;

import com.alibaba.fastjson.JSONObject;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.service.BSysUserStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName H5IndexController
 * @Description TODO
 * @Author ken
 * @Date 2019/7/20 19:58
 * @Version 1.0
 */
@Api(value = "H5客户端主页控制层", tags = "H5IndexController")
@Controller
@RequestMapping(value = "/h5")
public class H5IndexController extends BaseController {
    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(H5LoginController.class);

    private static final String H5_PATH = "/H5/";

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    @Resource
    private BSysUserService bSysUserService;

    /**
     * 客户端用户主页
     *
     * @return String
     * @author zcn
     */
    @ApiOperation(value = "客户端用户主页", notes = "客户端用户主页", httpMethod = "GET")
    @RequestMapping("/console")
    @Permission
    public String console(Model model) {
        BSysUserStatus userStatus = bSysUserStatusService.selectBSysUserStatusByUserId(Objects.requireNonNull(ShiroKit.getUser()).getId().intValue());
        model.addAttribute("userStatus", JSONObject.toJSON(userStatus));
        return H5_PATH + "/frame/console.html";
    }

    /**
     * 客户端产品列表主页
     *
     * @return String
     * @author zcn
     */
    @ApiOperation(value = "客户端产品列表主页", notes = "客户端产品列表主页", httpMethod = "GET")
    @RequestMapping("/product")
    @Permission
    public String product(Model model) {
        BSysUserStatus userStatus = bSysUserStatusService.selectBSysUserStatusByUserId(Objects.requireNonNull(ShiroKit.getUser()).getId().intValue());
        model.addAttribute("userStatus", JSONObject.toJSON(userStatus));
        return H5_PATH + "/frame/product.html";
    }

    /**
     * 个人信息认证页跳转
     *
     * @return
     */
    @ApiOperation(value = "个人信息认证页跳转", notes = "个人信息认证页跳转", httpMethod = "GET")
    @RequestMapping(value = "/personDetailIndex")
    @Permission
    public String personDetailIndex() {
        return H5_PATH + "/business/person_detail_index.html";
    }

    /**
     * 银行卡认证页面跳转
     *
     * @return
     */
    @ApiOperation(value = "/银行卡认证页面跳转", notes = "银行卡认证页面跳转", httpMethod = "GET")
    @RequestMapping(value = "/bankCardIndex")
    @Permission
    public String bankCardIndex(Model model) {
        ShiroUser user = ShiroKit.getUserNotNull();
        model.addAttribute("user", this.bSysUserService.selectBSysUserByUserId(user.getId().intValue()));
        return H5_PATH + "/business/bank_card_index.html";
    }
}
