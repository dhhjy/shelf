package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BEmergencyContactInfo;
import com.quick.shelf.modular.business.mapper.BEmergencyContactInfoMapper;
import org.springframework.stereotype.Service;

/**
 * 用户紧急联系人关联表的业务层
 * 主要操作 'b_emergency_contact_info' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BEmergencyContactInfoService extends ServiceImpl<BEmergencyContactInfoMapper, BEmergencyContactInfo> {

    /**
     * 通过用户表主键查询用户的紧急联系人信息
     *
     * @param userId
     * @return BEmergencyContactInfo
     */
    public BEmergencyContactInfo selectBEmergencyContactInfoByUserId(Integer userId) {
        return this.baseMapper.selectBEmergencyContactInfoByUserId(userId);
    }

    /**
     * 新增用户的紧急联系人信息
     */
    public void insertBEmergencyContactInfo(BEmergencyContactInfo bEmergencyContactInfo) {
        bEmergencyContactInfo.setCreateTime(DateUtil.getCurrentDate());
        this.baseMapper.insert(bEmergencyContactInfo);
    }

    /**
     * 更新用户的紧急联系人信息
     */
    public void updateBEmergencyContactInfo(BEmergencyContactInfo bEmergencyContactInfo) {
        bEmergencyContactInfo.setUpdateTime(DateUtil.getCurrentDate());
        this.baseMapper.updateById(bEmergencyContactInfo);
    }
}
