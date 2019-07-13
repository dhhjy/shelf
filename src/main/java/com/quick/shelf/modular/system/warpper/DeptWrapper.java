package com.quick.shelf.modular.system.warpper;

import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.common.node.ZTreeNode;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DecimalUtil;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 部门列表的包装
 *
 * @author zcn
 * @date 2017年4月25日 18:10:31
 */
public class DeptWrapper extends BaseControllerWrapper {

    public DeptWrapper(Map<String, Object> single) {
        super(single);
    }

    public DeptWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public DeptWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public DeptWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        Long pid = DecimalUtil.getLong(map.get("pid"));

        if (ToolUtil.isEmpty(pid) || pid.equals(0L)) {
            map.put("pName", "--");
        } else {
            map.put("pName", ConstantFactory.me().getDeptName(pid));
        }
    }

    /**
     * 筛选当前单位的列表
     */
    public static void filtrateDept(List<ZTreeNode> zTree){
        List<ZTreeNode> ztree = new ArrayList<>(zTree);
        Long deptId = Objects.requireNonNull(ShiroKit.getUser()).getDeptId();
        if(0 == deptId)
            return;
        for(ZTreeNode zt : ztree){
            if(!zt.getId().equals(deptId) && !zt.getPId().equals(deptId)){
                zTree.remove(zt);
            }
        }
    }
}
