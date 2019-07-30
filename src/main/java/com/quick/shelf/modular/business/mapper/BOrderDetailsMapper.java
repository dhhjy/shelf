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
     * @param userId
     * @return
     */
    BOrderDetails ifExistOrderByUserId(@Param("userId") Integer userId);

    /**
     * 查询用户待审核订单列表
     * @return
     */
    Page<Map<String, Object>> selectToAuditList(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptId") Long deptId);

}
