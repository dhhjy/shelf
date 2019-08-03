package com.quick.shelf.modular.business.mapper;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.modular.business.entity.BStagingList;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BStagingListMapper extends BaseMapper<BStagingList> {

    /**
     * 查询用户账单
     *
     * @return
     */
    Page<Map<String, Object>> selectBStagingListByUserId(@Param("page") Page page, @Param("dataScope") DataScope dataScope,
                                                         @Param("beginTime") String beginTime, @Param("endTime") String endTime,
                                                         @Param("userId") Integer userId, @Param("orderNumber") String orderNumber,
                                                         @Param("status") Integer status);

    /**
     * 批量更新还款计划中的剩余应还金额
     */
    void updateSurplusRefundByOrderNumber(@Param("pOrderNumber") String pOrderNumber, @Param("surplusRefund") BigDecimal surplusRefund);

    /**
     * 根据订单号查询所有的关联订单信息
     */
    List<BStagingList> selectBStagingListByPOrderNumber(@Param("orderNumber") String orderNumber);
}
