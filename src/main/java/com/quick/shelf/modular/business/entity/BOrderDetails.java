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
 * 用户关联的借款订单信息表
 * b_order_details
 */
@Data
@TableName("b_order_details")
public class BOrderDetails implements Serializable {

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
     * 产品编号
     * 1-秒秒贷 2-苹果ID贷 3-白条
     */
    @TableField("product_code")
    private Integer productCode;

    /**
     * 订单号
     */
    @TableField("order_number")
    private String orderNumber;

    /**
     * 还款方式
     * 1-每日本息 2-每日利息+服务费 3-到期全额
     */
    @TableField("mode_of_repayment")
    private Integer modeOfRepayment;

    /**
     * 申请借款的金额
     */
    @TableField("amount")
    private BigDecimal amount;

    /**
     * 申请借款的时长
     */
    @TableField("debt_duration")
    private Integer debtDuration;

    /**
     * 申请借款时的利息
     */
    @TableField("accrual")
    private BigDecimal accrual;

    /**
     * 申请借款的服务费用
     */
    @TableField("service_charge")
    private BigDecimal serviceCharge;

    /**
     * 申请借款的年华利率
     */
    @TableField("annual_interest_rate")
    private BigDecimal annualInterestRate;

    /**
     * 实际的放款金额
     * 因为会在申请的金额里面扣除手续费 认证费等所以
     * 申请的金额不等于实际的下款金额
     */
    @TableField("actual_amount")
    private BigDecimal actualAmount;

    /**
     * 当用户的还款方式为每日还款时，此处会有数据
     */
    @TableField("every_day_refund")
    private BigDecimal everyDayRefund;

    /**
     * 总共的应还金额
     */
    @TableField("count_refund")
    private BigDecimal countRefund;

    /**
     * 订单的状态
     */
    @TableField("status")
    private Integer status;

    /**
     * 逾期数
     */
    @TableField("overdue_number")
    private Integer overdueNumber;

    /**
     * 收款方式 paymentTerm
     */
    @TableField("payment_term")
    private Integer paymentTerm;

    /**
     * 申请时间
     */
    @TableField("create_time")
    private Date createTime;

    /**
     * 审核人关联主键
     */
    @TableField("check_user_id")
    private Long checkUserId;

    /**
     * 审核信息
     */
    @TableField("check_message")
    private String checkMessage;

    /**
     * 审核时间
     */
    @TableField("check_time")
    private Date checkTime;

    /**
     * 下款人关联主键
     */
    @TableField("draw_user_id")
    private Long drawUserId;

    /**
     * 下款信息
     */
    @TableField("draw_message")
    private String drawMessage;

    /**
     * 下款时间
     */
    @TableField("draw_time")
    private Date drawTime;
}
