package com.piesat.busiclogic.busic.menuset.dao;

import com.piesat.busiclogic.busic.menuset.entity.MenuInfo;
import com.piesat.jdbc.Page;

import java.util.List;
import java.util.Map;

public interface MenuMgrDao {
    Page<MenuInfo> getMenuData(long currentPage, int pageSize, MenuInfo menuInfo);

    int addMenuInfo(MenuInfo menuInfo);

    void deleteMenuInfo(MenuInfo menuInfo);

    int editStation(MenuInfo menuInfo);

    MenuInfo getMenuInfoById(MenuInfo menuInfo);

    List<Map<String, Object>> getMenuSetByCode(String code);

    List<Map<String, Object>> getMenuHead();

    List<Map<String, Object>> getTheLowSortList(String pid, String sort, String flag);

    void updateSort(String sort, String id);
}
