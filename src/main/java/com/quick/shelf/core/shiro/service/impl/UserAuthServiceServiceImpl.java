package com.quick.shelf.core.shiro.service.impl;

import cn.hutool.core.convert.Convert;
import cn.stylefeng.roses.core.util.SpringContextHolder;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.common.constant.state.ManagerStatus;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.core.shiro.service.UserAuthService;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.mapper.BSysUserMapper;
import com.quick.shelf.modular.system.entity.User;
import com.quick.shelf.modular.system.mapper.MenuMapper;
import com.quick.shelf.modular.system.mapper.UserMapper;
import org.apache.shiro.authc.CredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.util.ByteSource;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
@DependsOn("springContextHolder")
@Transactional(readOnly = true)
public class UserAuthServiceServiceImpl implements UserAuthService {

    @Resource
    @Lazy
    private UserMapper userMapper;

    @Resource
    @Lazy
    private MenuMapper menuMapper;

    @Resource
    @Lazy
    private BSysUserMapper bSysUserMapper;

    public static UserAuthService me() {
        return SpringContextHolder.getBean(UserAuthService.class);
    }

    /**
     * 查询系统内部用户
     * @param account 账号
     * @return
     */
    @Override
    public User user(String account) {
        // 系统用户查询
        User user = userMapper.getByAccount(account);
        // 账号不存在
        if (null == user) {
            throw new CredentialsException();
        }

        // 账号被冻结
        if (!user.getStatus().equals(ManagerStatus.OK.getCode())) {
            throw new LockedAccountException();
        }
        return user;
    }

    /**
     * 查询系统客户
     * @param phoneNumber 手机号
     * @return
     */
    @Override
    public User BSysuser(String phoneNumber) {
        //查询系统客户
        BSysUser bSysUser = bSysUserMapper.getByPhoneNumber(phoneNumber);

        // 账号不存在
        if (null == bSysUser) {
            throw new CredentialsException();
        }

        // 账号被冻结
        if (!bSysUser.getStatus().equals(ManagerStatus.OK.getCode())) {
            throw new LockedAccountException();
        }

        // 将查询出来的数据进行封装为全局 ShiroUser
        User user = new User();
        user.setUserId(Long.valueOf(bSysUser.getUserId()));
        user.setAccount(bSysUser.getUserAccount());
        user.setPassword(bSysUser.getPassword());
        user.setSalt(bSysUser.getSalt());
        user.setDeptId(bSysUser.getDeptId());
        user.setName(bSysUser.getName());
        user.setPhone(bSysUser.getPhoneNumber());
        user.setStatus(bSysUser.getStatus());
        user.setSex(bSysUser.getSex());
        user.setRoleId("999");
        return user;
    }

    @Override
    public ShiroUser shiroUser(User user) {

        ShiroUser shiroUser = ShiroKit.createShiroUser(user);

        //用户角色数组
        Long[] roleArray = Convert.toLongArray(user.getRoleId());

        //获取用户角色列表
        List<Long> roleList = new ArrayList<>();
        List<String> roleNameList = new ArrayList<>();
        for (Long roleId : roleArray) {
            roleList.add(roleId);
            roleNameList.add(ConstantFactory.me().getSingleRoleName(roleId));
        }
        shiroUser.setRoleList(roleList);
        shiroUser.setRoleNames(roleNameList);

        return shiroUser;
    }

    @Override
    public List<String> findPermissionsByRoleId(Long roleId) {
        return menuMapper.getResUrlsByRoleId(roleId);
    }

    @Override
    public String findRoleNameByRoleId(Long roleId) {
        return ConstantFactory.me().getSingleRoleTip(roleId);
    }

    @Override
    public SimpleAuthenticationInfo info(ShiroUser shiroUser, User user, String realmName) {
        String credentials = user.getPassword();

        // 密码加盐处理
        String source = user.getSalt();
        ByteSource credentialsSalt = new Md5Hash(source);
        return new SimpleAuthenticationInfo(shiroUser, credentials, credentialsSalt, realmName);
    }

}
