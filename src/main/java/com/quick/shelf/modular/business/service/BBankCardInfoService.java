package com.quick.shelf.modular.business.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.quick.shelf.core.util.DateUtil;
import com.quick.shelf.core.util.GenerateOrderNoUtil;
import com.quick.shelf.modular.business.entity.BBankCardInfo;
import com.quick.shelf.modular.business.entity.BSysUserStatus;
import com.quick.shelf.modular.business.mapper.BBankCardInfoMapper;
import com.quick.shelf.modular.constant.BusinessConst;
import com.quick.shelf.modular.creditPort.quickMoney.QuickMoneyConstartMethod;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.QuickMoneySignEntity;
import com.quick.shelf.modular.creditPort.quickMoney.bill99.entity.QuickMoneySignVerifyEntity;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 用户关联的银行卡业务层
 * 主要操作 'b_bank_card_info' 表
 *
 * @author zcn
 * @date 2019/07/11
 */
@Service
public class BBankCardInfoService extends ServiceImpl<BBankCardInfoMapper, BBankCardInfo> {

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(BBankCardInfoService.class);

    @Resource
    private BSysUserStatusService bSysUserStatusService;

    /**
     * 根据用户主键获取用户绑定的银行信息
     * 用户与银行卡信息的对应关系为一对多
     *
     * @param userId
     * @return List<BBankCardInfo>
     */
    public List<BBankCardInfo> selectBBankCardInfosByUserId(Integer userId) {
        return this.baseMapper.selectBBankCardInfosByUserId(userId);
    }

    /**
     * 根据用户主键id获取用户的最后绑定的一张银行卡
     * 用户与银行卡的关系为一对多，次方法只会查询一张
     *
     * @param userId
     * @return BBankCardInfo
     */
    public BBankCardInfo getBBankCardInfosByUserId(Integer userId) {
        return this.baseMapper.getBBankCardInfosByUserId(userId);
    }

    private void insertBBankCardInfo(BBankCardInfo bBankCardInfo) {
        bBankCardInfo.setCreateTime(DateUtil.getCurrentDate());
        this.baseMapper.insert(bBankCardInfo);
    }

    /**
     * H5客户端用户银行卡绑定签约申请
     * 执行此方法后，会提交用户的 银行卡号
     * 姓名、手机号、信息到快钱申请进行签约
     * 发送成功返回的 responseCode 为 ‘00’则
     * 标识成功，此时会下发验证码到客户的手机上
     *
     * @param userid               用户主键
     * @param quickMoneySignEntity 快钱签约申请对象
     */
    public String bankAuth(Integer userId, QuickMoneySignEntity quickMoneySignEntity) {
        logger.info("用户:{} 发送快钱银行卡签约申请", userId);
        // 设置订单编号
        quickMoneySignEntity.setExternalRefNumber(GenerateOrderNoUtil.gens(Long.valueOf(userId)));
        //TR2接收的数据
        HashMap respXml = QuickMoneyConstartMethod.bankAuth(quickMoneySignEntity);
        assert respXml != null;
        JSONObject json = JSONObject.fromObject(respXml);
        logger.info("用户：{} 申请协议签约的返回结果为：{}", userId, json.toString());
        // 成功的数据会被缓存到redis 缓存，因为后面会使用到
        return json.toString();
    }

    /**
     * H5客户签约银行卡申请提交成功后，接收到的验证码
     * 将通过本方法，传给快钱,完成银行卡签约的操作
     * 同时，签约成功后，保存永辉银行卡的信息，
     */
    public String bankAuthVerify(Integer userId, QuickMoneySignVerifyEntity quickMoneySignVerifyEntity) {
        logger.info("用户:{} 接收到的快钱签约验证码为:{}", userId, quickMoneySignVerifyEntity.getValidCode());
        HashMap respXml = QuickMoneyConstartMethod.bankAuthVerify(quickMoneySignVerifyEntity);
        assert respXml != null;
        JSONObject json = JSONObject.fromObject(respXml);

        // 如果返回的结果成功则保存用户的银行卡信息
        // 银行卡信息为一对多
        if (json.getString("responseCode").equals(QuickMoneyConstartMethod.SUCCESS_CODE)) {
            BBankCardInfo bBankCardInfo = new BBankCardInfo();
            bBankCardInfo.setUserId(userId);
            bBankCardInfo.setBankType(quickMoneySignVerifyEntity.getBankType());
            bBankCardInfo.setBankName(quickMoneySignVerifyEntity.getBankName());
            bBankCardInfo.setBankArea(quickMoneySignVerifyEntity.getBankArea());
            bBankCardInfo.setCardNumber(quickMoneySignVerifyEntity.getPan());
            bBankCardInfo.setPhoneNumber(quickMoneySignVerifyEntity.getPhoneNO());
            bBankCardInfo.setSignStatus(Integer.valueOf(BusinessConst.OK));
            bBankCardInfo.setNoAgree(json.getString("payToken"));
            bBankCardInfo.setStorablePan(json.getString("storablePan"));
            this.insertBBankCardInfo(bBankCardInfo);

            // 更新用户银行卡的认证状态
            BSysUserStatus bSysUserStatus = new BSysUserStatus();
            bSysUserStatus.setUserId(userId);
            bSysUserStatus.setBankInfoStatus(BusinessConst.OK);
            this.bSysUserStatusService.updateByUserId(bSysUserStatus);
        }

        logger.info("用户：{} 认证返回的结果为：{}", userId, json.toString());
        return json.toString();
    }
}
