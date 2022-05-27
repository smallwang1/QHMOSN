package com.piesat.busiclogic.busic.roleMgr.dao;

import com.piesat.busiclogic.busic.roleMgr.entity.MenuRole;
import com.piesat.busiclogic.busic.roleMgr.entity.ProductRole;
import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.jdbc.Page;

import java.util.List;
import java.util.Map;

public interface RoleMgrDao {


    Page<RoleMgr> getRoleData(long currentPage, int pageSize, RoleMgr roleMgr);

    String addRoleInfo(RoleMgr roleMgr);

    void deleteRoleInfo(RoleMgr roleMgr);

    int editRole(RoleMgr roleMgr);

    RoleMgr getRoleById(RoleMgr roleMgr);

    List<String> getRoleIdByUserid(String userid);

    List<Map<String, Object>> getAllRole(RoleMgr roleMgr);

    void addMeunRole(List<MenuRole> menuRoleList,String id);

    List<MenuRole> getMenuRole(String id);

    void deleteMenuRole(String id);

    void addProductRole(List<ProductRole> productRoles, String id);

    void deleteProductRole(String id);

    List<ProductRole> getProductRole(String id);
}
