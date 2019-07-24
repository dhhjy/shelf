package com.quick.shelf.modular.creditPort.xinYan;

/**
 * 新颜接口定义
 */
public enum XinYanConstantEnum {
    API_NAME_TB("淘宝", "taobaoweb"),
    API_NAME_ZFB("支付宝", "zhifubao"),
    API_NAME_YYS("运营商", "carrier"),
    API_NAME_ZHIMAFEN("芝麻信用查验", "zhimafen"),
    API_NAME_LD("全景雷达", "radar");

    private String serverName;  //服务名称
    private String apiName;     //服务标识

    XinYanConstantEnum(String serverName, String apiName) {
        this.serverName = serverName;
        this.apiName = apiName;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getApiName() {
        return this.apiName;
    }
}