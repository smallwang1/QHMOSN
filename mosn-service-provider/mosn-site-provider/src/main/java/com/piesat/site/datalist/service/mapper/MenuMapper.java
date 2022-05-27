package com.piesat.site.datalist.service.mapper;

import com.piesat.site.datalist.service.entity.MenuInfo;
import com.piesat.site.datalist.service.vo.MenuVo;

import java.util.List;

public interface MenuMapper {

    List<MenuInfo> selectMenuList(MenuVo menu);
}
