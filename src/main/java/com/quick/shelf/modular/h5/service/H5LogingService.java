package com.quick.shelf.modular.h5.service;

import com.quick.shelf.core.common.constant.Const;
import com.quick.shelf.core.common.constant.state.ManagerStatus;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.service.BSysUserService;
import com.quick.shelf.modular.business.service.BSysUserStatusService;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.h5.model.RegisterDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * H5客户端服务层
 */
@Service
public class H5LogingService {

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    /**
     * 添加用戶
     * 添加成功后 会缓存清空掉所有的控制台缓存
     *
     * @author fengshuonan
     * @Date 2018/12/24 22:51
     */
    @CacheEvict(value = BusinessConst.CONSOLE, allEntries = true)
    public String registerBSysUser(RegisterDto registerDto) {
        // 完善账号信息
        String salt = ShiroKit.getRandomSalt(5);
        String password = ShiroKit.md5(registerDto.getPassword(), salt);
        BSysUser bSysUser = new BSysUser();
        bSysUser.setUserAccount(registerDto.getUserAccount());
        bSysUser.setPhoneNumber(registerDto.getUserAccount());
        bSysUser.setPassword(password);
        bSysUser.setSalt(salt);
        bSysUser.setDeptId(registerDto.getDeptId());
        bSysUser.setStatus(ManagerStatus.OK.getCode());
        bSysUser.setRoleId(Const.CLIENT_USER_ROLE);
        bSysUser.setCreateTime(DateUtil.getCurrentDate());
        this.bSysUserService.insert(bSysUser);
        // 同时创建用户的状态信息表
        BSysUserStatus bSysUserStatus = new BSysUserStatus();
        bSysUserStatus.setUserId(bSysUser.getUserId());
        bSysUserStatusService.insertBSysUserStatus(bSysUserStatus);
        return "注册成功!";
    }
}
