package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 用户还款账单
 */
@Data
@TableName("b_staging_list")
public class BStagingList implements Serializable {

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
     * 关联部门
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 关联订单号
     */
    @TableField("p_order_number")
    private String pOrderNumber;

    /**
     * 每期应还
     */
    @TableField("every_day_refund")
    private BigDecimal everyDayRefund;

    /**
     * 总共应还
     */
    @TableField("count_refund")
    private BigDecimal countRefund;

    /**
     * 剩余应还
     */
    @TableField("surplus_refund")
    private BigDecimal surplusRefund;

    /**
     * 订单的状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 预计还款日期
     */
    @TableField("predict_repayment_time")
    private Date predictRepaymentTime;

    /**
     * 还款日期
     */
    @TableField("repayment_time")
    private Date repaymentTime;

    /**
     * 收款人
     * 默认为系统自动收款，
     * 手动回款时，将操作人的主键记录下来
     */
    @TableField("payee")
    private String payee;
}
