package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BEmergencyContactInfo;
import org.apache.ibatis.annotations.Param;

public interface BEmergencyContactInfoMapper extends BaseMapper<BEmergencyContactInfo> {

    /**
     *  通过用户表主键查询用户的紧急联系人信息
     * @param userId
     * @return
     */
    BEmergencyContactInfo selectBEmergencyContactInfoByUserId(@Param("userId") Integer userId);
}
