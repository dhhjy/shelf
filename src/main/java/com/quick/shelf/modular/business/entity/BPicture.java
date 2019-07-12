package com.quick.shelf.modular.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户相关的照片关联表
 * 'b_picture'
 */
@Data
@TableName("b_picture")
public class BPicture implements Serializable {

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
     * 身份证照片正面url
     */
    @TableField("id_card_url")
    private String idCardUrl;

    /**
     * 身份证背面url
     */
    @TableField("id_card_back_url")
    private String idCardBackUrl;

    /**
     * 银行卡正面url
     */
    @TableField("bank_card_url")
    private String bankCardUrl;

    /**
     * 手持身份证和银行卡 拍照 url
     */
    @TableField("man_card_url")
    private String manCardUrl;

    /**
     * 人脸照
     */
    @TableField("id_man_url")
    private String idManUrl;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private Date createTime;
}
