package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName 接口统计类
 * @Description TODO
 * @Author ken
 * @Date 2019/7/14 16:20
 * @Version 1.0
 */
@Data
@TableName("b_port_count")
public class BPortCount implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 部门主键
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 接口类型
     */
    @TableField("type")
    private String type;

    /**
     * 接口类型中文名
     */
    @TableField("type_name")
    private String typeName;

    /**
     * 接口返回的结果
     */
    @TableField("result")
    private String result;

    /**
     * 接口单价
     */
    @TableField("unit_price")
    private Double unitPrice;

    /**
     * 接口调用时间
     */
    @TableField("create_time")
    private Date createTime;
}
