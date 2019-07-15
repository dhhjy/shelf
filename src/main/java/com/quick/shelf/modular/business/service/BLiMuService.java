package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.modular.business.entity.BLiMuData;
import com.quick.shelf.modular.business.mapper.BLiMuMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @ClassName 立木征信业务层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/15 20:19
 * @Version 1.0
 */
@Service
public class BLiMuService extends ServiceImpl<BLiMuMapper, BLiMuData> {

    /**
     * 获取立木报告列表
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return Page<Map < String, Object>>
     */
    public Page<Map<String, Object>> selectBLiMuDatas(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBLiMuDatas(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 根据用户的主键，以及征信报告类型去获取对应的立木征信数据
     * <p>立木征信报告为一对多的模式，所以会返回最新一份的征信报告</p>
     * 立木的报告分两种，有原始数据 0，有页面数据 1
     *
     * @param userId 立木征信中关联的用户主键
     * @param type  立木征信四类报告的类型
     * @param dataType  原始数据：0 页面报告：1
     * @return BXinYanData
     */
    public BLiMuData selectBLiMuDataByUserId(Integer userId, String type, String dataType) {
        BLiMuData bXinYanData = new BLiMuData();
        bXinYanData.setUserId(userId);
        bXinYanData.setType(type);
        bXinYanData.setDataType(dataType);
        return this.baseMapper.selectBLiMuDataByUserId(bXinYanData);
    }
}
