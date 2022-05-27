package com.piesat.busiclogic.busic.organMgr.service;

import com.piesat.busiclogic.busic.organMgr.entity.OrganInfo;
import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.jdbc.Page;

import java.util.List;
import java.util.Map;

public interface OrganMgrService {
    public Page<OrganInfo> getOrganList(long currentPage, int pageSize, OrganInfo organInfo);

    void addOrgan(OrganInfo organInfo);

    void deleteOrgan(OrganInfo organInfo);

    void editOrgan(OrganInfo organInfo);

    OrganInfo getOrganById(OrganInfo organInfo);

    List<Map<String, Object>> getOrganTreeData(String pid);

}
