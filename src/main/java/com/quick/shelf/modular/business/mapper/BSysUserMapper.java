package com.quick.shelf.modular.business.mapper;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.modular.business.entity.BSysUser;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @ClassName BSysUserMapper
 * @Description TODO
 * @Author ken
 * @Date 2019/7/10 15:36
 * @Version 1.0
 */
public interface BSysUserMapper extends BaseMapper<BSysUser> {

    /**
     * 根据条件查询用户列表
     */
    Page<Map<String, Object>> selectBSysUsers(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptId") Long deptId);

    /**
     * 逻辑删除用户
     * @param userId
     */
    void deleteBSysUserByUserId(@Param("userId") Integer userId);

    /**
     * 通过账号获取用户
     */
    BSysUser getByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
