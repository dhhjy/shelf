package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName 立木征信实体类
 *
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 20:20
 * @Version 1.0
 */
@Data
@TableName("b_limu_data")
public class BLiMuData implements Serializable {

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
     * 数据类型: 0-原始数据 1-报告页面(html)
     */
    @TableField("data_type")
    private String dataType;

    /**
     * 接口类型（淘宝、运营商、升级、设备）
     */
    @TableField("type")
    private String type;

    /**
     * 数据类型描述
     */
    @TableField("type_text")
    private String typeText;

    /**
     * 用户数据
     */
    @TableField("data_value")
    private String dataValue;

    /**
     * 请求token
     */
    @TableField("token")
    private String token;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}
