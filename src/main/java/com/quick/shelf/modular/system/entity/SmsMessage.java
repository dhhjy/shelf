package com.quick.shelf.modular.system.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 短信内容实体类、也会用来做统计表格
 */
@Data
@TableName("sms_message")
public class SmsMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户手机号码
     */
    @TableField(value = "phone")
    private String phone;

    /**
     * 短信类型(1验证、2通知)
     * 默认 1 验证短信
     */
    @TableField(value = "sms_type")
    private Integer smsType;

    /**
     * 短信单价
     */
    @TableField(value = "unit_price")
    private Double unitPrice;

    /**
     * 短信发送内容
     */
    @TableField(value = "content")
    private String content;

    /**
     * 短信平台
     */
    @TableField(value = "operator")
    private Integer operator;

    /**
     * 短信模板编号
     */
    @TableField(value = "template_code")
    private String templateCode;

    /**
     * 用户注册的公司
     */
    @TableField(value = "dept_id")
    private Long deptId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}
