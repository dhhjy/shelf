package com.quick.shelf.modular.creditPort.quickMoney;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 快钱常用方法定义类
 */
public class QuickMoneyConstartMethod {
    /** log */
    private static final Logger logger = LoggerFactory.getLogger(QuickMoneyConstartMethod.class);

    /**
     * 生产环境
     */
    private static final String DEV_URL = "https://mas.99bill.com/cnp/getDynNum";

    /**
     * 测试环境
     */
    private static final String TEST_URL = "https://sandbox.99bill.com:9445/cnp/getDynNum";

    /**
     * 版本号 (必填)
     */
    private static final String VERSION = "1.0";

    /**
     * 商户号 （必填）
     */
    private static final String MERCHANID = "";

    /**
     * 商户自定义标识，一笔交易的订单号
     */
    public static final String EXTERNAL_REF_NUMBER = "";

    /**
     * 证件类型 0 身份证
     */
    public static  final String idType = "0";

    /**
     * 持卡人姓名
     */
    public static final String CARD_HOLDER_NAME = "";

    /**
     * 持卡人身 份证号
     */
    public static final String CARD_HOLDER_ID = "";

    /**
     * 位数字，最后一位为校验位，最长为19位数字。
     * 认证支付首次必填。
     */
    public static final String PAN = "";
}
