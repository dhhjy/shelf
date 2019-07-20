package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName BSysUser
 * @Description TODO
 * @Author ken
 * @Date 2019/7/10 15:35
 * @Version 1.0
 */
@Data
@TableName("b_sys_user")
public class BSysUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "user_id", type = IdType.AUTO)
    private Integer userId;

    /**
     * 账号
     */
    @TableField("user_account")
    private String userAccount;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 密码
     */
    @TableField("password")
    private String password;

    /**
     * 密码盐
     */
    @TableField("salt")
    private String salt;

    /**
     * 电话号码
     */
    @TableField("phone_number")
    private String phoneNumber;

    /**
     * 性别
     */
    @TableField("sex")
    private String sex;

    /**
     * 身份证
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 状态
     */
    @TableField("status")
    private String status;

    /**
     * 角色主键
     */
    @TableField("role_id")
    private String roleId;

    /**
     * 部门主键
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 第一推荐人
     */
    @TableField("first_referrer")
    private String firstReferrer;

    /**
     * 间接推荐人
     */
    @TableField("second_referrer")
    private String secondReferrer;

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
     * 更新人
     */
    @TableField("update_user")
    private Long updateUser;

    /**
     * 乐观锁
     */
    @TableField("version")
    private Integer version;
}
