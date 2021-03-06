package com.quick.shelf.modular.system.mapper;

import com.quick.shelf.core.common.node.ZTreeNode;
import com.quick.shelf.modular.system.entity.Dict;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 基础字典 Mapper 接口
 * </p>
 *
 * @author quick
 * @since 2019-03-13
 */
public interface DictMapper extends BaseMapper<Dict> {

    /**
     * 获取ztree的节点列表
     */
    List<ZTreeNode> dictTree(@Param("dictTypeId") Long dictTypeId);

    /**
     * where parentIds like ''
     */
    List<Dict> likeParentIds(@Param("dictId") Long dictId);

    /**
     * 根据字典 的 name 查询
     */
    Dict selectDictByName(@Param("name") String name);

    /**
     * 根据字典 code 键 查询所有对应的子集字典列表
     */
    List<Dict> selectDictByCoede(@Param("code") String code);
}
