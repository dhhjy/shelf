package com.quick.shelf.modular.business.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 分期账单控制层
 */
@Api(value = "分期账单控制层", tags = "BStagingListController")
@Controller
@RequestMapping(value = "/stagingList")
public class BStagingListController {
    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(BStagingListController.class);

    private static final String PREFIX = "/modular/business/stagingList/";

    /**
     * 分期账单主页跳转
     * <p>
     * 账单需要指定userId 即被查询的用户主键
     *
     * @return
     */
    @ApiOperation(value = "分期账单主页跳转", notes = "分期账单主页跳转", httpMethod = "POST")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/index/{userId}")
    public String index(@PathVariable("userId") Integer userId, Model model) {
        model.addAttribute("userId", userId);
        return PREFIX + "stagingListIndex.html";
    }
}
