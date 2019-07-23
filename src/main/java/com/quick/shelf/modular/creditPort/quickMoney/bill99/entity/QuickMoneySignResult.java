package com.quick.shelf.modular.creditPort.quickMoney.bill99.entity;

import lombok.Data;

/**
 * 快钱签约时，返回属性的对象封装类
 */
@Data
public class QuickMoneySignResult {

    /**
     * 版本号
     * 接口版本号标识当前消息对应的接口版本，1.0默认值
     */
    private String version;

    /**
     * 终端号
     * 快钱公司分配给商户的8位唯一标识号
     */
    private String terminalId;

    /**
     * 商户号
     * 快钱公司分配给商户的15位唯一标识号
     */
    private String merchantId;

    /**
     * 客户号
     * 用户主键ID
     * <p>
     * 商户自己定义，每个用户唯一，再次消费时与payToken作为匹配条件，获取相应卡号进行消费
     */
    private String customerId;

    /**
     * 外部跟踪号  订单编号
     * 商户自定义标识一笔交易的订单号
     */
    private String externalRefNumber;

    /**
     * 缩略卡号
     * 全卡号的前6后4位
     */
    private String storablePan;

    /**
     * 令牌信息
     * 签约接口的绑卡标识，签约短信验证时需传入
     */
    private String token;

    /**
     * 对应答码的文字描述信息
     */
    private String responseTextMessage;

    /**
     * 应答码
     * 系统的应答码，表示对其所收到 的交易的处理情况。详见错误码表
     */
    private String responseCode;
}
