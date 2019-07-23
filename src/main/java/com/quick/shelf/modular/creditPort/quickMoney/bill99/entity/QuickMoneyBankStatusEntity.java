package com.quick.shelf.modular.creditPort.quickMoney.bill99.entity;

import lombok.Data;

@Data
public class QuickMoneyBankStatusEntity {
    private String cardNo;

    public String xml;

    public String getXml() {
        return "<?xml version=\"1.0\"encoding=\"utf-8\" standalone=\"yes\"?>" +
                "<MasMessage xmlns=\"http://www.99bill.com/mas_cnp_merchant_interface\">" +
                "<version>1.0</version>" +
                "<QryCardContent>" +
                "<txnType>PUR</txnType>" +
                "<cardNo>" + this.cardNo + "</cardNo>" +
                "</QryCardContent>" +
                "</MasMessage>";
    }
}
