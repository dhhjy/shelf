package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.response.ResponseData;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import com.quick.shelf.modular.business.service.BOrderDetailsService;
import com.quick.shelf.modular.business.warpper.BOrderDetailsWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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

    public static final String PATHFIX = "/modular/business/orderDetails/";

    @Resource
    private BOrderDetailsService bOrderDetailsService;


    /**
     * 前端H5客户申请借款
     *
     * @param bOrderDetails
     * @return
     */
    @ApiOperation(value = "前端H5客户申请借款", notes = "前端H5客户申请借款", httpMethod = "POST")
    @RequestMapping(value = "/loanApplication", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData loanApplication( BOrderDetails bOrderDetails) {
        //  查询是否有正在进行中的订单
        if(this.bOrderDetailsService.ifExistOrderByUserId()){
            return ResponseData.success(0, "申请成功", this.bOrderDetailsService.loanApplication(bOrderDetails));
        }else{
            return ResponseData.success(-1, "订单冲突", null);
        }
    }

    @RequestMapping(value = "/index")
    public String index(){
        return PATHFIX + "orderDetails.html";
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
    @RequestMapping(value = "/toAuditList")
    @ResponseBody
    public Object toAuditList(@RequestParam(required = false) String name,
                              @RequestParam(required = false) String timeLimit,
                              @RequestParam(required = false) Long deptId){
        logger.info("查询待审核订单列表");

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> orderDetails = bOrderDetailsService.selectToAuditList(null, name, beginTime, endTime, deptId);
            Page wrapped = new BOrderDetailsWrapper(orderDetails).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> users = bOrderDetailsService.selectToAuditList(dataScope, name, beginTime, endTime, deptId);
            Page wrapped = new BOrderDetailsWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
    }
}
