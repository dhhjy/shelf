package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.mapper.BSysUserMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户信息主表的业务层
 * 主要操作 'b_sys_user' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BSysUserService extends ServiceImpl<BSysUserMapper, BSysUser> {

    /**
     * 根据条件查询用户列表
     *
     * @author zcn
     * @Date 2019/7/10 15:35
     */
    public Page<Map<String, Object>> selectBSysUsers(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBSysUsers(page, dataScope, name, beginTime, endTime, deptId);
    }
}
