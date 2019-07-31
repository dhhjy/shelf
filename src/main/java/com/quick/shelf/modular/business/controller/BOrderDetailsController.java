package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.response.ResponseData;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BEmergencyContactInfo;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.service.*;
import com.quick.shelf.modular.business.warpper.BOrderDetailsWrapper;
import com.quick.shelf.modular.business.warpper.BSysUserWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Api(value = "借款订单控制层", tags = "BOrderDetailsController")
@Controller
@RequestMapping(value = "/orderDetails")
public class BOrderDetailsController extends BaseController {

    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(BOrderDetailsController.class);

    private static final String PREFIX = "/modular/business/orderDetails/";

    /**
     * 待审核订单状态
     */
    private static final Integer TO_AUDIT_ORDER_STATUS = 0;

    /**
     * 待放款订单状态
     */
    private static final Integer TO_LOAN_ORDER_STATUS = 1;

    /**
     * 待还款订单状态
     */
    private static final Integer REPAYMENT_ORDER_STATUS = 2;

    /**
     * 逾期订单状态
     */

    /**
     * 完结订单状态
     */

    /**
     * 延期订单状态
     */

    /**
     * 回退订单状态
     */
    private static final Integer CHECK_ORDER_BACK_STATUS = 99;

    /**
     * 取消(拒绝)订单状态
     */
    private static final Integer CHECK_REFUSE_STATUS = -1;

    @Resource
    private BOrderDetailsService bOrderDetailsService;

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BUserBasicInfoService bUserBasicInfoService;

    @Resource
    private BEmergencyContactInfoService bEmergencyContactInfoService;

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    /**
     * 前端H5客户申请借款
     *
     * @param bOrderDetails
     * @return
     */
    @ApiOperation(value = "前端H5客户申请借款", notes = "前端H5客户申请借款", httpMethod = "POST")
    @RequestMapping(value = "/loanApplication", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData loanApplication(BOrderDetails bOrderDetails) {
        //  查询是否有正在进行中的订单
        if (this.bOrderDetailsService.ifExistOrderByUserId()) {
            return ResponseData.success(0, "申请成功", this.bOrderDetailsService.loanApplication(bOrderDetails));
        } else {
            return ResponseData.success(-1, "订单冲突", null);
        }
    }

    /**
     * 待审核订单主页跳转
     *
     * @return
     */
    @ApiOperation(value = "待审核订单主页跳转", notes = "待审核订单主页跳转", httpMethod = "POST")
    @Permission
    @RequestMapping(value = "/index")
    public String index() {
        return PREFIX + "orderDetails.html";
    }

    /**
     * 待下款订单主页跳转
     */
    @ApiOperation(value = "待下款订单主页跳转", notes = "待下款订单主页跳转", httpMethod = "POST")
    @Permission
    @RequestMapping(value = "/loanIndex")
    public String loanIndex() {
        return PREFIX + "loanIndex.html";
    }

    /**
     * 人工审核页面跳转
     *
     * @return
     */
    @ApiOperation(value = "人工审核页面跳转", notes = "人工审核页面跳转", httpMethod = "POST")
    @Permission
    @RequestMapping(value = "/manualCheckIndex/{userId}")
    public String manualCheckIndex(@PathVariable("userId") Integer userId, Model model) {
        setOrderDetailsInfo(userId, model);
        return PREFIX + "manualCheckIndex.html";
    }

    /**
     * 放款审核页面跳转
     *
     * @return
     */
    @ApiOperation(value = "放款审核页面跳转", notes = "放款审核页面跳转", httpMethod = "POST")
    @Permission
    @RequestMapping(value = "/loanCheckIndex/{userId}")
    public String loanCheckIndex(@PathVariable("userId") Integer userId, Model model) {
        setOrderDetailsInfo(userId, model);
        return PREFIX + "loanCheckIndex.html";
    }

    private Model setOrderDetailsInfo(Integer userId, Model model) {
        /**
         * 用户关联的借款订单信息
         */
        BOrderDetails bOrderDetails = this.bOrderDetailsService.selectBOrderDetailsByUserId(userId);
        if (null == bOrderDetails)
            bOrderDetails = new BOrderDetails();
        model.addAttribute("userId", ShiroKit.getUserNotNull().getId());
        model.addAttribute("userName", ShiroKit.getUserNotNull().getName());
        model.addAttribute("orderDetails", bOrderDetails);

        /**
         * 用户信息
         */
        model.addAttribute("bSysUser", BSysUserWrapper.deSensitization(this.bSysUserService.selectBSysUserByUserId(userId)));

        /**
         * 用户详细个人信息
         */
        model.addAttribute("bUserBasicInfo", this.bUserBasicInfoService.selectBUserBasicInfoByUserId(userId));

        /**
         * 用户联系人信息 b_emergency_contact_info
         */
        BEmergencyContactInfo bEmergencyContactInfo = this.bEmergencyContactInfoService.selectBEmergencyContactInfoByUserId(userId);
        model.addAttribute("bEmergencyContactInfo", BSysUserWrapper.deSensitization(bEmergencyContactInfo));

        /**
         * 用户所关联的中状态信息 b_sys_user_status
         */
        BSysUserStatus bSysUserStatus = this.bSysUserStatusService.selectBSysUserStatusByUserId(userId);
        model.addAttribute("bSysUserStatus", bSysUserStatus);

        return model;
    }

    /**
     * 待审核订单列表
     */
    @ApiOperation(value = "待审核订单列表", notes = "待审核订单列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "查询参数:姓名/账户/手机号", name = "name", dataType = "String"),
            @ApiImplicitParam(value = "时间查询", name = "timeLimit", dataType = "String"),
            @ApiImplicitParam(value = "部门主键", name = "deptId", dataType = "Long")
    })
    @Permission
    @RequestMapping(value = "/toAuditList")
    @ResponseBody
    public Object toAuditList(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String timeLimit,
                              @RequestParam(required = false) Long deptId) {
        logger.info("查询待审核订单列表");
        return list(name, timeLimit, deptId, TO_AUDIT_ORDER_STATUS);
    }

    /**
     * 待放款订单列表
     */
    @ApiOperation(value = "待放款订单列表", notes = "待放款订单列表", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "查询参数:姓名/账户/手机号", name = "name", dataType = "String"),
            @ApiImplicitParam(value = "时间查询", name = "timeLimit", dataType = "String"),
            @ApiImplicitParam(value = "部门主键", name = "deptId", dataType = "Long")
    })
    @Permission
    @RequestMapping(value = "/toLoanList")
    @ResponseBody
    public Object toLoanList(@RequestParam(required = false) String name,
                             @RequestParam(required = false) String timeLimit,
                             @RequestParam(required = false) Long deptId) {
        logger.info("查询待放款订单列表");
        return list(name, timeLimit, deptId, TO_LOAN_ORDER_STATUS);
    }

    private Object list(String name, String timeLimit, Long deptId, Integer Status) {
        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> orderDetails = bOrderDetailsService.selectToAuditList(null, name, beginTime, endTime, deptId, Status);
            Page wrapped = new BOrderDetailsWrapper(orderDetails).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> users = bOrderDetailsService.selectToAuditList(dataScope, name, beginTime, endTime, deptId, Status);
            Page wrapped = new BOrderDetailsWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
    }

    /**
     * 订单审核
     *
     * @param bOrderDetails
     * @return
     */
    @ApiOperation(value = "订单审核", notes = "订单审核", httpMethod = "POST")
    @Permission
    @RequestMapping(value = "/checkOrderDetails")
    @ResponseBody
    public ResponseData checkOrderDetails(BOrderDetails bOrderDetails) {
        this.bOrderDetailsService.checkOrderDetails(bOrderDetails);
        return ResponseData.success(0, "审核完成", null);
    }
}
