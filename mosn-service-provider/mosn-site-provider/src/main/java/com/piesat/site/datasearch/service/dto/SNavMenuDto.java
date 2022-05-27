package com.piesat.site.datasearch.service.dto;

import com.piesat.site.datasearch.service.entity.MosnNavMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SNavMenuDto {

    private Long menuId;
    private Long parentId;
    private String menuName;
    private String resourceArr;
    private String menuType;
    private String menuIcon;

    public SNavMenuDto(MosnNavMenu mosnNavMenu) {
        this.menuId = mosnNavMenu.getMenuId();
        this.parentId = mosnNavMenu.getParentId();
        this.menuName = mosnNavMenu.getMenuName();
        this.resourceArr = mosnNavMenu.getResourceArr();
        this.menuType = mosnNavMenu.getMenuType();
        this.menuIcon = mosnNavMenu.getMenuIcon();
    }
}
