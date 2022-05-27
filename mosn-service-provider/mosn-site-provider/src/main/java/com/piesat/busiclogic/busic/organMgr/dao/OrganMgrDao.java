package com.piesat.busiclogic.busic.organMgr.dao;

import com.piesat.busiclogic.busic.organMgr.entity.OrganInfo;
import com.piesat.jdbc.Page;

import java.util.List;
import java.util.Map;

public interface OrganMgrDao {
    Page<OrganInfo> getOrganList(long currentPage, int pageSize, OrganInfo organInfo);

    int addOrgan(OrganInfo organInfo);

    void deleteOrgan(OrganInfo organInfo);

    int editOrgan(OrganInfo organInfo);

    OrganInfo getOrganById(OrganInfo organInfo);

    List<Map<String, Object>> getOrganTreeData();
}
