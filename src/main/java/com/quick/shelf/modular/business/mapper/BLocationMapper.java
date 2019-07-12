package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BLocation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BLocationMapper extends BaseMapper<BLocation> {

    /**
     * 根据用户主键获取用户的GPS定位信息
     * 定位信息为一对多的关系
     *
     * @param userId
     * @return List<BLocation>
     */
    List<BLocation> selectBLocationByUserId(@Param("userId")Integer userId);
}
