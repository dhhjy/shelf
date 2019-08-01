package com.quick.shelf.modular.business.mapper;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.modular.business.entity.BBlackList;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface BBlackListMapper extends BaseMapper<BBlackList> {

    /**
     * 查询拉黑用户列表
     */
    Page<Map<String, Object>> selectBlackLists(@Param("page") Page page, @Param("dataScope") DataScope dataScope,
                                               @Param("name") String name, @Param("beginTime") String beginTime,
                                               @Param("endTime") String endTime, @Param("deptId") Long deptId);

    /**
     * 根据用户主键（userId）查询被拉黑的用户信息
     */
    BBlackList selectBBlackListByUserId(@Param("userId") Integer userId);
}
