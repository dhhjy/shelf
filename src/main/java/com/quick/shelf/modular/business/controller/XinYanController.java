package com.quick.shelf.modular.business.controller;

import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.modular.business.service.BXinYanDataService;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * 新颜征信控制层
 *
 * @author zcn
 * @date 2019/07/12
 */
@Api(value = "新颜征信控制层")
@Controller
@RequestMapping("/xinYan")
public class XinYanController extends BaseController {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BSysUserController.class);

    private static final String PREFIX = "/modular/business/xinYan/";

    @Resource
    private BXinYanDataService bXinYanDataService;

    @RequestMapping(value = "/getReDerData/{userId}")
    public String getReDerData(@PathVariable Integer userId, Model model) {
//        this.bXinYanDataService.selectBXinYanDataByUserId(userId,)
        System.out.println(userId);
        return PREFIX + "reDerData.html";
    }
}
