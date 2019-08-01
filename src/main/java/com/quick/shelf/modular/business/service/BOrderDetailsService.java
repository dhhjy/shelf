package com.quick.shelf.modular.business.service;

import cn.stylefeng.roses.core.datascope.DataScope;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.common.page.LayuiPageFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.shiro.ShiroUser;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.modular.business.entity.BBlackList;
import com.quick.shelf.modular.business.entity.BOrderAnalyzing;
import com.quick.shelf.modular.business.entity.BOrderDetails;
import com.quick.shelf.modular.business.entity.BSysUser;
import com.quick.shelf.modular.business.mapper.BOrderDetailsMapper;
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

    @Resource
    private BSysUserService bSysUserService;

    @Resource
    private BBlackListService bBlackListService;

    @Resource
    private BOrderAnalyzingService bOrderAnalyzingService;

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
        if (null != bOrderDetails.getCheckUserId() && null != bOrderDetails.getCheckMessage())
            bOrderDetails.setCheckTime(DateUtil.getCurrentDate());
        if (null != bOrderDetails.getDrawUserId() && null != bOrderDetails.getDrawMessage())
            bOrderDetails.setDrawTime(DateUtil.getCurrentDate());
        this.baseMapper.updateById(bOrderDetails);

        // 当为放款操作时，记录放款
        if (bOrderDetails.getStatus().equals(BOrderDetailsService.LOAN_STATUS)) {
            new Thread(() -> {
                analyze(bOrderDetails);
            }).start();
        }
    }

    private void analyze(BOrderDetails bOrderDetails) {
        Long deptId = ShiroKit.getUserNotNull().getDeptId();
        BOrderAnalyzing bOrderAnalyzing = this.bOrderAnalyzingService.selectBOrderAnalyzingByNowDate(deptId);
        bOrderDetails = this.baseMapper.selectById(bOrderDetails.getId());
        /**
         * 为空则该部门今天没有进行过分析，
         * 反之有进行过分析
         */
        if (null == bOrderAnalyzing) {
            bOrderAnalyzing = new BOrderAnalyzing();
            bOrderAnalyzing.setDeptId(deptId);
            // 合同金额
            bOrderAnalyzing.setContractAmount(bOrderDetails.getAmount());
            // 实际下款金额
            bOrderAnalyzing.setLoanAmount(bOrderDetails.getActualAmount());
            bOrderAnalyzing.setCreateDate(DateUtil.getCurrentDate());
            // 设置放款率
            bOrderAnalyzing.setLoanRatio(new BigDecimal("100"));
            this.bOrderAnalyzingService.insert(bOrderAnalyzing);
        } else {
            // 合同金额 累加
            bOrderAnalyzing.setContractAmount(bOrderAnalyzing.getContractAmount().add(bOrderDetails.getAmount()));
            // 实际下款金额
            bOrderAnalyzing.setLoanAmount(bOrderAnalyzing.getLoanAmount().add(bOrderDetails.getActualAmount()));
            // 设置放款率
            bOrderAnalyzing.setLoanRatio(new BigDecimal(this.bOrderAnalyzingService.selectLendingRate(deptId)));
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
