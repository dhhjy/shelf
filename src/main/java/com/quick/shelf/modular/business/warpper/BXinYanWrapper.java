package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DecimalUtil;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BXinYanWrapper
 * @Description TODO
 * @Author ken
 * @Date 2019/7/13 17:58
 * @Version 1.0
 */
public class BXinYanWrapper extends BaseControllerWrapper {
    public BXinYanWrapper(Map<String, Object> single) {
        super(single);
    }

    public BXinYanWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public BXinYanWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public BXinYanWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
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

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
        if(!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()){
            map.put("phoneNumber", getPhoneNumber(String.valueOf(map.get("phoneNumber"))));
            map.put("idCard", getIdCard(String.valueOf(map.get("idCard"))));
        }
    }
}
