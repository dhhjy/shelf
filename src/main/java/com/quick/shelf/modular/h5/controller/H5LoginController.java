package com.quick.shelf.modular.h5.controller;

import com.alibaba.fastjson.JSONObject;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.service.BSysUserStatusService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @ClassName H5LoginController
 * @Description TODO
 * @Author ken
 * @Date 2019/7/16 13:54
 * @Version 1.0
 */
@Api(value = "H5页面控制层", tags = "H5LoginController")
@Controller
@RequestMapping(value = "/h5")
public class H5LoginController extends BaseController {

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(H5LoginController.class);

    private static final String H5_PATH = "/H5/";

    /**
     * H5客户端登录页面跳转
     *
     * @return
     */
    @ApiOperation(value = "H5客户端登录页面跳转", notes = "H5客户端登录页面跳转", httpMethod = "GET")
    @RequestMapping(value = {"", "/", "login"}, method = RequestMethod.GET)
    public String login() {
        return H5_PATH + "login.html";
    }

    /**
     * H5客户端主页跳转
     *
     * @return
     */
    @ApiOperation(value = "H5客户端主页跳转", notes = "H5客户端主页跳转", httpMethod = "GET")
    @Permission
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return H5_PATH + "/index.html";
    }

    /**
     * 客户端用户主页
     *
     * @return String
     * @author zcn
     */
    @ApiOperation(value = "客户端用户主页", notes = "客户端用户主页", httpMethod = "GET")
    @RequestMapping("/console")
    public String console(Model model) {
        BSysUserStatus userStatus = bSysUserStatusService.selectBSysUserStatusByUserId(Objects.requireNonNull(ShiroKit.getUser()).getId().intValue());
        model.addAttribute("userStatus", JSONObject.toJSON(userStatus));
        return H5_PATH + "frame/console.html";
    }

    /**
     * 客户端注册页面跳转
     *
     * @return
     */
    @ApiOperation(value = "客户端注册页面跳转", notes = "客户端注册页面跳转", httpMethod = "GET")
    @RequestMapping(value = "/registerIndex")
    public String registerIndex() {
        return H5_PATH + "register.html";
    }

    /**
     * 客户端找回密码页面跳转
     *
     * @return
     */
    @ApiOperation(value = "客户端找回密码页面跳转", notes = "客户端找回密码页面跳转", httpMethod = "GET")
    @RequestMapping(value = "/findPassword")
    public String findPassword() {
        return H5_PATH + "findPassword.html";
    }
}
