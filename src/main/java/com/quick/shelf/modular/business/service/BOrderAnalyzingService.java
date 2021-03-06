package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BOrderAnalyzing;
import com.quick.shelf.modular.business.mapper.BOrderAnalyzingMapper;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 订单分析业务层
 */
@Service
public class BOrderAnalyzingService extends ServiceImpl<BOrderAnalyzingMapper, BOrderAnalyzing> {

    /**
     * 查询当天的订单分析
     *
     * @return
     */
    public BOrderAnalyzing selectBOrderAnalyzingByNowDate(Long deptId) {
        return this.baseMapper.selectBOrderAnalyzingByNowDate(deptId);
    }

    /**
     * 查询今日订单总数量
     */
    public Integer selectToDayBlanketOrder(Long deptId) {
        return this.baseMapper.selectToDayBlanketOrder(deptId);
    }

    /**
     * 查询今日下款订单数总数量
     */
    public Integer selectToDayLoanOrder(Long deptId) {
        return this.baseMapper.selectToDayLoanOrder(deptId);
    }

    /**
     * 新增订单分析
     */
    public void insert(BOrderAnalyzing bOrderAnalyzing) {
        bOrderAnalyzing.setCreateDate(DateUtil.getCurrentDate());
        this.baseMapper.insert(bOrderAnalyzing);
    }

    /**
     * 更新订单分析
     */
    public void update(BOrderAnalyzing bOrderAnalyzing) {
        this.baseMapper.updateById(bOrderAnalyzing);
    }

    /**
     * 获取12个月来所有的放款数据
     */
    public String selectEveryMonthData(Long deptId) {
        JSONArray json = JSONArray.fromObject(this.baseMapper.selectEveryMonthData(deptId));
        return json.get(0).toString();
    }

    /**
     * 获取每日门店top榜单
     * 默认查询7天，太多没办法展示
     * 不美观
     */
    public List<BOrderAnalyzing> selectEveryDayDeptData() {
        return this.baseMapper.selectEveryDayDeptData();
    }
}
