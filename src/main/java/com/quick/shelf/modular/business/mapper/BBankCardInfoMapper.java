package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BBankCardInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BBankCardInfoMapper extends BaseMapper<BBankCardInfo> {

    /**
     * 根据用户主键获取用户绑定的银行信息
     * 用户与银行卡信息的对应关系为一对多
     *
     * @param userId
     * @return List<BBankCardInfo>
     */
    List<BBankCardInfo> selectBBankCardInfosByUserId(@Param("userId") Integer userId);

    /**
     * 根据用户主键id获取用户的最后绑定的一张银行卡
     * 用户与银行卡的关系为一对多，次方法只会查询一张
     *
     * @param userId
     * @return BBankCardInfo
     */
    BBankCardInfo getBBankCardInfosByUserId(@Param("userId") Integer userId);
}
