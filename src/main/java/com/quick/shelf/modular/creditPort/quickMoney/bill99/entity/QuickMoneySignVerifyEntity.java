package com.quick.shelf.modular.creditPort.quickMoney.bill99.entity;

import com.quick.shelf.modular.creditPort.quickMoney.QuickMoneyConstartMethod;
import lombok.Data;

/**
 * 签约申请成功后，验证码发送的实体类
 */
@Data
public class QuickMoneySignVerifyEntity {
    /**
     * 客户号 (必填)
     * 用户在商户平台的编号，商户自 己定义，每个用户唯一，再次消费时与payToken作为匹配条件，获取相应卡号进行消费
     */
    private String customerId;

    /**
     * 交易订单号 (必填)
     */
    private String externalRefNumber;

    /**
     * 卡号 (必填)
     * 银行卡号。多数信用卡为16位数 字，最后一位为校验位，最长为19位数字。
     */
    private String pan;

    /**
     * 验证码(必填)
     */
    private String validCode;

    /**
     * 令牌信息(必填)
     * 约申请后接口返回的令牌信息，消费或绑卡时，需传
     */
    private String token;

    /**
     * 持卡人手机号 (必填)
     * 客户卡在银行预留的手机号
     */
    private String phoneNO;

    /**
     * 银行类型 例：ICBC
     */
    private String bankType;

    /**
     * 银行类型中文名称 例：工商银行
     */
    private String bankName;

    /**
     * 开户地址
     */
    private String bankArea;

    /**
     * 签约申请需要的XML格式数据
     */
    private String xml;

    public String getXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\">" +
                "<version>" + QuickMoneyConstartMethod.VERSION + "</version>" +
                "<indAuthDynVerifyContent>" +
                "<merchantId>" + QuickMoneyConstartMethod.MERCHANID + "</merchantId>" +
                "<terminalId>" + QuickMoneyConstartMethod.TERMINAL_ID + "</terminalId>" +
                "<customerId>" + this.customerId + "</customerId>" +
                "<externalRefNumber>" + this.externalRefNumber + "</externalRefNumber>" +
                "<pan>" + this.pan + "</pan>" +
                "<validCode>" + this.validCode + "</validCode>" +
                "<token>" + this.token + "</token>" +
                "<phoneNO>" + this.phoneNO + "</phoneNO>" +
                "</indAuthDynVerifyContent>" +
                "</MasMessage>";
    }
}
