package com.piesat.busiclogic.busic.userMgr.dao;

import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.busiclogic.busic.userMgr.entity.User2Role;
import com.piesat.busiclogic.busic.userMgr.entity.UserInfo;
import com.piesat.jdbc.Page;

import java.util.List;
import java.util.Map;

public interface UserDao {
    Page<UserInfo> getUserData(long currentPage, int pageSize, UserInfo userInfo);

    String addUser(UserInfo userInfo);

    void deleteUser(UserInfo userInfo);

    void deleteUserRole(String userid);

    int editUser(UserInfo userInfo);

    UserInfo getUserById(UserInfo userInfo);

    List<Map<String, Object>> getOrganTreeData();

    int resetPwd(UserInfo userInfo);

    List<User2Role> getRoleList(String id);

    void adduser2role(List<User2Role> roleMgrList, String id);

    int changeStatus(UserInfo userInfo);
}
