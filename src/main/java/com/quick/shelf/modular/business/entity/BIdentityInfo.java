package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联的认证过的身份证信息表
 */
@Data
@TableName("b_identity_info")
public class BIdentityInfo implements Serializable {

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
     * 真实姓名
     */
    @TableField("real_name")
    private String realName;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 生日
     */
    @TableField("birth")
    private String birth;

    /**
     * 身份证地址
     */
    @TableField("address")
    private String address;

    /**
     * 身份证号码
     */
    @TableField("identity_number")
    private String identityNumber;

    /**
     * 签发机关
     */
    @TableField("institutions")
    private String institutions;

    /**
     * 有效期限
     */
    @TableField("effective_date")
    private String effectiveDate;

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
