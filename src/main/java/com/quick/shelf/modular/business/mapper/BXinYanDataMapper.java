package com.quick.shelf.modular.business.mapper;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.modular.business.entity.BXinYanData;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface BXinYanDataMapper extends BaseMapper<BXinYanData> {

    /**
     * 根据用户的主键ID 去获取用户对应的新颜征信数据
     * 征信报告为一对多的模式，所以，此处只会返回最新时间
     * 的征信报告数据
     * @param userId
     * @return
     */
    BXinYanData selectBXinYanDataByUserId(BXinYanData bXinYanData);

    /**
     * 根据条件查询用户列表
     */
    Page<Map<String, Object>> selectBXinYanDatas(@Param("page") Page page, @Param("dataScope") DataScope dataScope, @Param("name") String name, @Param("beginTime") String beginTime, @Param("endTime") String endTime, @Param("deptId") Long deptId);

}
