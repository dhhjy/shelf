package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.response.ResponseData;
import com.quick.shelf.modular.business.service.BStagingListService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Map;

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

    @Resource
    private BStagingListService bStagingListService;

    /**
     * 分期账单主页跳转
     * <p>
     * 账单需要指定userId 即被查询的用户主键
     *
     * @return
     */
    @ApiOperation(value = "分期账单主页跳转", notes = "分期账单主页跳转", httpMethod = "POST")
    @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer")
    @Permission
    @RequestMapping(value = "/index/{userId}/{orderNumber}/{target}")
    public String index(@PathVariable("userId") Integer userId, @PathVariable("orderNumber") String orderNumber, @PathVariable("target") String target, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("orderNumber", orderNumber);
        model.addAttribute("target", target);
        return PREFIX + "stagingListIndex.html";
    }

    /**
     * 用户还款账单
     * 需要传入用户的主键 userId
     */
    @ApiOperation(value = "用户还款账单", notes = "用户还款账单", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户主键", name = "userId", required = true, dataType = "Integer"),
            @ApiImplicitParam(value = "订单号", name = "orderNumber", required = true, dataType = "String"),
            @ApiImplicitParam(value = "账单时间", name = "timeLimit", dataType = "String"),
            @ApiImplicitParam(value = "还款状态", name = "status", dataType = "Integer")
    })
    @RequestMapping(value = "/list/{userId}")
    @Permission
    @ResponseBody
    public Object list(@PathVariable("userId") Integer userId,
                       @RequestParam String orderNumber,
                       @RequestParam(required = false) String timeLimit,
                       @RequestParam(required = false) Integer status) {
        logger.info("查询主键为{}用户的还款账单", userId);
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        Page<Map<String, Object>> orderDetails = bStagingListService.selectBStagingListByUserId(null, beginTime, endTime, userId, orderNumber, status);
        return LayuiPageFactory.createPageInfo(orderDetails);
    }

    /**
     * 提前还款
     */
    @ApiOperation(value = "提前还款", notes = "提前还款", httpMethod = "POST")
    @ApiImplicitParam(value = "借款订单号", name = "orderNumber", required = true, dataType = "String")
    @BussinessLog(value = "提前还款")
    @Permission
    @RequestMapping(value = "/adjRepayment")
    @ResponseBody
    public ResponseData adjRepayment(String orderNumber) {
        // 异步操作还款
        new Thread(() -> this.bStagingListService.adjRepayment(orderNumber)).start();
        return ResponseData.success(0, "还款成功", null);
    }

    /**
     * 手动还款
     */
    @ApiOperation(value = "手动还款", notes = "手动还款", httpMethod = "POST")
    @ApiImplicitParam(value = "主键", name = "id", required = true, dataType = "Integer")
    @BussinessLog(value = "手动还款")
    @Permission
    @RequestMapping(value = "/repayment")
    @ResponseBody
    public ResponseData repayment(Integer id) {
        this.bStagingListService.repayment(id);
        return ResponseData.success(0, "还款成功", null);
    }

    /**
     * 手动逾期
     */
    @ApiOperation(value = "手动逾期", notes = "手动逾期", httpMethod = "POST")
    @ApiImplicitParam(value = "主键", name = "id", required = true, dataType = "Integer")
    @BussinessLog(value = "手动逾期")
    @Permission
    @RequestMapping(value = "/overdue")
    @ResponseBody
    public ResponseData overdue(Integer id) {
        this.bStagingListService.overdue(id);
        return ResponseData.success(0, "操作成功", null);
    }
}
