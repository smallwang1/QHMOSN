package com.piesat.busiclogic.busic.roleMgr.service;

import com.piesat.busiclogic.busic.menuset.entity.MenuInfo;
import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.jdbc.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface RoleMgrService {
    public Page<RoleMgr> getRoleData(long currentPage, int pageSize, RoleMgr roleMgr);

    void addRoleInfo(RoleMgr roleMgr) throws IOException;

    void deleteRoleInfo(RoleMgr roleMgr);

    void editRole(RoleMgr roleMgr) throws IOException;

    RoleMgr getRoleById(RoleMgr roleMgr);

    List<Map<String,Object>> getAllRole(RoleMgr roleMgr);
}
