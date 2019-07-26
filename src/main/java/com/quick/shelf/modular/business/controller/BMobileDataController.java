package com.quick.shelf.modular.business.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.modular.business.service.BMobileDataService;
import com.quick.shelf.modular.business.warpper.BMobileWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
 * @ClassName 电话管理
 * @Description TODO
 * @Author ken
 * @Date 2019/7/26 16:46
 * @Version 1.0
 */
@Api(value = "电话管理", tags = "BMobileDataController")
@Controller
@RequestMapping(value = "/mobile")
public class BMobileDataController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BMobileDataController.class);

    private static final String PREFIX = "/modular/business/mobile/";

    @Resource
    private BMobileDataService bMobileDataService;

    /**
     * 电话管理主页跳转
     *
     * @return
     */
    @ApiOperation(value = "电话管理主页跳转", notes = "电话管理主页跳转", httpMethod = "GET")
    @RequestMapping(value = "/index")
    public String index() {
        return PREFIX + "mobile.html";
    }

    /**
     * 获取电话号码列表
     *
     * @param tMobile
     * @return
     */
    @ApiOperation(value = "获取电话号码列表", notes = "获取电话号码列表", httpMethod = "POST")
    @ApiImplicitParam(value = "运营商类型", name = "tMobile", dataType = "Integer")
    @RequestMapping(value = "/list")
    @ResponseBody
    @Permission
    public Object list(@RequestParam(required = false) Integer tMobile) {
        logger.info("查询号码列表");
        Page<Map<String, Object>> mobiles = this.bMobileDataService.selectBMobileDatas(null, tMobile);
        Page wrapped = new BMobileWrapper( mobiles ).wrap();
        return LayuiPageFactory.createPageInfo(wrapped);
    }
}
