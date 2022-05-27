package com.piesat.site.datalist.service.impl;

import com.piesat.site.datalist.service.IMenuService;
import com.piesat.site.datalist.service.entity.MenuInfo;
import com.piesat.site.datalist.service.mapper.MenuMapper;
import com.piesat.site.datalist.service.vo.MenuVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("menuService")
public class MenuServiceImpl implements IMenuService {

    @Autowired
    private MenuMapper menuMapper;

    @Override
    public List<MenuInfo> selectMenuList(MenuVo menu) {
        return menuMapper.selectMenuList(menu);
    }
}
