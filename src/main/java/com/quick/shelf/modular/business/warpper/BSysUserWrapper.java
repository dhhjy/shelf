package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DecimalUtil;
import com.quick.shelf.modular.business.entity.BBankCardInfo;
import com.quick.shelf.modular.business.entity.BEmergencyContactInfo;
import com.quick.shelf.modular.business.entity.BIdentityInfo;
import com.quick.shelf.modular.business.entity.BSysUser;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BSysUserWrapper
 * @Description TODO
 * @Author ken
 * @Date 2019/7/10 16:12
 * @Version 1.0
 */
public class BSysUserWrapper extends BaseControllerWrapper {

    public BSysUserWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    /**
     * 脱敏手机号
     *
     * @param phoneNumber
     * @return
     */
    private static String getPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() > 10) {
            return phoneNumber.substring(0, 3) + "*****" + phoneNumber.substring(8);
        } else {
            return phoneNumber;
        }
    }

    /**
     * 脱敏身份证
     *
     * @param idCard
     * @return
     */
    private static String getIdCard(String idCard) {
        if (idCard.length() > 14) {
            return idCard.substring(0, 4) + "********" + idCard.substring(14);
        } else if (("null").equals(idCard)) {
            return "";
        } else {
            return idCard;
        }
    }

    /**
     * 脱敏银行卡号
     *
     * @param map
     */
    private static String getCardNumber(String cardNumber) {
        if (cardNumber.length() == 12) {
            return cardNumber.substring(0, 4) + "******" + cardNumber.substring(9);
        } else if (cardNumber.length() == 15) {
            return cardNumber.substring(0, 4) + "*******" + cardNumber.substring(12);
        } else if (cardNumber.length() > 15) {
            return cardNumber.substring(0, 4) + "********" + cardNumber.substring(15);
        } else if (("null").equals(cardNumber)) {
            return "";
        } else {
            return cardNumber;
        }
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        // 当不为总公司或管理员的时候，进行数据脱敏
        if (!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()) {
            map.put("phoneNumber", getPhoneNumber(String.valueOf(map.get("phoneNumber"))));
            map.put("idCard", getIdCard(String.valueOf(map.get("idCard"))));
        }
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
        map.put("statusName", ConstantFactory.me().getStatusName((String) map.get("status")));
    }

    /**
     * 单条数据脱敏
     */
    public static BSysUser deSensitization(BSysUser bSysUser) {
        // 当不为总公司或管理员的时候，进行数据脱敏
        if (!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()) {
            bSysUser.setPhoneNumber(getPhoneNumber(bSysUser.getPhoneNumber()));
            bSysUser.setIdCard(getIdCard(bSysUser.getIdCard()));
        }
        return bSysUser;
    }

    /**
     * 紧急联系人中的电话进行脱敏
     */
    public static BEmergencyContactInfo deSensitization(BEmergencyContactInfo bEmergencyContactInfo) {
        if(bEmergencyContactInfo == null)
            return null;

        // 当不为总公司或管理员的时候，进行数据脱敏
        if (!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()) {
            bEmergencyContactInfo.setRelativePhone1(getPhoneNumber(bEmergencyContactInfo.getRelativePhone1()));
            bEmergencyContactInfo.setRelativePhone2(getPhoneNumber(bEmergencyContactInfo.getRelativePhone2()));
            bEmergencyContactInfo.setSocialPhone(getPhoneNumber(bEmergencyContactInfo.getSocialPhone()));
        }
        return bEmergencyContactInfo;
    }

    /**
     * 认证身份证信息中的身份证号码进行脱敏
     */
    public static BIdentityInfo deSensitization(BIdentityInfo bIdentityInfo) {
        if(bIdentityInfo == null)
            return null;

        // 当不为总公司或管理员的时候，进行数据脱敏
        if (!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()) {
            bIdentityInfo.setIdentityNumber(getIdCard(bIdentityInfo.getIdentityNumber()));
        }
        return bIdentityInfo;
    }

    /**
     * 认证的银行卡信息脱敏
     */
    public static List<BBankCardInfo> deSensitization(List<BBankCardInfo> bBankCardInfos) {
        if(bBankCardInfos.size() < 1)
            return bBankCardInfos;

        // 当不为总公司或管理员的时候，进行数据脱敏
        if (!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()) {
            for (BBankCardInfo bbci : bBankCardInfos) {
                bbci.setCardNumber(getCardNumber(bbci.getCardNumber()));
                bbci.setPhoneNumber(getPhoneNumber(bbci.getPhoneNumber()));
            }
        }
        return bBankCardInfos;
    }
}
