package com.quick.shelf.modular.creditPort.liMu;

/**
 * @ClassName 立木接口定义
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 23:14
 * @Version 1.0
 */
public enum LiMuConstantEnum {
    API_NAME_TB("淘宝报告", "taobaoReport"),
    API_NAME_YYS("运营商报告", "mobileReport"),
    API_NAME_LFSJ("立方升级", "lifangupgradecheck"),
    API_NAME_SBZW("设备指纹", "fingerprint"),
    API_NAME_JS("机审报告", "machinecheck");

    private String serverName;  //服务名称
    private String apiName;     //服务标识

    LiMuConstantEnum(String serverName, String apiName) {
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
