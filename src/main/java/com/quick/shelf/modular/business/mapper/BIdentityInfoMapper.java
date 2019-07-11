package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BIdentityInfo;
import org.apache.ibatis.annotations.Param;

public interface BIdentityInfoMapper extends BaseMapper<BIdentityInfo> {

    /**
     * 通过用户主键获取用户认证的身份证信息
     *
     * @param userId
     * @return BIdentityInfo
     */
    BIdentityInfo selectBIdentityInfoByUserId(@Param("userId") Integer userId);
}
