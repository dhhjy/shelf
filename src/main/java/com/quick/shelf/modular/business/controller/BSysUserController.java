package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.base.controller.BaseController;
import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.warpper.BSysUserWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
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
@RequestMapping(value = "/business/bSysUser")
public class BSysUserController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BSysUserController.class);

    private static final String PREFIX = "/modular/business/bSysUser/";

    @Resource
    private BSysUserService bSysUserService;

    /**
     * 跳转到用户管理界面
     */
    @ApiOperation(value = "跳转到用户管理界1面",notes = "跳转到用户管理界面",httpMethod = "GET")
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
    @ApiOperation(value = "查询用户列表",notes = "查询用户列表",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "查询参数:姓名/账户/手机号",name="name",dataType = "String"),
            @ApiImplicitParam(value = "时间查询",name="timeLimit",dataType = "String"),
            @ApiImplicitParam(value = "部门主键",name="deptId",dataType = "Long")
    })
    @RequestMapping("/list")
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
}
