package com.quick.shelf.modular.business.controller;

import cn.stylefeng.roses.core.datascope.DataScope;
import cn.stylefeng.roses.core.util.ToolUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.core.common.annotion.BussinessLog;
import com.quick.shelf.core.common.annotion.Permission;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BLiMuData;
import com.quick.shelf.modular.business.service.BLiMuService;
import com.quick.shelf.modular.business.warpper.BLiMuWrapper;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.creditPort.liMu.LiMuConstantEnum;
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

/**
 * @ClassName 立木征信控制层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 19:33
 * @Version 1.0
 */
@Api(value = "BLiMuController", tags = "BLiMuController")
@Controller
@RequestMapping(value = "/liMu")
public class BLiMuController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BLiMuController.class);

    private static final String PATH = "/modular/business/liMu/";

    @Resource
    private BLiMuService bLiMuService;

    /**
     * 立木征信报告跳转页面
     *
     * @return
     */
    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return PATH + "index.html";
    }

    /**
     * 查询立木征信列表
     *
     * @author zcn
     * @Date 2018/12/24 22:43
     */
    @ApiOperation(value = "查询立木征信列表", notes = "查询立木征信列表", httpMethod = "POST")
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

        logger.info("查询立木征信列表");

        //拼接查询条件
        String beginTime = "";
        String endTime = "";

        if (ToolUtil.isNotEmpty(timeLimit)) {
            String[] split = timeLimit.split(" - ");
            beginTime = split[0];
            endTime = split[1];
        }

        if (ShiroKit.isAdmin()) {
            Page<Map<String, Object>> users = bLiMuService.selectBLiMuDatas(null, name, beginTime, endTime, deptId);
            Page wrapped = new BLiMuWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        } else {
            DataScope dataScope = new DataScope(ShiroKit.getDeptDataScope());
            Page<Map<String, Object>> users = bLiMuService.selectBLiMuDatas(dataScope, name, beginTime, endTime, deptId);
            Page wrapped = new BLiMuWrapper(users).wrap();
            return LayuiPageFactory.createPageInfo(wrapped);
        }
    }

    /**
     * 获取立木运营商报告
     * @return
     */
    @BussinessLog(value = "获取立木运营商报告")
    @ApiOperation(value = "获取立木运营商报告",notes = "获取立木运营商报告",httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name="userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/getMobileReport/{userId}")
    public String getMobileReport(@PathVariable Integer userId, Model model){
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_YYS.getApiName(), BusinessConst.PAGE_DATA.toString());
        model.addAttribute("mobileReport",BLiMuWrapper.replaceHtmlUrl(data.getDataValue()));
        return PATH + "liMu_mobileReport.html";
    }

    /**
     * 获取淘宝认证报告
     * @return
     */
    @BussinessLog(value = "获取淘宝认证报告")
    @ApiOperation(value = "获取淘宝认证报告",notes = "获取淘宝认证报告",httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name="userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/getTaoBaoReport/{userId}")
    public String getTaoBaoReport(@PathVariable Integer userId, Model model){
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_TB.getApiName(), BusinessConst.PAGE_DATA.toString());
        model.addAttribute("taoBaoReport",BLiMuWrapper.replaceHtmlUrl(data.getDataValue()));
        return PATH + "liMu_taoBaoReport.html";
    }

    /**
     * 获取立方升级报告
     * @return
     */
    @BussinessLog(value = "获取立方升级报告")
    @ApiOperation(value = "获取立方升级报告",notes = "获取立方升级报告",httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name="userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/getLiFangUpgradeCheck/{userId}")
    public String getLiFangUpgradeCheck(@PathVariable Integer userId, Model model){
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_LFSJ.getApiName(), BusinessConst.PAGE_DATA.toString());
        model.addAttribute("liFangUpgradeCheck",BLiMuWrapper.replaceHtmlUrl(data.getDataValue()));
        return PATH + "liMu_liFangUpgradeCheck.html";
    }

    /**
     * 获取立木设备指纹
     * @return
     */
    @BussinessLog(value = "获取立木设备指纹")
    @ApiOperation(value = "获取立木设备指纹",notes = "获取立木设备指纹",httpMethod = "GET")
    @ApiImplicitParam(value = "用户主键", name="userId", required = true, dataType = "Integer")
    @RequestMapping(value = "/getMachineCheck/{userId}")
    public String getMachineCheck(@PathVariable Integer userId, Model model){
        BLiMuData data = this.bLiMuService.selectBLiMuDataByUserId(userId, LiMuConstantEnum.API_NAME_SBZW.getApiName(), BusinessConst.ORIGINAL_DATA.toString());
        model.addAttribute("machineCheck",BLiMuWrapper.replaceHtmlUrl(data.getDataValue()));
        return PATH + "liMu_machineCheck.html";
    }
}
