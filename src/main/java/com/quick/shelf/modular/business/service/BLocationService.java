package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.modular.business.entity.BLocation;
import com.quick.shelf.modular.business.mapper.BLocationMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BLocationService extends ServiceImpl<BLocationMapper, BLocation> {

    /**
     * 根据用户主键获取用户的GPS定位信息
     * 定位信息为一对多的关系
     *
     * @param userId
     * @return List<BLocation>
     */
    public List<BLocation> selectBLocationByUserId(Integer userId){
        return this.baseMapper.selectBLocationByUserId(userId);
    }
}
