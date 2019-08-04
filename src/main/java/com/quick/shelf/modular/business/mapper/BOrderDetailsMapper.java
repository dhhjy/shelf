package com.quick.shelf.modular.business.mapper;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 用户借款订单dao
 */
public interface BOrderDetailsMapper extends BaseMapper<BOrderDetails> {

    /**
     * 查询贷款订单是否重复
     *
     * @param userId
     * @return
     */
    BOrderDetails ifExistOrderByUserId(@Param("userId") Integer userId);

    /**
     * 查询审核、放款、还款、完结、回退、拒绝 列表通用查询方法
     *
     * @return
     */
    Page<Map<String, Object>> selectToAuditList(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("name") String name,
                                                @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptId") Long deptId, @Param("status") Integer status,
                                                @Param("overdue") String overdue);

    /**
     * 根据用户主键查询用户正在进行
     * 中的订单信息
     *
     * @param userId
     * @return
     */
    BOrderDetails selectBOrderDetailsByUserId(@Param("userId") Integer userId);
}
