package com.quick.shelf.modular.h5.model;

import lombok.Data;

/**
 * H5 客户端注册对象
 */
@Data
public class RegisterDto {
    /**
     * 注册账号，同时也是手机号
     */
    private String userAccount;

    /**
     * 注册时的验证码
     */
    private String vercode;

    /**
     * 用户密码
     */
    private String password;

    /**
     * 用户注册后属于哪家公司
     */
    private Long deptId;
}
