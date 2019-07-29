package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import org.apache.ibatis.annotations.Param;

/**
 * 用户借款订单dao
 */
public interface BOrderDetailsMapper extends BaseMapper<BOrderDetails> {

    /**
     * 查询贷款订单是否重复
     * @param userId
     * @return
     */
    BOrderDetails ifExistOrderByUserId(@Param("userId") Integer userId);
}
