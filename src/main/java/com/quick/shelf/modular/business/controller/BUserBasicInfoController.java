package com.quick.shelf.modular.business.controller;

import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.response.ResponseData;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BEmergencyContactInfo;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BUserBasicInfo;
import com.quick.shelf.modular.business.service.BUserBasicInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName 客户基本信息控制层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/21 17:51
 * @Version 1.0
 */
@Api(value = "客户基本信息控制层", tags = "BUserBasicInfoController")
@Controller
@RequestMapping(value = "/basicInfo")
public class BUserBasicInfoController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BUserBasicInfoController.class);

    @Resource
    private BUserBasicInfoService bUserBasicInfoService;

    /**
     * 用户基本信息新增 或 更新
     *
     * @param bUserBasicInfo        用户基本信息对象
     * @param bEmergencyContactInfo 用户紧急联系人对象
     */
    @ApiOperation(value = "用户基本信息新增/更新", notes = "用户基本信息新增/更新", httpMethod = "POST")
    @RequestMapping(value = "/addOrUpt", method = RequestMethod.POST)
    @ResponseBody
    public ResponseData userBasicInfoDoAddAndUpt(BSysUser bSysUser, BUserBasicInfo bUserBasicInfo, BEmergencyContactInfo bEmergencyContactInfo) {
        Integer userId = ShiroKit.getUserNotNull().getId().intValue();
        logger.info("主键:{} 的用户正在进行用户基本信息的认证！", userId);
        this.bUserBasicInfoService.userBasicInfoDoAddAndUpt(userId, bSysUser, bUserBasicInfo, bEmergencyContactInfo);
        return ResponseData.success(0, "认证成功", null);
    }
}
