package com.quick.shelf.modular.creditPort.quickMoney.bill99.entity;

import lombok.Data;

/**
 * @project mgwCore
 * @description:发送参数对象
 * @author cen
 * @create_time:Jun 22, 2009
 * @modify_time:Jun 22, 2009
 */
@Data
public class MerchantInfo {
    /**
     * 商户号
     */
    private String merchantId;
    /**
     * 登录密码
     */
    private String password;
    /**
     * tr1发送xml数据
     */
    private String xml;
    /**
     * 发送url
     */
    private String url;
    /**
     * 证书密码
     */
    private String certPass;
    /**
     * 证书路径
     */
    private String certPath;
    /**
     * 超时时间(单位秒)
     */
    private int timeOut;

    //提交地址
    private String domainName;
    //提交端口
    private String sslPort;
}
