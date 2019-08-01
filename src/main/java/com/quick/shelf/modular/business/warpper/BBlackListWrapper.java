package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DecimalUtil;

import java.util.List;
import java.util.Map;

public class BBlackListWrapper extends BaseControllerWrapper {
    public BBlackListWrapper(Map<String, Object> single) {
        super(single);
    }

    public BBlackListWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public BBlackListWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public BBlackListWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        // 当不为总公司或管理员的时候，进行数据脱敏
        if (!ShiroKit.isAdmin() && !ShiroKit.isDeptAdmin()) {
            map.put("userAccount", getUserAccount(String.valueOf(map.get("userAccount"))));
        }
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
    }

    /**
     * 脱敏账号
     *
     * @param phoneNumber
     * @return
     */
    private static String getUserAccount(String phoneNumber) {
        if (phoneNumber.length() > 10) {
            return phoneNumber.substring(0, 3) + "*****" + phoneNumber.substring(8);
        } else {
            return phoneNumber;
        }
    }
}
