package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.modular.business.entity.BPortCount;
import com.quick.shelf.modular.business.mapper.BPortCountMapper;
import com.quick.shelf.modular.system.service.DictService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @ClassName 接口统计业务层
 * @Description TODO
 * @Author ken
 * @Date 2019/7/14 16:27
 * @Version 1.0
 */
@Service
public class BPortCountService extends ServiceImpl<BPortCountMapper, BPortCount> {

    @Resource
    private DictService dictService;

    @Resource
    private BSysUserService bSysUserService;

    /**
     * 插入接口统计
     *
     * @param deptId
     * @param type
     * @param typeName
     * @param msg
     */
    public void insertBPortCount(Long deptId, String type, String typeName, String msg) {
        BPortCount bPortCount = new BPortCount();
        bPortCount.setDeptId(deptId);
        bPortCount.setType(type);
        bPortCount.setTypeName(typeName);
        bPortCount.setResult(msg);
        bPortCount.setCreateTime(new Date());
        bPortCount.setUnitPrice(Double.valueOf(this.dictService.selectDictByName(type).getCode()));
        this.baseMapper.insert(bPortCount);
    }

    /**
     * 获取接口类的 今日调用数量，总调用数量
     * 今日调用总价，以及总调用价格
     * 当接口回调日志完成以后，会清除该缓存，在重写调用本方法查询
     * @return
     */
    public Map<String, String> getPortNum(Long deptId) {
        if (!ShiroKit.isAdmin() || !ShiroKit.isDeptAdmin())
            deptId = Objects.requireNonNull(ShiroKit.getUser()).getDeptId();
        return this.baseMapper.getPortNum(deptId);
    }

    /**
     * 获取接口类的 图表类型的数据 接口调用量、接口调用价格
     * 获取7天内的数据
     * 当接口回调日志完成以后，会清除该缓存，在重写调用本方法查询
     * @return
     */
    public Map<String, List> getPortChart(Long deptId) {
        Map<String, List> result = new HashMap<>();
        if (!ShiroKit.isAdmin() || !ShiroKit.isDeptAdmin())
            deptId = Objects.requireNonNull(ShiroKit.getUser()).getDeptId();
        Map<String, Object> map = this.baseMapper.getPortChartCount(deptId);

        LinkedList<Integer> data = new LinkedList<>();
        data.add(Integer.valueOf(map.get("6_count").toString()));
        data.add(Integer.valueOf(map.get("5_count").toString()));
        data.add(Integer.valueOf(map.get("4_count").toString()));
        data.add(Integer.valueOf(map.get("3_count").toString()));
        data.add(Integer.valueOf(map.get("2_count").toString()));
        data.add(Integer.valueOf(map.get("1_count").toString()));
        data.add(Integer.valueOf(map.get("0_count").toString()));

        LinkedList<Integer> day = new LinkedList<>();
        day.add(Integer.valueOf(map.get("6_day").toString()));
        day.add(Integer.valueOf(map.get("5_day").toString()));
        day.add(Integer.valueOf(map.get("4_day").toString()));
        day.add(Integer.valueOf(map.get("3_day").toString()));
        day.add(Integer.valueOf(map.get("2_day").toString()));
        day.add(Integer.valueOf(map.get("1_day").toString()));
        day.add(Integer.valueOf(map.get("0_day").toString()));

        LinkedList<Double> price = new LinkedList<>();
        if (null == map.get("6_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("6_price").toString()));
        if (null == map.get("5_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("5_price").toString()));
        if (null == map.get("4_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("4_price").toString()));
        if (null == map.get("3_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("3_price").toString()));
        if (null == map.get("2_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("2_price").toString()));
        if (null == map.get("1_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("1_price").toString()));
        if (null == map.get("0_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("0_price").toString()));

        result.put("data", data);
        result.put("day", day);
        result.put("price", price);
        return result;
    }

    /**
     * 获取接口类的 今日调用数量，总调用数量
     * 今日调用总价，以及总调用价格
     * 当接口回调日志完成以后，会清除该缓存，在重写调用本方法查询
     * @return
     */
    public Map<String, String> getSmsPortNum(Long deptId) {
        if (!ShiroKit.isAdmin() || !ShiroKit.isDeptAdmin())
            deptId = Objects.requireNonNull(ShiroKit.getUser()).getDeptId();
        return this.baseMapper.getSmsPortNum(deptId);
    }

    /**
     * 获取短信接口类的 图表类型的数据 接口调用量、接口调用价格
     * 获取7天内的数据
     * 当接口回调日志完成以后，会清除该缓存，在重写调用本方法查询
     * @return
     */
    public Map<String, List> getSmsPortChartCount(Long deptId) {
        Map<String, List> result = new HashMap<>();
        if (!ShiroKit.isAdmin() || !ShiroKit.isDeptAdmin())
            deptId = Objects.requireNonNull(ShiroKit.getUser()).getDeptId();
        Map<String, Object> map = this.baseMapper.getSmsPortChartCount(deptId);

        LinkedList<Integer> data = new LinkedList<>();
        data.add(Integer.valueOf(map.get("6_count").toString()));
        data.add(Integer.valueOf(map.get("5_count").toString()));
        data.add(Integer.valueOf(map.get("4_count").toString()));
        data.add(Integer.valueOf(map.get("3_count").toString()));
        data.add(Integer.valueOf(map.get("2_count").toString()));
        data.add(Integer.valueOf(map.get("1_count").toString()));
        data.add(Integer.valueOf(map.get("0_count").toString()));

        LinkedList<Integer> day = new LinkedList<>();
        day.add(Integer.valueOf(map.get("6_day").toString()));
        day.add(Integer.valueOf(map.get("5_day").toString()));
        day.add(Integer.valueOf(map.get("4_day").toString()));
        day.add(Integer.valueOf(map.get("3_day").toString()));
        day.add(Integer.valueOf(map.get("2_day").toString()));
        day.add(Integer.valueOf(map.get("1_day").toString()));
        day.add(Integer.valueOf(map.get("0_day").toString()));

        LinkedList<Double> price = new LinkedList<>();
        if (null == map.get("6_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("6_price").toString()));
        if (null == map.get("5_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("5_price").toString()));
        if (null == map.get("4_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("4_price").toString()));
        if (null == map.get("3_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("3_price").toString()));
        if (null == map.get("2_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("2_price").toString()));
        if (null == map.get("1_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("1_price").toString()));
        if (null == map.get("0_price"))
            price.add(0.0);
        else
            price.add(Double.valueOf(map.get("0_price").toString()));

        result.put("data", data);
        result.put("day", day);
        result.put("price", price);
        return result;
    }

    /**
     * 客户端用户总量
     * 查询结果会被缓存，防止多次查询，浪费资源
     * 当客户端注册时，会删除该接口的缓存，重新查询
     * @param deptId
     * @return
     */
    public Integer getClientUserCount(Long deptId) {
        if (ShiroKit.isAdmin() || ShiroKit.isDeptAdmin()) {
            //管理员或者总公司查询就查询全部出来
            return this.bSysUserService.getBSysUserCount(0L);
        } else {
            // 分公司只查询自己的
            return this.bSysUserService.getBSysUserCount(deptId);
        }
    }

    /**
     * 客户端今日用户总量
     * 查询结果会被缓存，防止多次查询，浪费资源
     * 当客户端注册时，会删除该接口的缓存，重新查询
     * @param deptId
     * @return
     */
    public Integer getClientToDayUserCount(Long deptId) {
        if (ShiroKit.isAdmin() || ShiroKit.isDeptAdmin()) {
            //管理员或者总公司查询就查询全部出来
            return this.bSysUserService.getBSysUserToDayCount(0L);
        } else {
            // 分公司只查询自己的
            return this.bSysUserService.getBSysUserToDayCount(deptId);
        }
    }
}
