package com.quick.shelf.modular.business.controller;

import com.quick.shelf.modular.business.service.BBankCardInfoService;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.QuickMoneySignEntity;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.QuickMoneySignVerifyEntity;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Api(value = "银行卡信息控制层", tags = "BBankCardInfoController")
@Controller
@RequestMapping(value = "/bankInfo")
public class BBankCardInfoController {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BBankCardInfoController.class);

    @Resource
    private BBankCardInfoService bBankCardInfoService;

    /**
     * 银行卡签约申请
     *
     * @return
     */
    @RequestMapping(value = "/bankAuth/{userId}")
    @ResponseBody
    public String bankAuth(@PathVariable("userId") Integer userId, QuickMoneySignEntity quickMoneySignEntity) {
        logger.info("主键：{} 的用户正在申请银行卡签约", userId);
        String message = this.bBankCardInfoService.bankAuth(userId, quickMoneySignEntity);
        return message.equals("交易成功") ? "00" : message;
    }

    /**
     * 银行卡验证码
     */
    @RequestMapping(value = "/bankAuthVerify/{userId}")
    @ResponseBody
    public String bankAuthVerify(@PathVariable("userId") Integer userId, QuickMoneySignVerifyEntity quickMoneySignVerifyEntity) {
        logger.info("主键：{} 的用户正在进行银行卡验证码校验", userId);
        return this.bBankCardInfoService.bankAuthVerify(userId, quickMoneySignVerifyEntity);
    }
}
