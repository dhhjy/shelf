package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.modular.business.entity.BIdentityInfo;
import com.quick.shelf.modular.business.mapper.BIdentityInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 用户身份证关联表的业务层
 * 主要操作 'b_identity_info' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BIdentityInfoService extends ServiceImpl<BIdentityInfoMapper, BIdentityInfo> {

    /**
     * 通过用户主键查询用户认证的身份证信息
     *
     * @param userId
     * @return BIdentityInfo
     */
    public BIdentityInfo selectBIdentityInfoByUserId(Integer userId) {
        return this.baseMapper.selectBIdentityInfoByUserId(userId);
    }
}
