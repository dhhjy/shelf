package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.Map;

/**
 * @ClassName BMobileWrapper
 * @Description TODO
 * @Author ken
 * @Date 2019/7/26 17:27
 * @Version 1.0
 */
public class BMobileWrapper extends BaseControllerWrapper {
    public BMobileWrapper(Map<String, Object> single) {
        super(single);
    }

    public BMobileWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public BMobileWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public BMobileWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

        System.out.println(map);
    }
}
