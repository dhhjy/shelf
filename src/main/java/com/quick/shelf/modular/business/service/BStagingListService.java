package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import com.quick.shelf.modular.business.entity.BStagingList;
import com.quick.shelf.modular.business.mapper.BStagingListMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 还款账单业务层
 */
@Service
public class BStagingListService extends ServiceImpl<BStagingListMapper, BStagingList> {

    /**
     * 还款状态-已还款
     */
    private static final Integer REPAYMENT = 1;

    /**
     * 还款状态-逾期
     */
    private static final Integer OVERDUE = 2;

    @Resource
    private BOrderDetailsService bOrderDetailsService;

    /**
     * 新增还款账单
     */
    public void insert(BStagingList bStagingList) {
        this.baseMapper.insert(bStagingList);
    }

    /**
     * 查询用户账单
     */
    public Page<Map<String, Object>> selectBStagingListByUserId(DataScope dataScope, String beginTime, String endTime, Integer userId, String orderNumber, Integer status) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectBStagingListByUserId(page, dataScope, beginTime, endTime, userId, orderNumber, status);
    }

    /**
     * 查询用户主键查询对应的逾期账单
     */
    public List<BStagingList> selectOverdueStagingList(Integer userId) {
        return this.baseMapper.selectOverdueStagingList(userId);
    }

    /**
     * 根据订单号查询分期账单
     *
     * @param orderNumber
     * @return
     */
    private List<BStagingList> selectBStagingListByPOrderNumber(String orderNumber) {
        return this.baseMapper.selectBStagingListByPOrderNumber(orderNumber);
    }

    /**
     * 提前还款
     */
    public void adjRepayment(String orderNumber) {
        List<BStagingList> bStagingLists = selectBStagingListByPOrderNumber(orderNumber);
        for (BStagingList bsl : bStagingLists) {
            repayment(bsl.getId());
        }
    }

    /**
     * 用户还款
     */
    public void repayment(Integer id) {
        BStagingList bStagingList = this.baseMapper.selectById(id);
        // 更新这一期的还款情况
        bStagingList.setStatus(REPAYMENT);
        // 设置这一期的还款时间
        bStagingList.setRepaymentTime(DateUtil.getCurrentDate());
        // 收款人
        bStagingList.setPayee(ShiroKit.getUserNotNull().getName());
        this.baseMapper.updateById(bStagingList);

        // 当剩余应还的金额，与本期还款金额一致时，默认为最后一期，即还完就还清欠款的意思
        if (bStagingList.getEveryDayRefund().equals(bStagingList.getSurplusRefund())) {
            BOrderDetails bOrderDetails = this.bOrderDetailsService.selectBOrderDetailsByUserId(bStagingList.getUserId());
            // 订单完结 还清状态为4
            bOrderDetails.setStatus(4);
            this.bOrderDetailsService.updateById(bOrderDetails);
        }

        // 根据订单号批量更新其他的账单
        // 计算剩余应还金额，应还金额应当更新到该用户的账单中
        BigDecimal bd = bStagingList.getSurplusRefund().subtract(bStagingList.getEveryDayRefund());
        this.baseMapper.updateSurplusRefundByOrderNumber(bStagingList.getPOrderNumber(), bd);
    }

    /**
     * 用户逾期
     */
    public void overdue(Integer id) {
        BStagingList bStagingList = this.baseMapper.selectById(id);
        // 更新这一期的还款情况
        bStagingList.setStatus(OVERDUE);
        // 设置这一期的还款时间
        bStagingList.setRepaymentTime(DateUtil.getCurrentDate());
        // 收款人
        bStagingList.setPayee(ShiroKit.getUserNotNull().getName());
        this.baseMapper.updateById(bStagingList);

        // 记录该用户的借款信息中累加一次逾期记录
        BOrderDetails bOrderDetails = this.bOrderDetailsService.selectBOrderDetailsByUserId(bStagingList.getUserId());
        bOrderDetails.setOverdueNumber(bOrderDetails.getOverdueNumber() + 1);
        this.bOrderDetailsService.updateById(bOrderDetails);
    }
}
