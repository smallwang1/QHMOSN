package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnNavMenu;

import java.util.List;

public interface MosnNavMenuMapper {

    /**
     * 查询导航菜单
     * @param navMenu
     * @return
     */
    List<MosnNavMenu> selectNavMenuList(MosnNavMenu navMenu);

    /**
     * 根据Id查询菜单
     * @param menuId
     * @return
     */
    MosnNavMenu selectNavMenuById(Long menuId);

    /**
     * 校验菜单名称是否唯一
     * @param menuName
     * @param parentId
     * @return
     */
    MosnNavMenu checkMenuNameUnique(String menuName, Long parentId);

    /**
     * 是否存在菜单子节点
     * @param menuId
     * @return
     */
    Integer hasChildByMenuId(Long menuId);

    /**
     * 新增导航菜单
     * @param navMenu
     * @return
     */
    Integer insertNavMenu(MosnNavMenu navMenu);

    /**
     * 修改导航菜单
     * @param navMenu
     * @return
     */
    Integer updateNavMenu(MosnNavMenu navMenu);

    /**
     * 根据Id删除菜单
     * @param menuId
     * @return
     */
    Integer deleteNavMenuById(Long menuId);
}
