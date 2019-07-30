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

    public boolean ifExistOrderByUserId() {
        return null == this.baseMapper.ifExistOrderByUserId(ShiroKit.getUserNotNull().getId().intValue());
    }

    /**
     * 查询待审核订单列表
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return
     */
    public Page<Map<String, Object>> selectToAuditList(DataScope dataScope, String name, String beginTime, String endTime, Long deptId) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectToAuditList(page, dataScope, name, beginTime, endTime, deptId);
    }
}
