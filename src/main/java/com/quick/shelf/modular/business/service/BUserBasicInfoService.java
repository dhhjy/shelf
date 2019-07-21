package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BEmergencyContactInfo;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.entity.BUserBasicInfo;
import com.quick.shelf.modular.business.mapper.BUserBasicInfoMapper;
import com.quick.shelf.modular.constant.BusinessConst;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户基本信息业务层
 * 主要操作 'b_user_basic_info' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BUserBasicInfoService extends ServiceImpl<BUserBasicInfoMapper, BUserBasicInfo> {

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BEmergencyContactInfoService emergencyContactInfoService;

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    /**
     * 通过用户表主键查询关联的用户信息
     *
     * @param userId
     * @return BUserBasicInfo
     */
    public BUserBasicInfo selectBUserBasicInfoByUserId(Integer userId) {
        return this.baseMapper.selectBUserBasicInfoByUserId(userId);
    }

    /**
     * 新增用户详细信息
     *
     * @param bUserBasicInfo
     */
    public void insertBUserBasicInfo(BUserBasicInfo bUserBasicInfo) {
        bUserBasicInfo.setCreateTime(DateUtil.getCurrentDate());
        this.baseMapper.insert(bUserBasicInfo);
    }

    /**
     * 更新用户详细信息
     *
     * @param bUserBasicInfo
     */
    public void updateBUserBasicInfo(BUserBasicInfo bUserBasicInfo) {
        bUserBasicInfo.setUpdateTime(DateUtil.getCurrentDate());
        this.baseMapper.updateById(bUserBasicInfo);
    }

    /**
     * 新增或更新用户的基本信息，单位信息，紧急联系人
     *
     * @param userId
     * @param bUserBasicInfo
     * @param bEmergencyContactInfo
     */
    public void userBasicInfoDoAddAndUpt(Integer userId, BSysUser bSysUser, BUserBasicInfo bUserBasicInfo, BEmergencyContactInfo bEmergencyContactInfo) {
        // 更新用户信息
        bSysUser.setUserId(userId);
        bSysUser.setUpdateTime(DateUtil.getCurrentDate());
        if (bSysUser.getSex().equals("M"))
            bSysUser.setSex("男");
        if (bSysUser.getSex().equals("F"))
            bSysUser.setSex("女");
        this.bSysUserService.updateById(bSysUser);

        // 更新或新增用户详细信息
        bUserBasicInfo.setUserId(userId);
        BUserBasicInfo userBasicInfo = this.selectBUserBasicInfoByUserId(userId);
        // 不存在用户信息就新增，存在则更新
        if (null == userBasicInfo) {
            this.insertBUserBasicInfo(bUserBasicInfo);
        } else {
            bUserBasicInfo.setId(userBasicInfo.getId());
            this.updateBUserBasicInfo(bUserBasicInfo);
        }

        // 更新或新增紧急联系人信息
        bEmergencyContactInfo.setUserId(userId);
        BEmergencyContactInfo emergencyContactInfo = this.emergencyContactInfoService.selectBEmergencyContactInfoByUserId(userId);
        // 不存在该用户的紧急联系人则新增，存在则更新
        if (null == emergencyContactInfo) {
            this.emergencyContactInfoService.insertBEmergencyContactInfo(bEmergencyContactInfo);
        } else {
            bEmergencyContactInfo.setId(emergencyContactInfo.getId());
            this.emergencyContactInfoService.updateBEmergencyContactInfo(bEmergencyContactInfo);
        }

        // 改变用户的认证状态
        BSysUserStatus bSysUserStatus = new BSysUserStatus();
        bSysUserStatus.setUserId(userId);
        // 个人信息状态
        bSysUserStatus.setUserBasicStatus(BusinessConst.OK);
        // 关联紧急联系人认证状态
        bSysUserStatus.setContactStatus(BusinessConst.OK);
        this.bSysUserStatusService.updateByUserId(bSysUserStatus);
    }
}
