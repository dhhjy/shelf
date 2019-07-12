package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户关联的GPS定位信息
 * GPS 定位信息为一对多
 */
@Data
@TableName("b_location")
public class BLocation implements Serializable {

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
     * 纬度
     */
    @TableField("latitude")
    private String latitude;

    /**
     * 经度
     */
    @TableField("longitude")
    private String longitude;

    /**
     * 详细街道位置
     */
    @TableField("addr_str")
    private String addrStr;//详细街道信息

    /**
     * 详细位置
     */
    @TableField("description")
    private String description;

    /**
     * 登录设备（手机型号即品牌+型号+系统版本）
     */
    @TableField("device_info")
    private String deviceInfo;

    /**
     * GPS定位时间
     */
    @TableField("create_time")
    private Date createTime;
}