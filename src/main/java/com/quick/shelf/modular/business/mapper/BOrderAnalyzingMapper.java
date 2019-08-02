package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BOrderAnalyzing;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Map;

public interface BOrderAnalyzingMapper extends BaseMapper<BOrderAnalyzing> {

    /**
     * 查询今天是否有统计记录
     */
    BOrderAnalyzing selectBOrderAnalyzingByNowDate(@Param("deptId") Long deptId);

    /**
     * 查询今日下款订单总数量
     */
    Integer selectToDayLoanOrder(@Param("deptId") Long deptId);

    /**
     * 查询今日订单总数量
     */
    Integer selectToDayBlanketOrder(@Param("deptId") Long deptId);

    /**
     * 查询每日门店top榜单数据
     */
    List<BOrderAnalyzing> selectEveryDayDeptData();

    /**
     * 查询12个月放款数据
     */
    Map<String, Object> selectEveryMonthData(@Param("deptId") Long deptId);
}
