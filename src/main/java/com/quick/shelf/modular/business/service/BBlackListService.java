package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BBlackList;
import com.quick.shelf.modular.business.mapper.BBlackListMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 黑名单服务层
 */
@Service
public class BBlackListService extends ServiceImpl<BBlackListMapper, BBlackList> {

    /**
     * 根据条件查询黑名单列表
     *
     * @author zcn
     * @Date 2019/7/10 15:35
     */
    public Page<Map<String, Object>> selectBlackLists(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBlackLists(page, dataScope, name, beginTime, endTime, deptId);
    }

    /**
     * 根据用户主键ID查询拉黑信息
     *
     * @param userId
     * @return
     */
    public BBlackList selectBBlackListByUserId(Integer userId) {
        return this.baseMapper.selectBBlackListByUserId(userId);
    }

    /**
     * 添加用户到黑名单
     */
    public void insert(BBlackList bBlackList) {
        bBlackList.setBlackTime(DateUtil.getCurrentDate());
        this.baseMapper.insert(bBlackList);
    }

    /**
     * 解除用户黑名单限制
     */
    public void unBlackList(Integer userId) {
        BBlackList bBlackList = selectBBlackListByUserId(userId);
        this.baseMapper.deleteById(bBlackList.getId());
    }
}
