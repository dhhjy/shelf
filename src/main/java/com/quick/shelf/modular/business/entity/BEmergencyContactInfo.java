package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户紧急联系人关联表
 */
@Data
@TableName("b_emergency_contact_info")
public class BEmergencyContactInfo implements Serializable {

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
     * 第一联系人姓名
     */
    @TableField("relative_name1")
    private String relativeName1;

    /**
     * 第一联系人关系
     */
    @TableField("relative1")
    private String relative1;

    /**
     * 第一联系人手机号
     */
    @TableField("relative_phone1")
    private String relativePhone1;

    /**
     * 第二联系人姓名
     */
    @TableField("relative_name2")
    private String relativeName2;

    /**
     * 第二联系人关系
     */
    @TableField("relative2")
    private String relative2;

    /**
     * 第二联系人手机号
     */
    @TableField("relative_phone2")
    private String relativePhone2;

    /**
     * 第三联系人姓名
     */
    @TableField("relative_name3")
    private String relativeName3;

    /**
     * 第三联系人关系
     */
    @TableField("social")
    private String social;

    /**
     * 第三联系人手机号
     */
    @TableField("social_phone")
    private String socialPhone;

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
}
