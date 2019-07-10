package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DecimalUtil;

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

    private static String getPhoneNumber(String phoneNumber) {
        if (phoneNumber.length() > 10) {
            return phoneNumber.substring(0, 3) + "*****" + phoneNumber.substring(8);
        } else {
            return phoneNumber;
        }
    }

    private static String getIdCard(String idCard) {
        if (idCard.length() > 14) {
            return idCard.substring(0, 4) + "********" + idCard.substring(14);
        } else if (("null").equals(idCard)) {
            return "";
        } else {
            return idCard;
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

}
