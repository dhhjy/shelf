package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.util.DecimalUtil;

import java.util.List;
import java.util.Map;

/**
 * @ClassName XinYanWrapper
 * @Description TODO
 * @Author ken
 * @Date 2019/7/13 17:58
 * @Version 1.0
 */
public class XinYanWrapper extends BaseControllerWrapper {
    public XinYanWrapper(Map<String, Object> single) {
        super(single);
    }

    public XinYanWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public XinYanWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public XinYanWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
    }


}
