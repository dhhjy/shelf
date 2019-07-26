package com.quick.shelf.modular.h5.controller;

import com.alibaba.fastjson.JSONObject;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.response.ResponseData;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.core.util.RedisUtil;
import com.quick.shelf.core.util.ShortUrlUtil;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.service.BSysUserStatusService;
import com.quick.shelf.modular.h5.model.RegisterDto;
import com.quick.shelf.modular.h5.service.H5LogingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

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
    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(H5LoginController.class);

    private static final String H5_PATH = "/H5/";

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    @Resource
    private H5LogingService h5LogingService;

    @Resource
    private RedisUtil redisUtil;

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
    public String index(Model model) {
        ShiroUser user = ShiroKit.getUserNotNull();
        model.addAttribute("userStatus", JSONObject.toJSON(this.bSysUserStatusService.selectBSysUserStatusByUserId(user.getId().intValue())));
        return H5_PATH + "/index.html";
    }

    /**
     * 客户端注册页面跳转
     *
     * @return
     */
    @ApiOperation(value = "客户端注册页面跳转", notes = "客户端注册页面跳转", httpMethod = "GET")
    @RequestMapping(value = {"/registerIndex"}, method = RequestMethod.GET)
    public String registerIndex(Long promoteId, Model model) {
        if (promoteId == null || promoteId == 0L)
            promoteId = 21L;
        model.addAttribute("deptId", promoteId);
        return H5_PATH + "register.html";
    }

    /**
     * 客户端找回密码页面跳转
     *
     * @return
     */
    @ApiOperation(value = "客户端找回密码页面跳转", notes = "客户端找回密码页面跳转")
    @RequestMapping(value = "/findPasswordIndex")
    public String findPasswordIndex() {
        return H5_PATH + "findPassword.html";
    }

    /**
     * 客户端用户找回密码
     *
     * @return
     */
    @ApiOperation(value = "客户端用户找回密码", notes = "客户端用户找回密码", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "userAccount", required = true, dataType = "String"),
            @ApiImplicitParam(value = "验证码", name = "vercode", required = true, dataType = "String"),
            @ApiImplicitParam(value = "密码", name = "password", required = true, dataType = "String"),
            @ApiImplicitParam(value = "注册后属于哪家公司", name = "deptId", required = true, dataType = "Long"),
    })
    @RequestMapping(value = "/findPassword", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData findPassword(RegisterDto registerDto) {
        String message;
        String phone = registerDto.getUserAccount();
        logger.info("手机号:{}的用户正在找回密码！", phone);
        String code = redisUtil.get(phone).toString();
        // 删除缓存
        redisUtil.del(phone);
        // 校验验证码是否正确
        if (null != code && null != registerDto.getVercode() && !code.equals(registerDto.getVercode())) {
            message = "验证码不正确";
            return ResponseData.success(3, message, null);
        }
        // 校验手机号是否存在
        else if (this.bSysUserService.phoneIsExist(phone)) {
            message = "手机号不存在";
            return ResponseData.success(2, message, null);
        } else {
            message = h5LogingService.findPassword(registerDto);
            return ResponseData.success(0, message, registerDto);
        }
    }

    /**
     * 客户端用户注册
     *
     * @param bSysUser 用户对象
     * @param vercode  注册验证码
     */
    @ApiOperation(value = "客户端用户注册", notes = "客户端用户注册", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "手机号", name = "userAccount", required = true, dataType = "String"),
            @ApiImplicitParam(value = "验证码", name = "vercode", required = true, dataType = "String"),
            @ApiImplicitParam(value = "密码", name = "password", required = true, dataType = "String"),
            @ApiImplicitParam(value = "注册后属于哪家公司", name = "deptId", required = true, dataType = "Long"),
    })
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData register(RegisterDto registerDto) {
        String message;
        String phone = registerDto.getUserAccount();
        logger.info("手机号:{}的用户正在注册！", phone);
        String code = redisUtil.get(phone).toString();
        // 删除缓存
        redisUtil.del(phone);
        // 校验验证码是否正确
        if (null != code && null != registerDto.getVercode() && !code.equals(registerDto.getVercode())) {
            message = "验证码不正确";
            return ResponseData.success(3, message, null);
        }
        // 校验是否重复注册
        else if (!this.bSysUserService.phoneIsExist(phone)) {
            message = "该手机号已注册";
            return ResponseData.success(2, message, null);
        } else {
            message = h5LogingService.registerBSysUser(registerDto);
            return ResponseData.success(0, message, registerDto);
        }
    }

    /**
     * 获取分公司推广链接
     *
     * @return
     */
    @ApiOperation(value = "获取分公司推广链接", notes = "获取分公司推广链接", httpMethod = "POST")
    @RequestMapping(value = "/promoteLinks", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData getPromoteLinks() {
        String oldUrl = getProjectPath() + "/h5/registerIndex?promoteId=" + ShiroKit.getUserNotNull().getDeptId();
        return ResponseData.success(200, oldUrl, ShortUrlUtil.getShortUrl(oldUrl));
    }
}
