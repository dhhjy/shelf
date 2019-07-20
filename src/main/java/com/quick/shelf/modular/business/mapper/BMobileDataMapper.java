package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BMobileData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @ClassName 通讯录电话
 * @Description TODO
 * @Author ken
 * @Date 2019/7/19 21:29
 * @Version 1.0
 */
public interface BMobileDataMapper extends BaseMapper<BMobileData> {

    /**
     * 批量插入手机通信录
     * @param list
     */
    void batchInsert(@Param("list") List<BMobileData> list);
}
