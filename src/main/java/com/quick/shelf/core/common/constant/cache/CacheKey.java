package com.quick.shelf.core.common.constant.cache;

/**
 * 缓存标识前缀集合,常用在ConstantFactory类中
 *
 * @author zcn
 * @date 2019/07/09
 */
public interface CacheKey {

    /**
     * 角色名称(多个)
     */
    String ROLES_NAME = "roles_name_";

    /**
     * 角色名称(单个)
     */
    String SINGLE_ROLE_NAME = "single_role_name_";

    /**
     * 角色英文名称
     */
    String SINGLE_ROLE_TIP = "single_role_tip_";

    /**
     * 部门名称
     */
    String DEPT_NAME = "dept_name_";

    /**
     * 部门树缓存
     */
    String DEPT_TREE = "dept_tree";

    /**
     * 角色树缓存
     */
    String ROLE_TREE = "role_tree";

    /**
     * 接口统计
     */
    String PORT_NAME = "port_count_";
}
