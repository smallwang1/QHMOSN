package com.piesat.busiclogic.busic.userMgr.dao.impl;

import com.piesat.busiclogic.busic.userMgr.dao.UserDao;
import com.piesat.busiclogic.busic.userMgr.entity.User2Role;
import com.piesat.busiclogic.busic.userMgr.entity.UserInfo;
import com.piesat.common.util.EncryptUtil;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public Page<UserInfo> getUserData(long currentPage, int pageSize, UserInfo userInfo) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select SYS_USER.*,SYS_ORG.ORGAN_NAME from ");
        sb.append("    SYS_USER left join SYS_ORG on SYS_USER.ORG_ID = SYS_ORG.ID ");
        sb.append("    where 1=1 ");

        Map<String,Object> map = new HashMap<>();
        if(!PublicUtil.isEmpty(userInfo.getUserName())){
            sb.append(" and USERNAME like '%'+:USERNAME+'%' ");
            map.put("USERNAME",userInfo.getUserName());
        }
        if(!PublicUtil.isEmpty(userInfo.getOrg_id())){
            sb.append(" and ORG_ID =:ORG_ID");
            map.put("ORG_ID",userInfo.getOrg_id());
        }
        if(!PublicUtil.isEmpty(userInfo.getIs_lock())){
            sb.append(" and IS_LOCK =:IS_LOCK");
            map.put("IS_LOCK",userInfo.getIs_lock());
        }
        if(!PublicUtil.isEmpty(userInfo.getUser_type())){
            sb.append(" and USER_TYPE =:USER_TYPE");
            map.put("USER_TYPE",userInfo.getUser_type());
        }
        sb.append(" order by create_date desc");
        return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(UserInfo.class),currentPage,pageSize);
    }

    @Override
    public String addUser(UserInfo userInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into SYS_USER");
        sb.append(" (");
        sb.append("USERNAME,\n");
        sb.append("USER_TYPE,\n");
        sb.append("SALT,\n");
        sb.append("PASSWORD,\n");
        sb.append("AUTH_STATUS,\n");
        sb.append("IS_LOCK,\n");
        sb.append("NICKNAME,\n");
        sb.append("CREATE_DATE,\n");
        sb.append("AUTH_TYPE,\n");
        sb.append("ORG_ID\n");
        sb.append(" ) values (");
        sb.append(":USERNAME,\n");
        sb.append(":USER_TYPE,\n");
        sb.append(":SALT,\n");
        sb.append(":PASSWORD,\n");
        sb.append(":AUTH_STATUS,\n");
        sb.append(":IS_LOCK,\n");
        sb.append(":NICKNAME,\n");
        sb.append(":CREATE_DATE,\n");
        sb.append(":AUTH_TYPE,\n");
        sb.append(":ORG_ID) \n");

        Map<String,Object> map = new HashMap<>();
        String id = UUID.randomUUID().toString();
        map.put("USERNAME", userInfo.getUserName());
        map.put("USER_TYPE", userInfo.getUser_type());
        String salt = EncryptUtil.getRandomSalt();
        map.put("SALT",salt);
        map.put("PASSWORD", EncryptUtil.encrypt(userInfo.getPassWord(), salt, EncryptUtil.HASH1, EncryptUtil.HASH_ITERATIONS));
        map.put("AUTH_STATUS", userInfo.getAuth_status());
        map.put("IS_LOCK", userInfo.getIs_lock());
        map.put("NICKNAME",userInfo.getNickName());
//        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        map.put("CREATE_DATE",System.currentTimeMillis());
        map.put("AUTH_TYPE",userInfo.getAuth_type());
        map.put("ORG_ID",userInfo.getOrg_id());
        long ids = pageHelper.update(sb.toString(),map);
        if(!PublicUtil.isEmpty(ids)){
            return  String.valueOf(ids);
        }else{
            return "";
        }
    }

    @Override
    public void deleteUser(UserInfo userInfo) {
        List<String> idList = Arrays.asList(userInfo.getId().split(","));
        String sql = "delete from SYS_USER where id = ? ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public void deleteUserRole(String userid) {
        String sql = "delete from SYS_USER_ROLE WHERE USER_ID=:USER_ID";
        Map<String,Object> map = new HashMap<>();
        map.put("USER_ID",userid);
        pageHelper.getJdbcTempalte().update(sql, map);
    }

    @Override
    public int editUser(UserInfo userInfo) {
        StringBuffer sb = new StringBuffer(100);
        Map<String,Object> map = new HashMap<>();
        sb.append("update SYS_USER set");
        if(!PublicUtil.isEmpty(userInfo.getUserName())){
            sb.append(" USERNAME=:USERNAME ,");
        }
        if(!PublicUtil.isEmpty(userInfo.getUser_type())){
            sb.append(" USER_TYPE=:USER_TYPE,");
        }
        if(!PublicUtil.isEmpty(userInfo.getPassWord())){
            sb.append(" SALT=:SALT,");
            sb.append(" PASSWORD=:PASSWORD, ");
            String salt = EncryptUtil.getRandomSalt();
            map.put("SALT",salt);
            map.put("PASSWORD",  EncryptUtil.encrypt(userInfo.getPassWord(),salt , EncryptUtil.HASH1, EncryptUtil.HASH_ITERATIONS));
        }
        if(!PublicUtil.isEmpty(userInfo.getAuth_status())){
            sb.append(" AUTH_STATUS=:AUTH_STATUS ,");
        }
        if(!PublicUtil.isEmpty(userInfo.getNickName())){
            sb.append(" NICKNAME=:NICKNAME ,");
        }
        if(!PublicUtil.isEmpty(userInfo.getIs_lock())){
            sb.append(" IS_LOCK=:IS_LOCK ,");
        }
        if(!PublicUtil.isEmpty(userInfo.getOrg_id())){
            sb.append(" ORG_ID=:ORG_ID,");
        }
        if(!PublicUtil.isEmpty(userInfo.getAuth_type())){
            sb.append(" AUTH_TYPE=:AUTH_TYPE,");
        }
        sb.append(" UPDATE_DATE =:UPDATE_DATE");
        sb.append(" WHERE ID =:ID ");
        map.put("USERNAME", userInfo.getUserName());
        map.put("USER_TYPE", userInfo.getUser_type());
        map.put("AUTH_STATUS", userInfo.getAuth_status());
        map.put("NICKNAME", userInfo.getNickName());
        map.put("ORG_ID", userInfo.getOrg_id());
        map.put("IS_LOCK", userInfo.getIs_lock());
        map.put("AUTH_TYPE",userInfo.getAuth_type());
        map.put("UPDATE_DATE",System.currentTimeMillis());
        map.put("ID",userInfo.getId());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public UserInfo getUserById(UserInfo userInfo) {
        String sql ="SELECT SYS_USER.USERNAME,\n" +
                "        SYS_USER.AUTH_TYPE,\n" +
                "        SYS_USER.ID,\n" +
                "        SYS_USER.IS_LOCK,\n" +
                "        SYS_USER.USER_TYPE,\n" +
                "        SYS_USER.NICKNAME,\n" +
                "        SYS_USER.CREATE_DATE,\n" +
                "        SYS_USER.ORG_ID,\n" +
                "        SYS_ORG.ORGAN_NAME,\n" +
                "        SYS_USER_DETAIL.NAME,\n" +
                "        POLITICAL_FEATURE,\n" +
                "        IDCARD,\n" +
                "        SEX,\n" +
                "        PHONE,\n" +
                "        EMAIL,\n" +
                "        birthday,\n" +
                "        nation,\n" +
                "        DESCRIPTION\n" +
                "FROM SYS_USER\n" +
                "LEFT JOIN SYS_ORG\n" +
                "    ON SYS_USER.ORG_ID = SYS_ORG.ID\n" +
                "LEFT JOIN SYS_USER_DETAIL\n" +
                "    ON SYS_USER.ID =SYS_USER_DETAIL.USER_ID\n" +
                "WHERE SYS_USER.ID =:id ";
        Map<String,Object> map = new HashMap<>();
        map.put("id", userInfo.getId());
        return  pageHelper.findSingleData(sql, map, new CommonMapper(UserInfo.class));
    }

    @Override
    public List<Map<String, Object>> getOrganTreeData() {
        String qrySql = "select id,organ_name,ordering,parent_organ as pid from SYS_ORG order by ordering";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(qrySql);
    }

    @Override
    public int resetPwd(UserInfo userInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update SYS_USER set ");
        sb.append("        SALT=:SALT,");
        sb.append("        PASSWORD=:PASSWORD where ID=:ID");
        Map<String,Object> map = new HashMap<>();
        map.put("PASSWORD",EncryptUtil.encrypt("111111", "", EncryptUtil.HASH1, EncryptUtil.HASH_ITERATIONS));
        map.put("SALT","");
        map.put("ID",userInfo.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public List<User2Role> getRoleList(String id) {
        String sql = "select SYS_USER_ROLE.*,SYS_ROLE.ROLE_NAME from SYS_USER_ROLE,SYS_ROLE where " +
                "SYS_USER_ROLE.ROLE_ID = SYS_ROLE.ID AND SYS_USER_ROLE.USER_ID=:id ";
        Map<String,Object> querymap = new HashMap<>();
        querymap.put("id",id);
        List<User2Role> list = new ArrayList<>();
        List<Map<String, Object>> listMap = pageHelper.getJdbcTempalte().queryForList(sql,querymap);
        for(Map<String,Object> map:listMap){
            User2Role user2Role = new User2Role();
            user2Role.setRole_id(String.valueOf(map.get("ROLE_ID")));
            user2Role.setRole_name(String.valueOf(map.get("ROLE_NAME")));
            list.add(user2Role);
        }
        return list;
    }

    @Override
    public void adduser2role(List<User2Role> roleMgrList, String id) {
        String sql = "insert into SYS_USER_ROLE (USER_ID,ROLE_ID) values (?,?)";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < roleMgrList.size(); i++){
            User2Role user2Role = roleMgrList.get(i);
            batchArgs.add(new Object[]{id,String.valueOf(user2Role.getRole_id())});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public int changeStatus(UserInfo userInfo) {
        String sql = "update sys_user set is_lock=:is_lock where id=:id";
        Map<String,Object> map = new HashMap<>();
        map.put("is_lock",userInfo.getIs_lock());
        map.put("id",userInfo.getId());
        return pageHelper.getJdbcTempalte().update(sql.toString(), map);
    }
}
