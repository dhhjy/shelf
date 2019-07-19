package com.quick.shelf.core.util.sms.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
@Getter
@Setter
public class SmsPortInfo {
    private Integer id;                     //主键
    private String port_name;               //接口名称
    private Integer port_type;              //接口类型
    private String access_key_id;           //访问标识
    private String access_key_secet;        //访问密匙
    private String sign_name;               //签名
    private String template_code;           //模板编号
    private Integer sort;                   //排序
    private Integer if_verification;        //使用该接口作为验证码短信接口
    private Integer if_inform;              //使用该接口作为通知短信接口
    private Integer if_marketing;           //使用该接口作为营销短信接口
    private String remark;                  //备注
    private Date create_time;               //创建时间
}
