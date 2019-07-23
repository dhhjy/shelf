package com.quick.shelf.modular.creditPort.quickMoney.bill99.entity;

import com.quick.shelf.modular.creditPort.quickMoney.QuickMoneyConstartMethod;
import lombok.Data;

/**
 * 快钱签约申请实体
 */
@Data
public class QuickMoneySignEntity {
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
     * 持卡人姓名
     */
    private String cardHolderName;

    /**
     * 证件类型
     * 默认为 0；0 = 身份证
     */
    private String idType = "0";

    /**
     * 持卡人身份证号
     * 客户卡在银行预留的身份证号，再次交易无需上送
     */
    private String cardHolderId;

    /**
     * 持卡人手机号 (必填)
     * 客户卡在银行预留的手机号
     */
    private String phoneNO;

    /**
     * 签约申请需要的XML格式数据
     */
    private String xml;

    public String getXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
                "<MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\">" +
                "<version>" + QuickMoneyConstartMethod.VERSION + "</version>" +
                "<indAuthContent>" +
                "<merchantId>" + QuickMoneyConstartMethod.MERCHANID + "</merchantId>" +
                "<terminalId>" + QuickMoneyConstartMethod.TERMINAL_ID + "</terminalId>" +
                "<customerId>" + this.customerId + "</customerId>" +
                "<externalRefNumber>" + this.externalRefNumber + "</externalRefNumber>" +
                "<pan>" + this.pan + "</pan>" +
                "<cardHolderName>" + this.cardHolderName + "</cardHolderName>" +
                "<idType>" + this.idType + "</idType>" + "<cardHolderId>" +
                this.cardHolderId + "</cardHolderId>" +
                "<phoneNO>" + this.phoneNO + "</phoneNO>" +
                "<bindType>0</bindType>" +
                "</indAuthContent>" +
                "</MasMessage>";
    }
}
