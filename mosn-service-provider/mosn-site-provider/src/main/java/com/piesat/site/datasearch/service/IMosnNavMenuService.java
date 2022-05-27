package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.FNavMenuDto;
import com.piesat.site.datasearch.service.entity.MosnNavMenu;

import java.util.List;

public interface IMosnNavMenuService {

    /**
     * 按条件分页查找导航菜单
     * @param navMenu
     * @return
     */
    List<FNavMenuDto> selectNavMenuList(MosnNavMenu navMenu);

    /**
     * 根据菜单Id查询产品集合
     * @param menuId
     * @return
     */
    MosnNavMenu selectNavMenuById(Long menuId);

    /**
     *校验菜单名称是否唯一
     * @param navMenu
     * @return
     */
    String checkMenuNameUnique(MosnNavMenu navMenu);

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
     * 是否存在菜单子节点
     * @param menuId
     * @return
     */
    boolean hasChildByMenuId(Long menuId);

    /**
     * 根据Id删除菜单
     * @param menuId
     * @return
     */
    Integer deleteNavMenuById(Long menuId);
}
