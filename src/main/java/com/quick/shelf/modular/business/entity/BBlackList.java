package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户黑名单关联表
 */
@Data
@TableName("b_black_list")
public class BBlackList implements Serializable {

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
     * 拉黑用户账号
     */
    @TableField("user_account")
    private String userAccount;

    /**
     * 拉黑用户姓名
     */
    @TableField("name")
    private String name;

    /**
     * 所属公司
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 拉黑原因描述
     */
    @TableField("description")
    private String description;

    /**
     * 拉黑操作人关联主键ID
     */
    @TableField("operate_id")
    private Long operateId;

    /**
     * 拉黑操作人姓名
     */
    @TableField("operate")
    private String operate;

    /**
     * 拉黑时间
     */
    @TableField("black_time")
    private Date blackTime;
}
