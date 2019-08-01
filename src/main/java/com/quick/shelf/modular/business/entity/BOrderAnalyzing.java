package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("b_order_analyzing")
public class BOrderAnalyzing implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 关联部门
     */
    @TableField("dept_id")
    private Long deptId;

    /**
     * 关联部门名称
     *
     * 非数据库字段
     */
    private String simpleName;

    /**
     * 合同金额
     */
    @TableField("contract_amount")
    private BigDecimal contractAmount;

    /**
     * 实际放款金额
     */
    @TableField("loan_amount")
    private BigDecimal loanAmount;

    /**
     * 放款率
     */
    @TableField("loan_ratio")
    private BigDecimal loanRatio;

    /**
     * 统计时间
     */
    @TableField("create_date")
    private Date createDate;
}
