package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import io.lettuce.core.dynamic.annotation.Param;

public interface BSysUserStatusMapper extends BaseMapper<BSysUserStatus> {

    /**
     * 通过用户表主键查询关联用户的各种认证状态信息
     *
     * @param userId
     * @return BPicture
     */
    BSysUserStatus selectBSysUserStatusByUserId(@Param("userId") Integer userId);

    /**
     * 更新用户的状态
     * @param bSysUserStatus
     */
    void updateByUserId(BSysUserStatus bSysUserStatus);
}
