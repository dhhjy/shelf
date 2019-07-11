package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BUserBasicInfo;
import org.apache.ibatis.annotations.Param;

public interface BUserBasicInfoMapper extends BaseMapper<BUserBasicInfo> {

    /**
     * 根据用户关联主键查找用户关联信息
     */
    BUserBasicInfo selectBUserBasicInfoByUserId(@Param("userId") Integer userId);
}
