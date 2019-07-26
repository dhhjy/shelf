package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.modular.business.entity.BMobileData;
import com.quick.shelf.modular.business.mapper.BMobileDataMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BMobileDataService
 * @Description TODO
 * @Author ken
 * @Date 2019/7/19 21:31
 * @Version 1.0
 */
@Service
public class BMobileDataService extends ServiceImpl<BMobileDataMapper, BMobileData> {
    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BMobileDataService.class);

    /**
     * 批量插入通讯录
     *
     * @param tMobileData List<BMobileData> 集合对象
     */
    public void batchInsert(List<BMobileData> tMobileData) {
        if (tMobileData.size() == 0)
            return;

        // 插入
        this.baseMapper.batchInsert(tMobileData);

        logger.info("本次插入通讯录:{}条数据。", tMobileData.size());
    }

    /**
     * 查询电话列表
     *
     * @param tMobile
     * @return
     */
    public Page<Map<String, Object>> selectBMobileDatas(DataScope dataScope, Integer tMobile) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBMobileDatas(page, dataScope, tMobile);
    }
}
