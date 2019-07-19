package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.modular.business.core.constant.BusinessConstant;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.mapper.BSysUserStatusMapper;
import org.springframework.stereotype.Service;

/**
 * 用户状态关联业务层
 * 主要处理用户的各种认证状态
 */
@Service
public class BSysUserStatusService extends ServiceImpl<BSysUserStatusMapper, BSysUserStatus> {

    /**
     * 通过用户表主键查询关联用户的各种认证状态信息
     *
     * @param userId
     * @return BSysUserStatus
     */
    public BSysUserStatus selectBSysUserStatusByUserId(Integer userId) {
        return this.baseMapper.selectBSysUserStatusByUserId(userId);
    }

    /**
     * 根据 type类型 重置用户认证的信息
     *
     * @param userId
     * @param type
     */
    public void resetInfo(Integer userId, String type) {
        BSysUserStatus bSysUserStatus = new BSysUserStatus();
        bSysUserStatus.setUserId(userId);
        if (BusinessConstant.LIMU_MOBILE_REPORT_STATUS.contains(type))
            bSysUserStatus.setLimuMobileReportStatus("0");
        if (BusinessConstant.IDENTITY_STATUS.contains(type))
            bSysUserStatus.setIdentityStatus("0");
        if (BusinessConstant.USER_BASIC_STATUS.contains(type))
            bSysUserStatus.setUserBasicStatus("0");
        if (BusinessConstant.BANK_INFO_STATUS.contains(type))
            bSysUserStatus.setBankInfoStatus("0");
        if (BusinessConstant.CONTACT_STATUS.contains(type))
            bSysUserStatus.setContactStatus("0");
        if (BusinessConstant.SMS_STATUS.contains(type))
            bSysUserStatus.setSmsStatus("0");
        this.baseMapper.updateByUserId(bSysUserStatus);
    }

    /**
     * 重置用户的全部认证信息
     * 立木运营商报告(手机),身份证认证，用户个人信息，
     * 银行卡信息，紧急联系人信息，短信认证信息
     *
     * @param userId
     */
    public void resetAllInfo(Integer userId) {
        BSysUserStatus bSysUserStatus = new BSysUserStatus();
        bSysUserStatus.setUserId(userId);
        bSysUserStatus.setLimuMobileReportStatus("0");
        bSysUserStatus.setIdentityStatus("0");
        bSysUserStatus.setUserBasicStatus("0");
        bSysUserStatus.setBankInfoStatus("0");
        bSysUserStatus.setContactStatus("0");
        bSysUserStatus.setSmsStatus("0");
        this.baseMapper.updateByUserId(bSysUserStatus);
    }

    /**
     * 新增用户状态信息表
     * @param bSysUserStatus
     */
    public void insertBSysUserStatus(BSysUserStatus bSysUserStatus) {
        this.baseMapper.insert(bSysUserStatus);
    }

    /**
     * 根据关联用户主键 userId 更新用户状态信息
     *
     * @param bSysUserStatus
     */
    public void updateByUserId(BSysUserStatus bSysUserStatus) {
        this.baseMapper.updateByUserId(bSysUserStatus);
    }
}
