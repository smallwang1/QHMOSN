package com.piesat.busiclogic.busic.userMgr.service;

import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.busiclogic.busic.userMgr.entity.UserInfo;
import com.piesat.jdbc.Page;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {

    public Page<UserInfo> getUserData(long currentPage, int pageSize, UserInfo userInfo);

    void addUser(UserInfo userInfo) throws IOException;

    void deleteUser(UserInfo userInfo);

    void editUser(UserInfo userInfo) throws IOException;

    UserInfo getUserById(UserInfo userInfo);

    List<Map<String, Object>> getOrganTreeData();

    void resetPwd(UserInfo userInfo);

    void changeStatus(UserInfo userInfo);
}
