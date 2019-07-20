package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 立木通讯录实体对象
 */
@Data
@TableName("b_mobile_data")
public class BMobileData  implements Serializable {

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
     * 运营商类型
     */
    @TableField("t_mobile")
    private Integer tMobile;

    /**
     * 状态(0-未使用，1-已使用，被导出用作营销短信)
     */
    @TableField("status")
    private Integer status;         //状态(0-未使用，1-已使用，被导出用作营销短信)

    /**
     * 号码
     */
    @TableField("callNum")
    private String callNum;         //号码

    /**
     * 是否命中风险名单（1：命中0：未命中）
     */
    @TableField("isHitRiskList")
    private String isHitRiskList;   //是否命中风险名单（1：命中0：未命中）

    /**
     * 归属地
     */
    @TableField("attribution")
    private String attribution;     //归属地

    /**
     * 通话次数
     */
    @TableField("callCnt")
    private Integer callCnt;        //通话次数

    /**
     * 通话时长
     */
    @TableField("callTime")
    private String callTime;        //通话时长

    /**
     * 主叫次数
     */
    @TableField("callingCnt")
    private Integer callingCnt;     //主叫次数

    /**
     * 主叫时长(s)
     */
    @TableField("callingTime")
    private String callingTime;     //主叫时长(s)

    /**
     * 被叫次数
     */
    @TableField("calledCnt")
    private Integer calledCnt;      //被叫次数

    /**
     * 被叫时长(s)
     */
    @TableField("calledTime")
    private String calledTime;      //被叫时长(s)

    /**
     * 最近一次通话时间(yyyy-MM-ddHH:mm:ss)
     */
    @TableField("lastStart")
    private Date lastStart;         //最近一次通话时间(yyyy-MM-ddHH:mm:ss)

    /**
     * 最近一次通话时时长(s)
     */
    @TableField("lastTime")
    private String lastTime;        //最近一次通话时时长(s)

    /**
     * 创建时间(yyyy-MM-dd)
     */
    @TableField("create_time")
    private Date createTime;       //创建时间(yyyy-MM-dd)
}

