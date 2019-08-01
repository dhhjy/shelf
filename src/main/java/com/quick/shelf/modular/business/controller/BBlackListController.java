package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.response.ResponseData;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.service.BBlackListService;
import com.quick.shelf.modular.business.warpper.BBlackListWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 用户黑名单控制层
 */
@Api(value = "用户黑名单控制层", tags = "BBlackListController")
@Controller
@RequestMapping(value = "/blackList")
public class BBlackListController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BBlackListController.class);

    public static final String PREFIX = "/modular/business/blackList/";

    @Resource
    private BBlackListService bBlackListService;

    /**
     * 黑名单主页跳转
     */
    @ApiOperation(value = "黑名单主页跳转", notes = "黑名单主页跳转", httpMethod = "GET")
    @Permission
    @RequestMapping(value = "/index")
    public String index() {
        return PREFIX + "blackList.html";
    }

    /**
     * 查询黑名单列表
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:43
     */
    @ApiOperation(value = "查询黑名单列表", notes = "查询黑名单列表", httpMethod = "POST")
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

        logger.info("查询黑名单列表");

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> users = bBlackListService.selectBlackLists(null, name, beginTime, endTime, deptId);
            Page wrapped = new BBlackListWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> users = bBlackListService.selectBlackLists(dataScope, name, beginTime, endTime, deptId);
            Page wrapped = new BBlackListWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
    }

    /**
     * 解除用户黑名单限制
     *
     * @param userId
     * @return
     */
    @BussinessLog(value = "解除黑名单")
    @ApiOperation(value = "解除用户黑名单限制", notes = "解除用户黑名单限制", httpMethod = "PUT")
    @ApiImplicitParam(value = "用户主键ID", name = "userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/unBlackList/{userId}", method = RequestMethod.PUT)
    @ResponseBody
    public ResponseData unBlackList(@PathVariable("userId") Integer userId) {
        this.bBlackListService.unBlackList(userId);
        return ResponseData.success(0, "解除成功", null);
    }
}
