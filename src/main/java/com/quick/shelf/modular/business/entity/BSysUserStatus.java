package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联的认证状态信息表
 * 主要为各种的认证状态
 * 'b_sys_user_status'
 */
@Data
@TableName("b_sys_user_status")
public class BSysUserStatus implements Serializable {

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
     * 身份证认证状态
     */
    @TableField("identity_status")
    private String identityStatus;

    /**
     * 个人信息认证状态
     */
    @TableField("user_basic_status")
    private String userBasicStatus;

    /**
     * 银行卡认证状态
     */
    @TableField("bank_info_status")
    private String bankInfoStatus;

    /**
     * 紧急联系人认证状态
     */
    @TableField("contact_status")
    private String contactStatus;

    /**
     * 短信认证状态
     */
    @TableField("sms_status")
    private String smsStatus;

    /**
     * 指纹设备验证
     */
    @TableField("device_info_status")
    private String deviceInfoStatus;

    /**
     * 立木运营商信用报告验证
     */
    @TableField("limu_mobile_report_status")
    private String limuMobileReportStatus;

    /**
     * 立木淘宝验证
     */
    @TableField("limu_taobao_report_status")
    private String limuTaobaoReportStatus;

    /**
     * 立木立方升级验证
     */
    @TableField("limu_lifang_upgrade_check_status")
    private String limuLifangUpgradeCheckStatus;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 创建时间
     */
    @TableField("update_time")
    private Date updateTime;
}
