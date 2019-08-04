package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.core.util.GenerateOrderNoUtil;
import com.quick.shelf.modular.business.entity.*;
import com.quick.shelf.modular.business.mapper.BOrderDetailsMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 用户借款订单的业务层
 */
@Service
public class BOrderDetailsService extends ServiceImpl<BOrderDetailsMapper, BOrderDetails> {
    /**
     * 放款状态
     */
    private static final Integer LOAN_STATUS = 2;

    /**
     * 逾期
     */
    private static final String OVERDUE = "overdue";

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BBlackListService bBlackListService;

    @Resource
    private BOrderAnalyzingService bOrderAnalyzingService;

    @Resource
    private BStagingListService bStagingListService;

    /**
     * 客户端用户申请借款
     *
     * @param bOrderDetails
     * @return
     */
    public BOrderDetails loanApplication(BOrderDetails bOrderDetails) {
        ShiroUser user = ShiroKit.getUserNotNull();
        bOrderDetails.setUserId(user.getId().intValue());
        bOrderDetails.setDeptId(user.getDeptId());
        bOrderDetails.setStatus(0);
        bOrderDetails.setOrderNumber(GenerateOrderNoUtil.gens(user.getId()));
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
     * 查询审核、放款、还款、完结、回退、拒绝 列表通用查询方法
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return
     */
    public Page<Map<String, Object>> selectToAuditList(DataScope dataScope, String name, String beginTime, String endTime, Long deptId, Integer status) {
        return selectList(dataScope, name, beginTime, endTime, deptId, status, null);
    }

    /**
     * 查询逾期订单列表方法
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @return
     */
    public Page<Map<String, Object>> selectOverdueList(DataScope dataScope, String name, String beginTime, String endTime, Long deptId, Integer status) {
        return selectList(dataScope, name, beginTime, endTime, deptId, status, OVERDUE);
    }

    /**
     * 列表查询
     *
     * @param dataScope
     * @param name
     * @param beginTime
     * @param endTime
     * @param deptId
     * @param status
     * @param overdue
     * @return
     */
    private Page<Map<String, Object>> selectList(DataScope dataScope, String name, String beginTime, String endTime, Long deptId, Integer status, String overdue) {
        Page page = LayuiPageFactory.defaultPage();
        return this.baseMapper.selectToAuditList(page, dataScope, name, beginTime, endTime, deptId, status, overdue);
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
        if (null != bOrderDetails.getCheckUserId() && null != bOrderDetails.getCheckMessage())
            bOrderDetails.setCheckTime(DateUtil.getCurrentDate());
        if (null != bOrderDetails.getDrawUserId() && null != bOrderDetails.getDrawMessage())
            bOrderDetails.setDrawTime(DateUtil.getCurrentDate());
        this.baseMapper.updateById(bOrderDetails);
        //查询更新后的对象
        bOrderDetails = this.baseMapper.selectById(bOrderDetails.getId());

        // 当为放款操作时，记录放款，并且异步生成还款账单
        if (bOrderDetails.getStatus().equals(BOrderDetailsService.LOAN_STATUS)) {
            // 记录放款
            BOrderDetails finalBOrderDetails = bOrderDetails;
            new Thread(() -> analyze(finalBOrderDetails)).start();
            // 还款账单
            new Thread(() -> createStaging(finalBOrderDetails)).start();
        }
    }

    /**
     * 创建还款账单
     *
     * @param bOrderDetails
     */
    private void createStaging(BOrderDetails bOrderDetails) {
        BStagingList bStagingList = new BStagingList();
        BeanUtils.copyProperties(bOrderDetails, bStagingList, "id", "status");
        for (int x = 0; x < bOrderDetails.getDebtDuration(); x++) {
            bStagingList.setStatus(0);
            if (x == 0)
                bStagingList.setPredictRepaymentTime(DateUtil.getCurrentDate());
            else
                bStagingList.setPredictRepaymentTime(DateUtil.afterDate(DateUtil.getCurrentDate(), x));
            // 订单号
            bStagingList.setPOrderNumber(bOrderDetails.getOrderNumber());
            //剩余应还
            bStagingList.setSurplusRefund(bOrderDetails.getCountRefund());
            // 插入账单记录
            this.bStagingListService.insert(bStagingList);
        }
    }

    /**
     * 记录放款到放款分析表
     *
     * @param bOrderDetails
     */
    private void analyze(BOrderDetails bOrderDetails) {
        bOrderDetails = this.baseMapper.selectById(bOrderDetails.getId());
        Long deptId = bOrderDetails.getDeptId();
        BOrderAnalyzing bOrderAnalyzing = this.bOrderAnalyzingService.selectBOrderAnalyzingByNowDate(deptId);
        /**
         * 为空则该部门今天没有进行过分析，
         * 反之有进行过分析
         */
        // 今日总订单数
        Integer blanketOrder = this.bOrderAnalyzingService.selectToDayBlanketOrder(deptId);
        // 今日下款订单数
        Integer loanOrder = this.bOrderAnalyzingService.selectToDayLoanOrder(deptId);
        // 下款率
        float loanRatio = (float) (loanOrder / blanketOrder * 100);
        if (null == bOrderAnalyzing) {
            bOrderAnalyzing = new BOrderAnalyzing();
            bOrderAnalyzing.setDeptId(deptId);
            // 合同金额
            bOrderAnalyzing.setContractAmount(bOrderDetails.getAmount());
            // 实际下款金额
            bOrderAnalyzing.setLoanAmount(bOrderDetails.getActualAmount());
            // 设置今日总共多少笔订单
            bOrderAnalyzing.setBlanketOrder(blanketOrder);
            // 设置今日下款订单总数
            bOrderAnalyzing.setLoanOrder(loanOrder);
            // 设置放款率
            bOrderAnalyzing.setLoanRatio(new BigDecimal(Float.toString(loanRatio)));
            this.bOrderAnalyzingService.insert(bOrderAnalyzing);
        } else {
            // 合同金额 累加
            bOrderAnalyzing.setContractAmount(bOrderAnalyzing.getContractAmount().add(bOrderDetails.getAmount()));
            // 实际下款金额 累加
            bOrderAnalyzing.setLoanAmount(bOrderAnalyzing.getLoanAmount().add(bOrderDetails.getActualAmount()));
            // 设置今日总共多少笔订单
            bOrderAnalyzing.setBlanketOrder(blanketOrder);
            // 设置今日下款订单总数
            bOrderAnalyzing.setLoanOrder(loanOrder);
            // 设置放款率
            bOrderAnalyzing.setLoanRatio(new BigDecimal(Float.toString(loanRatio)));
            this.bOrderAnalyzingService.update(bOrderAnalyzing);
        }
    }

    /**
     * 拉黑用户
     */
    public void addBlackList(BOrderDetails bOrderDetails) {
        Integer userId = bOrderDetails.getUserId();
        BBlackList bBlackList = new BBlackList();
        bBlackList.setUserId(userId);
        BSysUser bSysUser = this.bSysUserService.selectBSysUserByUserId(userId);
        bBlackList.setUserAccount(bSysUser.getUserAccount());
        bBlackList.setName(bSysUser.getName());
        bBlackList.setDeptId(bSysUser.getDeptId());
        if (null != bOrderDetails.getDrawUserId()) {
            // 设置放款时的拉黑描述
            bBlackList.setDescription(bOrderDetails.getDrawMessage());
        } else if (null != bOrderDetails.getCheckUserId()) {
            // 设置审核时的拉黑描述
            bBlackList.setDescription(bOrderDetails.getCheckMessage());
        }
        // 设置拉黑操作人ID
        bBlackList.setOperateId(ShiroKit.getUserNotNull().getId());
        // 设置拉黑操作人姓名
        bBlackList.setOperate(ShiroKit.getUserNotNull().getName());
        this.bBlackListService.insert(bBlackList);
    }
}
