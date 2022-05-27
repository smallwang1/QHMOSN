package com.piesat.site.datasearch.service.dto;

import com.piesat.site.datasearch.service.entity.MosnNavMenu;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class FNavMenuDto {

    private Long menuId;
    private String menuName;
    private String menuType;
    private String menuIcon;
    private List<SNavMenuDto> navMenuDtoList;

    public FNavMenuDto(MosnNavMenu mosnNavMenu) {
        this.menuId = mosnNavMenu.getMenuId();
        this.menuName = mosnNavMenu.getMenuName();
        this.menuType = mosnNavMenu.getMenuType();
        this.menuIcon = mosnNavMenu.getMenuIcon();
    }
}
