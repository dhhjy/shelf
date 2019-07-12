package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联的新颜认证信息表
 */
@Data
@TableName("b_xinyan_data")
public class BXinYanData implements Serializable {

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
     * 数据类型（0-原始数据，1-报告(页面数据)）
     */
    @TableField("data_type")
    private Integer dataType;

    /**
     * 类型(类型为接口服务标识)
     */
    @TableField("type")
    private String type;        //类型(类型为接口服务标识)

    /**
     * 类型名称
     */
    @TableField("type_text")
    private String typeText;   //类型名称

    /**
     * 数据
     */
    @TableField("data_value")
    private String dataValue;  //数据

    /**
     * 任务查询凭证
     */
    @TableField("token")
    private String token;       //任务查询凭证

    /**
     * 备用字段
     */
    @TableField("remark")
    private String remark;      //备用字段

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;   //创建时间
}
