package com.quick.shelf.modular.business.mapper;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.modular.business.entity.BLiMuData;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @ClassName BLiMuMapper
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 20:25
 * @Version 1.0
 */
public interface BLiMuMapper extends BaseMapper<BLiMuData> {

    /**
     * 根据条件查询立木征信列表
     */
    Page<Map<String, Object>> selectBLiMuDatas(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptId") Long deptId);

    /**
     * 查询用户对应的立木征信数据
     * @param bLiMuData
     * @return
     */
    BLiMuData selectBLiMuDataByUserId(BLiMuData bLiMuData);
}
