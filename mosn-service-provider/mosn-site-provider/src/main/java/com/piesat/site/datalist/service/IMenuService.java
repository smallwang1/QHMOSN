package com.piesat.site.datalist.service;

import com.piesat.site.datalist.service.entity.MenuInfo;
import com.piesat.site.datalist.service.vo.MenuVo;

import java.util.List;

public interface IMenuService {

    List<MenuInfo> selectMenuList(MenuVo menu);
}
