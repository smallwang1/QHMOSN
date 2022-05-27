package com.piesat.busiclogic.busic.organMgr.service.impl;

import com.piesat.busiclogic.busic.organMgr.dao.OrganMgrDao;
import com.piesat.busiclogic.busic.organMgr.entity.OrganInfo;
import com.piesat.busiclogic.busic.organMgr.service.OrganMgrService;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OrganMgrServiceImpl implements OrganMgrService {

    @Autowired
    private OrganMgrDao organMgrDao;

    @Override
    public Page<OrganInfo> getOrganList(long currentPage, int pageSize, OrganInfo organInfo) {
        return organMgrDao.getOrganList(currentPage,pageSize,organInfo);
    }

    @Override
    public void addOrgan(OrganInfo organInfo) {
        int i =  organMgrDao.addOrgan(organInfo);
        if(i<=0){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void deleteOrgan(OrganInfo organInfo) {
        if(PublicUtil.isEmpty(organInfo.getId())){
            throw new BusinessException("缺少参数ID");
        }
        organMgrDao.deleteOrgan(organInfo);
    }

    @Override
    public void editOrgan(OrganInfo organInfo) {
        if(PublicUtil.isEmpty(organInfo.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = organMgrDao.editOrgan(organInfo);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public OrganInfo getOrganById(OrganInfo organInfo) {
        return organMgrDao.getOrganById(organInfo);
    }

    @Override
    public List<Map<String, Object>> getOrganTreeData(String pid) {
        if(PublicUtil.isEmpty(pid)){
            pid = "1";
        }
        List<Map<String,Object>> Resultlist = organMgrDao.getOrganTreeData();

        try {
            Resultlist = Misc.recursion(pid,Resultlist);
            return Resultlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
