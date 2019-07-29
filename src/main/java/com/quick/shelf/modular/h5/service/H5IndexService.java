package com.quick.shelf.modular.h5.service;

import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.service.BSysUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * H5客户端服务层
 */
@Service
public class H5IndexService {

    @Resource
    private BSysUserService bSysUserService;

    /**
     * 支付密码设置
     */
    public void setPayPasswordFun(String payPassword) {
        Long userId = ShiroKit.getUserNotNull().getId();
        BSysUser user = this.bSysUserService.selectBSysUserByUserId(userId.intValue());
        String salt = ShiroKit.getRandomSalt(5);
        String password = ShiroKit.md5(payPassword, salt);
        user.setPayPassword(password);
        user.setPaySalt(salt);
        this.bSysUserService.update(user);
    }

    /**
     * 用户密码比对
     */
    public boolean comparisonPayPassword(String payPassword) {
        Long userId = ShiroKit.getUserNotNull().getId();
        BSysUser user = this.bSysUserService.selectBSysUserByUserId(userId.intValue());
        String orgPass = user.getPayPassword();
        String password = ShiroKit.md5(payPassword, user.getPaySalt());
        return orgPass.equals(password);
    }
}
