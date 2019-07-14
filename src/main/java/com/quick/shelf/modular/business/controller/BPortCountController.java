package com.quick.shelf.modular.business.controller;

import com.quick.shelf.core.base.BaseController;
import com.quick.shelf.modular.business.service.BPortCountService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * @ClassName 接口统计控制层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/14 17:35
 * @Version 1.0
 */
@Controller
@RequestMapping(value = "/bPortCount")
public class BPortCountController extends BaseController {

    @Resource
    private BPortCountService bPortCountService;

    /**
     * 获取接口单日总数与全部的数量
     */
    @RequestMapping(value = "/getPortNum")
    @ResponseBody
    public Object getPortNum() {
        return this.bPortCountService.getPortNum();
    }

    /**
     * 获取接口类的 图表类型的数据
     * 获取7天内的数据
     */
    @RequestMapping(value = "/getPortChart")
    @ResponseBody
    public Object getPortChart() {
        return this.bPortCountService.getPortChart();
    }
}
