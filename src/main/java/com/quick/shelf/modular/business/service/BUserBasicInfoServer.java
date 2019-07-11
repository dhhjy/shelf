package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.modular.business.entity.BUserBasicInfo;
import com.quick.shelf.modular.business.mapper.BUserBasicInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 用户基本信息业务层
 * 主要操作 'b_user_basic_info' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BUserBasicInfoServer extends ServiceImpl<BUserBasicInfoMapper, BUserBasicInfo> {

    /**
     *  通过用户表主键查询关联的用户信息
     *
     * @param userId
     * @return BUserBasicInfo
     */
    public BUserBasicInfo selectBUserBasicInfoByUserId(Integer userId){
        return this.baseMapper.selectBUserBasicInfoByUserId(userId);
    }
}
