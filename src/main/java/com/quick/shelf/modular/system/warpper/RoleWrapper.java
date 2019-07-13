package com.quick.shelf.modular.system.warpper;

import com.quick.shelf.core.common.constant.factory.ConstantFactory;
import com.quick.shelf.core.common.node.ZTreeNode;
import com.quick.shelf.core.shiro.ShiroKit;
import com.quick.shelf.core.util.DecimalUtil;
import cn.stylefeng.roses.core.base.warpper.BaseControllerWrapper;
import cn.stylefeng.roses.kernel.model.page.PageResult;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 角色列表的包装类
 *
 * @author zcn
 * @date 2017年2月19日10:59:02
 */
public class RoleWrapper extends BaseControllerWrapper {

    public RoleWrapper(Map<String, Object> single) {
        super(single);
    }

    public RoleWrapper(List<Map<String, Object>> multi) {
        super(multi);
    }

    public RoleWrapper(Page<Map<String, Object>> page) {
        super(page);
    }

    public RoleWrapper(PageResult<Map<String, Object>> pageResult) {
        super(pageResult);
    }

    @Override
    protected void wrapTheMap(Map<String, Object> map) {
        map.put("pName", ConstantFactory.me().getSingleRoleName(DecimalUtil.getLong(map.get("pid"))));
        map.put("deptName", ConstantFactory.me().getDeptName(DecimalUtil.getLong(map.get("deptId"))));
    }

    /**
     * 筛选当前用户的角色
     */
    public static List<ZTreeNode> filtrateRole(List<ZTreeNode> roleZTree) {
        if(ShiroKit.isAdmin())
            return roleZTree;
        List<ZTreeNode> zTree = new ArrayList<>();
        List<Long> roles = Objects.requireNonNull(ShiroKit.getUser()).getRoleList();
        for (ZTreeNode tree : roleZTree) {
            for (Long rs : roles) {
                if (tree.getId().equals(rs)) {
                    zTree.add(tree);
                    break;
                }
            }
        }
        roleZTree.clear();
        roleZTree.addAll(zTree);
        return roleZTree;
    }
}
