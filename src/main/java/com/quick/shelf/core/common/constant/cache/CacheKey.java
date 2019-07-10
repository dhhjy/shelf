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
     * 性别名称
     */
    String SEX_NAME = "sex_name_";

    /**
     * 常用部门树缓存
     */
    String DEPT_TREE_NAME = "dept_tree";
}
