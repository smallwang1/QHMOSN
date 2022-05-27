package com.piesat.busiclogic.busic.roleMgr.service.impl;

import com.piesat.busiclogic.busic.roleMgr.dao.RoleMgrDao;
import com.piesat.busiclogic.busic.roleMgr.entity.MenuRole;
import com.piesat.busiclogic.busic.roleMgr.entity.ProductRole;
import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.busiclogic.busic.roleMgr.service.RoleMgrService;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.Page;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class RoleMgrServiceImpl implements RoleMgrService {

    @Autowired
    private RoleMgrDao roleMgrDao;

    @Override
    public Page<RoleMgr> getRoleData(long currentPage, int pageSize, RoleMgr roleMgr) {
        return roleMgrDao.getRoleData(currentPage,pageSize,roleMgr);
    }

    @Override
    @Transactional
    public void addRoleInfo(RoleMgr roleMgr) throws IOException {
        String id =  roleMgrDao.addRoleInfo(roleMgr);
        if(PublicUtil.isEmpty(id)){
            throw new BusinessException("新增失败");
        }
        if(!PublicUtil.isEmpty(roleMgr.getMenuRoleList())){// 添加菜单权限
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<MenuRole> menuRoleList = mapper.readValue(roleMgr.getMenuRoleList(),new TypeReference<List<MenuRole>>() { });
            roleMgrDao.addMeunRole(menuRoleList,id);
        }

        if(!PublicUtil.isEmpty(roleMgr.getProductRole())){ //添加产品权限
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<ProductRole> productRoles = mapper.readValue(roleMgr.getProductRole(),new TypeReference<List<ProductRole>>() { });
            roleMgrDao.addProductRole(productRoles,id);
        }
    }

    @Override
    public void deleteRoleInfo(RoleMgr roleMgr) {
        if(PublicUtil.isEmpty(roleMgr.getId())){
            throw new BusinessException("缺少参数id");
        }
        roleMgrDao.deleteRoleInfo(roleMgr);
    }

    @Override
    @Transactional
    public void editRole(RoleMgr roleMgr) throws IOException {
        if(PublicUtil.isEmpty(roleMgr.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = roleMgrDao.editRole(roleMgr);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
        if(!PublicUtil.isEmpty(roleMgr.getMenuRoleList())){ // 编辑菜单权限
            roleMgrDao.deleteMenuRole(roleMgr.getId());
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<MenuRole> menuRoleList = mapper.readValue(roleMgr.getMenuRoleList(),new TypeReference<List<MenuRole>>() { });
            roleMgrDao.addMeunRole(menuRoleList,roleMgr.getId());
        } else {
            roleMgrDao.deleteMenuRole(roleMgr.getId());
        }

        if(!PublicUtil.isEmpty(roleMgr.getProductRole())){ //编辑产品权限
            roleMgrDao.deleteProductRole(roleMgr.getId());
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<ProductRole> productRoles = mapper.readValue(roleMgr.getProductRole(),new TypeReference<List<ProductRole>>() { });
            roleMgrDao.addProductRole(productRoles,roleMgr.getId());
        }else {
            roleMgrDao.deleteProductRole(roleMgr.getId());
        }
    }

    @Override
    public RoleMgr getRoleById(RoleMgr roleMgr) {
        RoleMgr roleMgr1 = roleMgrDao.getRoleById(roleMgr);
        if(!PublicUtil.isEmpty(roleMgr1)){ // 添加菜单、产品的权限信息
            roleMgr1.setMenuRoleBack(roleMgrDao.getMenuRole(roleMgr.getId()));
            roleMgr1.setProductRoleBack(roleMgrDao.getProductRole(roleMgr.getId()));
        }
       return roleMgr1;
    }

    @Override
    public List<Map<String, Object>> getAllRole(RoleMgr roleMgr) {
        return roleMgrDao.getAllRole(roleMgr);
    }
}
