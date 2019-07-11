package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联的银行卡信息表
 * 银行卡可以关联多张 一对多的关系
 * 即一个用户多张银行卡
 */
@Data
@TableName("b_bank_card_info")
public class BBankCardInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联用户主键
     */
    @TableField("user_id")
    private Integer userId;

    /**
     * 开户银行类型
     */
    @TableField("bank_type")
    private String bankType;

    /**
     * 开户银行名称
     */
    @TableField("bank_name")
    private String bankName;

    /**
     * 开户省市区
     */
    @TableField("bank_area")
    private String bankArea;

    /**
     * 预留号码
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 银行卡号
     */
    @TableField("card_number")
    private String cardNumber;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private Date updateTime;

    /**
     * 银行卡是否已经签约:0-未签约 1-已签约 2-已授权
     */
    @TableField("sign_status")
    private Integer signStatus;

    /**
     * 银行卡收款协议号
     */
    @TableField("no_agree")
    private String noAgree;
}
