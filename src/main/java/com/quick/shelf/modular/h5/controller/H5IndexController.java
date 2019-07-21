package com.quick.shelf.modular.h5.controller;

import com.alibaba.fastjson.JSONObject;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.service.BSysUserStatusService;
import com.quick.shelf.modular.system.service.DictService;
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

    private static final String H5_PATH = "/H5/frame";

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    @Resource
    private DictService dictService;

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
        return H5_PATH + "/console.html";
    }

    /**
     * 个人信息认证页跳转
     *
     * @return
     */
    @ApiOperation(value = "个人信息认证页跳转", notes = "个人信息认证页跳转", httpMethod = "GET")
    @RequestMapping(value = "/personDetailIndex")
    public String personDetailIndex() {
        return H5_PATH + "/person_detail_index.html";
    }
}
