package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.modular.business.entity.BBankCardInfo;
import com.quick.shelf.modular.business.mapper.BBankCardInfoMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户关联的银行卡业务层
 * 主要操作 'b_bank_card_info' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BBankCardInfoService extends ServiceImpl<BBankCardInfoMapper, BBankCardInfo> {

    /**
     * 根据用户主键获取用户绑定的银行信息
     * 用户与银行卡信息的对应关系为一对多
     *
     * @param userId
     * @return List<BBankCardInfo>
     */
    public List<BBankCardInfo> selectBBankCardInfosByUserId(Integer userId) {
        return this.baseMapper.selectBBankCardInfosByUserId(userId);
    }

    /**
     * 根据用户主键id获取用户的最后绑定的一张银行卡
     * 用户与银行卡的关系为一对多，次方法只会查询一张
     *
     * @param userId
     * @return BBankCardInfo
     */
    public BBankCardInfo getBBankCardInfosByUserId(Integer userId) {
        return this.baseMapper.getBBankCardInfosByUserId(userId);
    }
}
