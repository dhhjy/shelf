package com.quick.shelf.modular.creditPort.quickMoney;

import com.quick.shelf.modular.creditPort.quickMoney.bill99.Post;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.QuickMoneyBankStatusEntity;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.QuickMoneySignEntity;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.QuickMoneySignVerifyEntity;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.TransInfo;

import java.util.HashMap;

/**
 * 快钱常用方法定义类
 */
public class QuickMoneyConstartMethod {
    /**
     * 生产环境
     */
    private static final String DEV_URL = "https://mas.99bill.com/cnp/";

    /**
     * 测试环境
     */
    public static final String TEST_URL = "https://sandbox.99bill.com:9445/cnp/";

    /**
     * 版本号 (必填)
     */
    public static final String VERSION = "1.0";

    /**
     * 商户号 （必填）
     */
    public static final String MERCHANID = "104110045112012";

    /**
     * 终端号 (必填)
     */
    public static final String TERMINAL_ID = "00002012";


    /**
     * JKS路径
     */
    public static final String JKS_FILE = "D:\\TS\\10411004511201290.jks";

    /**
     * 签约申请接口
     * 用户申请签约时的路径
     */
    private static final String AUTH_PATH = "ind_auth";

    /**
     * 签约短信验证接口
     * 用户申请签约时的路径
     */
    private static final String AUTH_VERIFY_PATH = "ind_auth_verify";

    /**
     * 银行卡信息查询接口
     */
    private static final String QUERY_CARD_INFO = "query_cardinfo";

    /**
     * 成功标识
     */
    public static final String SUCCESS_CODE = "00";

    /**
     * 发送用户银行卡的签约申请
     *
     * @return
     */
    public static HashMap bankAuth(QuickMoneySignEntity quickMoneySignEntity) {
        // 设置订单编号
        TransInfo transInfo = new TransInfo();
        //设置手机动态鉴权节点
        transInfo.setRecordeText_1("indAuthContent");
        transInfo.setRecordeText_2("ErrorMsgContent");

        //TR2接收的数据
        HashMap respXml = null;
        try {
            respXml = Post.sendPost(QuickMoneyConstartMethod.TEST_URL + QuickMoneyConstartMethod.AUTH_PATH, quickMoneySignEntity.getXml(), transInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respXml;
    }

    /**
     * 发送用户签约的验证码到快钱
     */
    public static HashMap bankAuthVerify(QuickMoneySignVerifyEntity quickMoneySignVerifyEntity) {
        TransInfo transInfo = new TransInfo();
        //设置手机动态鉴权节点
        transInfo.setRecordeText_1("indAuthDynVerifyContent");
        transInfo.setRecordeText_2("ErrorMsgContent");
        System.out.println(quickMoneySignVerifyEntity.getXml());
        HashMap respXml = null;
        try {
            respXml = Post.sendPost(QuickMoneyConstartMethod.TEST_URL + QuickMoneyConstartMethod.AUTH_VERIFY_PATH, quickMoneySignVerifyEntity.getXml(), transInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respXml;
    }

    /**
     * 查询银行卡信息
     */
    public static HashMap bankStatusQuery(String bankNo)
    {
        QuickMoneyBankStatusEntity quickMoneyBankStatusEntity = new QuickMoneyBankStatusEntity();
        quickMoneyBankStatusEntity.setCardNo(bankNo);
        TransInfo transInfo = new TransInfo();
        //设置手机动态鉴权节点
        transInfo.setRecordeText_1("QryCardContent");
        transInfo.setRecordeText_2("ErrorMsgContent");
        System.out.println("xml = " + quickMoneyBankStatusEntity.getXml());
        HashMap respXml = null;
        try {
            respXml = Post.sendPost(QuickMoneyConstartMethod.TEST_URL + QuickMoneyConstartMethod.QUERY_CARD_INFO, quickMoneyBankStatusEntity.getXml(), transInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return respXml;
    }
}
