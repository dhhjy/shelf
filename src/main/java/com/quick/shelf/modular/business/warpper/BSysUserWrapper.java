package com.quick.shelf.modular.business.warpper;

import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

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

    @Override
    protected void wrapTheMap(Map<String, Object> map) {

    }
}
