package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import com.quick.shelf.modular.business.mapper.BOrderDetailsMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 用户借款订单的业务层
 */
@Service
public class BOrderDetailsService extends ServiceImpl<BOrderDetailsMapper, BOrderDetails> {

    /**
     * 客户端用户申请借款
     *
     * @param bOrderDetails
     * @return
     */
    public BOrderDetails loanApplication(BOrderDetails bOrderDetails) {
        ShiroUser user = ShiroKit.getUserNotNull();
        bOrderDetails.setUserId(user.getId().intValue());
        bOrderDetails.setStatus(0);
        bOrderDetails.setCreateTime(DateUtil.getCurrentDate());
        this.baseMapper.insert(bOrderDetails);
        return bOrderDetails;
    }

    /**
     * 判断当前申请的用户是否有未完结的借款订单存在
     * 如果有订单存在，则不可继续借款，需要还清之前的
     * 订单才可以发起借款，
     * 存在返回
     *
     * @return
     */
    public boolean ifExistOrderByUserId() {
        return null == this.baseMapper.ifExistOrderByUserId(ShiroKit.getUserNotNull().getId().intValue());
    }

    /**
     * 查询待审核订单列表
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return
     */
    public Page<Map<String, Object>> selectToAuditList(DataScope dataScope, String name, String beginTime, String endTime, Long deptId, Integer status) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectToAuditList(page, dataScope, name, beginTime, endTime, deptId, status);
    }

    /**
     * 根据用户主键获取用户正在进行中的
     * 待审核订单
     *
     * @param userId
     * @return
     */
    public BOrderDetails selectBOrderDetailsByUserId(Integer userId) {
        return this.baseMapper.selectBOrderDetailsByUserId(userId);
    }

    /**
     * 订单审核
     */
    public void checkOrderDetails(BOrderDetails bOrderDetails) {
        this.baseMapper.updateById(bOrderDetails);
    }
}
