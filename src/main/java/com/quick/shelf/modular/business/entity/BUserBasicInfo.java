package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联的详细信息表
 */
@Data
@TableName("b_user_basic_info")
public class BUserBasicInfo implements Serializable {

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
     * 学历
     */
    @TableField("education")
    private String education;

    /**
     * 工作
     */
    @TableField("job")
    private String job;

    /**
     * 月收入
     */
    @TableField("income")
    private String income;

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
     * 单位名称
     */
    @TableField("company")
    private String company;

    /**
     * 单位详细地址
     */
    @TableField("company_address")
    private String companyAddress;

    /**
     * 单位所在城市
     */
    @TableField("company_city")
    private String companyCity;

    /**
     * 单位电话
     */
    @TableField("company_phone")
    private String companyPhone;

    /**
     * 婚姻状况
     */
    @TableField("marriage")
    private String marriage;

    /**
     * 子女数
     */
    @TableField("children")
    private String children;

    /**
     * 详细地址（精确到门牌号）
     */
    @TableField("address")
    private String address;

    /**
     * 居住城市
     */
    @TableField("address_city")
    private String addressCity;

    /**
     * 手机服务密码
     */
    @TableField("phone_service_number")
    private String phoneServiceNumber;

    /**
     * 邮箱号码
     */
    @TableField("mail")
    private String mail;
}
