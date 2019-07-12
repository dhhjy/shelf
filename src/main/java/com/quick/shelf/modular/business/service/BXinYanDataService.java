package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.modular.business.entity.BXinYanData;
import com.quick.shelf.modular.business.mapper.BXinYanDataMapper;
import org.springframework.stereotype.Service;

/**
 * 用户关联的新颜征信业务层
 * 'b_xinyan_data'
 */
@Service
public class BXinYanDataService extends ServiceImpl<BXinYanDataMapper, BXinYanData> {

    /**
     * 根据用户的主键，以及征信报告类型去获取对应的新颜征信数据
     * <p>新颜征信报告为一对多的模式，所以会返回最新一份的征信报告</p>
     * 新颜的报告只有原始数据，所以将默认为 0
     *
     * @param userId
     * @param type
     * @return BXinYanData
     */
    public BXinYanData selectBXinYanDataByUserId(Integer userId, String type) {
        BXinYanData bXinYanData = new BXinYanData();
        bXinYanData.setUserId(userId);
        bXinYanData.setType(type);
        bXinYanData.setDataType(0);
        return this.baseMapper.selectBXinYanDataByUserId(bXinYanData);
    }

}
