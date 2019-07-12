package com.quick.shelf.modular.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.quick.shelf.modular.business.entity.BXinYanData;

public interface BXinYanDataMapper extends BaseMapper<BXinYanData> {

    /**
     * 根据用户的主键ID 去获取用户对应的新颜征信数据
     * 征信报告为一对多的模式，所以，此处只会返回最新时间
     * 的征信报告数据
     * @param userId
     * @return
     */
    BXinYanData selectBXinYanDataByUserId(BXinYanData bXinYanData);
}
