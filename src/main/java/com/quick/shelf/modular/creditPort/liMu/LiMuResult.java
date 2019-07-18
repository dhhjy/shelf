package com.quick.shelf.modular.creditPort.liMu;

import lombok.Data;
import lombok.ToString;

/**
 * @ClassName 立木回调通知参数封装类
 * @Description TODO
 * @Author ken
 * @Date 2019/7/18 14:56
 * @Version 1.0
 */
@Data
@ToString
public class LiMuResult {
    /**
     * 用户标识
     */
    private String uid;
    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 状态码:0000 成功，其他失败
     */
    private String code;
    /**
     * 状态描述
     */
    private String msg;
    /**
     * 查询唯一标
     */
    private String token;
}
