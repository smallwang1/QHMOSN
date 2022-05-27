package com.piesat.busiclogic.busic.userMgr.service.impl;

import com.piesat.busiclogic.busic.userMgr.dao.UserDao;
import com.piesat.busiclogic.busic.userMgr.entity.User2Role;
import com.piesat.busiclogic.busic.userMgr.entity.UserInfo;
import com.piesat.busiclogic.busic.userMgr.service.UserService;
import com.piesat.busiclogic.common.Util.Misc;
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
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public Page<UserInfo> getUserData(long currentPage, int pageSize, UserInfo userInfo) {
        return userDao.getUserData(currentPage,pageSize,userInfo);
    }

    @Override
    @Transactional
    public void addUser(UserInfo userInfo) throws IOException {
        String id =  userDao.addUser(userInfo);
        if(PublicUtil.isEmpty(id)){
            throw new BusinessException("新增失败");
        }
        if(!PublicUtil.isEmpty(userInfo.getRoleMgrList())){
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<User2Role> user2RoleList = mapper.readValue(userInfo.getRoleMgrList(),new TypeReference<List<User2Role>>() { });
            userDao.adduser2role(user2RoleList,id);
        }
    }

    @Override
    public void deleteUser(UserInfo userInfo) {
        if(PublicUtil.isEmpty(userInfo.getId())){
            throw new BusinessException("缺少参数id");
        }
        userDao.deleteUser(userInfo);
    }

    @Override
    @Transactional
    public void editUser(UserInfo userInfo) throws IOException {
        if(PublicUtil.isEmpty(userInfo.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = userDao.editUser(userInfo);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
        if(!PublicUtil.isEmpty(userInfo.getRoleMgrList())){
            userDao.deleteUserRole(userInfo.getId());
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<User2Role> user2RoleList = mapper.readValue(userInfo.getRoleMgrList(),new TypeReference<List<User2Role>>() { });
            userDao.adduser2role(user2RoleList,userInfo.getId());
        }else{
            userDao.deleteUserRole(userInfo.getId());
        }
    }

    @Override
    public UserInfo getUserById(UserInfo userInfo) {
        UserInfo userInfo1 = userDao.getUserById(userInfo);
        if(!PublicUtil.isEmpty(userInfo1)){
            userInfo1.setRoleMgrBack(userDao.getRoleList(userInfo.getId()));
        }
        return userInfo1;
    }

    @Override
    public List<Map<String, Object>> getOrganTreeData() {
        List<Map<String,Object>> Resultlist = userDao.getOrganTreeData();
        try {
            Resultlist = Misc.recursion("1",Resultlist);
            return Resultlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void resetPwd(UserInfo userInfo) {
        userDao.resetPwd(userInfo);
    }

    @Override
    public void changeStatus(UserInfo userInfo) {
         int i = userDao.changeStatus(userInfo);
    }
}
