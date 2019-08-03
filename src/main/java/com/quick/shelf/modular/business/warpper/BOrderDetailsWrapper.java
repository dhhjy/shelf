package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.util.DecimalUtil;

import java.util.List;
import java.util.Map;

public class BOrderDetailsWrapper extends BaseControllerWrapper {
    public BOrderDetailsWrapper(Map<String, Object> single) {
        super(single);
    }

    public BOrderDetailsWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public BOrderDetailsWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public BOrderDetailsWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        if (null == map)
            return;
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
        map.put("statusName", getStatusName(map.get("status").toString()));
        map.put("modeOfRepaymentName", getModeOfRepaymentName(map.get("modeOfRepayment").toString()));
        map.put("productCodeName", getProductCodeName(map.get("productCode").toString()));
    }

    /**
     * 获取还款方式
     */
    public static String getModeOfRepaymentName(String modeOfRepayment) {
        if (null == modeOfRepayment || ("").equals(modeOfRepayment))
            return "未选择还款方式";
        if (modeOfRepayment.equals("1"))
            return "每日本息";
        if (modeOfRepayment.equals("2"))
            return "每日利息+服务费";
        if (modeOfRepayment.equals("3"))
            return "到期全额";

        return "";
    }

    /**
     * 获取订单状态
     *
     * @param status
     * @return
     */
    public static String getStatusName(String status) {
        if (null == status || ("").equals(status))
            return "未知";
        if (status.equals("0"))
            return "待审核";
        if (status.equals("1"))
            return "待下款";
        if (status.equals("2"))
            return "待还款";
        if (status.equals("3"))
            return "逾期";
        if (status.equals("4"))
            return "已完结";
        return "";
    }

    public static String getProductCodeName(String productCode) {
        if (null == productCode || ("").equals(productCode))
            return "未知";
        if (productCode.equals("1"))
            return "秒秒贷";
        if (productCode.equals("2"))
            return "苹果ID贷";
        if (productCode.equals("3"))
            return "白条";
        return "";
    }
}
