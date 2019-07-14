package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BPortCount;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface BPortCountMapper extends BaseMapper<BPortCount> {

    /**
     * 获取今日接口数与接口的总数
     *
     * @return
     */
    Map<String, String> getPortNum(@Param("deptId") Long deptId);

    /**
     * 获取今日接口数与前6天的接口的总数
     *
     * @return
     */
    Map<String, Object> getPortChartCount(@Param("deptId") Long deptId);

    /**
     * 获取今日接口价格与前6天的价格统计总数
     *
     * @return
     */
    Map<String, Object> getPortChartPrice(@Param("deptId") Long deptId);
}
