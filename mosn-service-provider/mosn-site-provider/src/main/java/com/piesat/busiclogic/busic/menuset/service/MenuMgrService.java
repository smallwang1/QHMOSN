package com.piesat.busiclogic.busic.menuset.service;

import com.piesat.busiclogic.busic.menuset.entity.MenuInfo;
import com.piesat.jdbc.Page;

import java.util.List;
import java.util.Map;

public interface MenuMgrService {
    public Page<MenuInfo> getMenuData(long currentPage, int pageSize, MenuInfo menuInfo);

    void addMenuInfo(MenuInfo menuInfo);

    void deleteMenuInfo(MenuInfo menuInfo);

    void editMenuInfo(MenuInfo menuInfo);

    MenuInfo getMenuById(MenuInfo menuInfo);

    List<Map<String, Object>> getMenuSetDataByTree(String code);

    List<Map<String, Object>> getMenuHead();

    void moveMenu(String id, String targetid, String flag);
}
