package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.annotion.PortLog;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.entity.BXinYanData;
import com.quick.shelf.modular.business.mapper.BXinYanDataMapper;
import com.quick.shelf.modular.creditPort.xinYan.XinYanConstant;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

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

    /**
     * 保存新颜原始数据（新颜只获取原始数据）
     *
     * @param bXinYanData
     */
    public void insert(BXinYanData bXinYanData) {
        // 数据类型
        bXinYanData.setDataType(0);
        // 创建时间 一对多
        bXinYanData.setCreateTime(new Date());
        this.baseMapper.insert(bXinYanData);
    }

    /**
     * 获取新颜报告列表
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return Page<Map < String, Object>>
     */
    public Page<Map<String, Object>> selectBXinYanDatas(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBXinYanDatas(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 通过接口获取新颜雷达报告
     *
     * @param userId
     * @param bSysUser
     * @return
     */
    @PortLog(type = "radar", typeName = "全景雷达")
    public String getReDerData(Integer userId, BSysUser bSysUser) {
        String base64Str = XinYanConstant.assembleEncryptParams(String.valueOf(userId), bSysUser.getIdCard(),
                bSysUser.getName(), bSysUser.getPhoneNumber(), null);
        return XinYanConstant.getRaDerResult(base64Str);
    }
}
