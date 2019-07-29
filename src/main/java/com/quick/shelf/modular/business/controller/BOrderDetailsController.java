package com.quick.shelf.modular.business.controller;

import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.response.ResponseData;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import com.quick.shelf.modular.business.service.BOrderDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Api(value = "借款订单控制层", tags = "BOrderDetailsController")
@Controller
@RequestMapping(value = "/orderDetails")
public class BOrderDetailsController extends BaseController {

    /**
     * log
     */
    public static final Logger logger = LoggerFactory.getLogger(BOrderDetailsController.class);

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
}
