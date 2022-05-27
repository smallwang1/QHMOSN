package com.piesat.busiclogic.busic.roleMgr.dao.impl;

import com.piesat.busiclogic.busic.roleMgr.dao.RoleMgrDao;
import com.piesat.busiclogic.busic.roleMgr.entity.MenuRole;
import com.piesat.busiclogic.busic.roleMgr.entity.ProductRole;
import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class RoleMgrDaoImpl implements RoleMgrDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public Page<RoleMgr> getRoleData(long currentPage, int pageSize, RoleMgr roleMgr) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from ");
        sb.append("    SYS_ROLE");
        sb.append("    where 1 = 1 ");

        Map<String,Object> map = new HashMap<>();
        if(!PublicUtil.isEmpty(roleMgr.getRole_name())){
            sb.append(" and ROLE_NAME like '%'+:ROLE_NAME+'%' ");
            map.put("ROLE_NAME",roleMgr.getRole_name());
        }

        if(!PublicUtil.isEmpty(roleMgr.getType())){
            sb.append(" and TYPE=:TYPE");
            map.put("TYPE",roleMgr.getType());
        }

        if(!PublicUtil.isEmpty(roleMgr.getStatus())){
            sb.append(" and STATUS=:STATUS");
            map.put("STATUS",roleMgr.getStatus());
        }
        sb.append(" order by create_date desc");

        return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(RoleMgr.class),currentPage,pageSize);
    }

    @Override
    public String addRoleInfo(RoleMgr roleMgr) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into  SYS_ROLE");
        sb.append("(");
        sb.append("ROLE_NAME,");
        sb.append("ROLE_CODE,");
        sb.append("REMARK,");
        sb.append("TYPE,");
        sb.append("CREATE_DATE,");
        sb.append("CREATE_BY,");
        sb.append("STATUS");
        sb.append(")values(");

        sb.append(":ROLE_NAME,");
        sb.append(":ROLE_CODE,");
        sb.append(":REMARK,");
        sb.append(":TYPE,");
        sb.append(":CREATE_DATE,");
        sb.append(":CREATE_BY,");
        sb.append(":STATUS)");

        Map<String,Object> map = new HashMap<>();
        map.put("ROLE_NAME",roleMgr.getRole_name());
        map.put("ROLE_CODE", roleMgr.getRole_code());
        map.put("TYPE", roleMgr.getType());
        map.put("REMARK", roleMgr.getRemark());
        map.put("CREATE_DATE",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("CREATE_BY", roleMgr.getCreate_by());
        map.put("STATUS",roleMgr.getStatus());
        long id = pageHelper.update(sb.toString(),map);
        return  String.valueOf(id);
    }

    @Override
    public void deleteRoleInfo(RoleMgr roleMgr) {
        List<String> idList = Arrays.asList(roleMgr.getId().split(","));
        String sql = "delete from SYS_ROLE where id = ? ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public void deleteMenuRole(String id) {
        String sql = "delete from SYS_ROLE_MENU WHERE ROLE_ID=:ROLE_ID";
        Map<String,Object> map = new HashMap<>();
        map.put("ROLE_ID",id);
        pageHelper.getJdbcTempalte().update(sql,map);
    }

    @Override
    public void addProductRole(List<ProductRole> productRoles, String id) {
        String sql = "insert into MGR_PRODUCT_PROTOROLE (role_id,product_id) values(?,?)";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < productRoles.size(); i++){
            ProductRole ProductRole = productRoles.get(i);
            batchArgs.add(new Object[]{id,String.valueOf(ProductRole.getProduct_id())});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public void deleteProductRole(String id) {
        String sql = "delete from MGR_PRODUCT_PROTOROLE WHERE ROLE_ID=:ROLE_ID";
        Map<String,Object> map = new HashMap<>();
        map.put("ROLE_ID",id);
        pageHelper.getJdbcTempalte().update(sql,map);
    }

    @Override
    public List<ProductRole> getProductRole(String id) {
        String sql = "SELECT MGR_PRODUCT_PROTOROLE.*\n" +
                "FROM MGR_PRODUCT_PROTOROLE, MGR_PRODUCT\n" +
                "WHERE MGR_PRODUCT_PROTOROLE.PRODUCT_ID = MGR_PRODUCT.id\n" +
                "\tAND role_id = :id " +
                "\tAND is_pro = '1'";
        Map<String,Object> querymap = new HashMap<>();
        querymap.put("id",id);
        List<Map<String,Object>> roleList = pageHelper.getJdbcTempalte().queryForList(sql,querymap);
        List<ProductRole> productRoles = new ArrayList<>();
        for(Map<String,Object> map:roleList){
            ProductRole productRole = new ProductRole();
            productRole.setProduct_id(String.valueOf(map.get("PRODUCT_ID")));
            productRole.setRole_id(String.valueOf(map.get("ROLE_ID")));
            productRoles.add(productRole);
        }
        return productRoles;
    }

    @Override
    public int editRole(RoleMgr roleMgr) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update SYS_ROLE  set ");
        Map<String,Object> map = new HashMap<>();
        sb.append(" ROLE_NAME=:ROLE_NAME ,");
        map.put("ROLE_NAME", roleMgr.getRole_name());
        if(!PublicUtil.isEmpty(roleMgr.getRole_code())){
            sb.append(" ROLE_CODE=:ROLE_CODE,");
            map.put("ROLE_CODE", roleMgr.getRole_code());
        }
        sb.append(" REMARK=:REMARK, ");
        map.put("REMARK", roleMgr.getRemark());
        sb.append("TYPE=:TYPE, ");
        map.put("TYPE", roleMgr.getType());
        if(!PublicUtil.isEmpty(roleMgr.getUpdate_by())){
            sb.append(" UPDATE_BY=:UPDATE_BY ,");
            map.put("UPDATE_BY", roleMgr.getUpdate_by());
        }
        if(!PublicUtil.isEmpty(roleMgr.getStatus())){
           sb.append(" STATUS=:STATUS,");
           map.put("STATUS", roleMgr.getStatus());
        }
        sb.append(" UPDATE_DATE=:UPDATE_DATE");
        map.put("UPDATE_DATE",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) );
        map.put("ID",roleMgr.getId());
        sb.append(" WHERE ID =:ID ");
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public RoleMgr getRoleById(RoleMgr roleMgr) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" SYS_ROLE where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", roleMgr.getId());
      return  pageHelper.findSingleData(sb.toString(), map, new CommonMapper(RoleMgr.class));
    }

    @Override
    public List<String> getRoleIdByUserid(String userid) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select sys_role.ROLE_CODE ");
        sb.append("     from sys_role,sys_user_role");
        sb.append("     where sys_role.id = sys_user_role.ROLE_ID");
        sb.append("      and sys_user_role.USER_ID=:USER_ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("USER_ID", userid);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map,String.class);
    }

    @Override
    public List<Map<String, Object>> getAllRole(RoleMgr roleMgr) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from ");
        sb.append("    SYS_ROLE");
        sb.append("    where 1 = 1 ");

        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }

    @Override
    public void addMeunRole(List<MenuRole> menuRoleList,String id) {
        String sql = "insert into sys_role_menu (role_id,menu_id) values(?,?)";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < menuRoleList.size(); i++){
            MenuRole menuRole = menuRoleList.get(i);
            batchArgs.add(new Object[]{id,String.valueOf(menuRole.getMenu_id())});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public List<MenuRole> getMenuRole(String id) {
        String  sql = "SELECT sys_role_menu.*\n" +
                "FROM sys_role_menu, sys_menu_select\n" +
                "WHERE sys_role_menu.MENU_ID = sys_menu_select.ID\n" +
                "\tAND role_id = :id " +
                "\tAND sys_menu_select.HASNEXT = '0'";
        Map<String,Object> querymap = new HashMap<>();
        querymap.put("id",id);
        List<Map<String,Object>> roleList = pageHelper.getJdbcTempalte().queryForList(sql,querymap);
        List<MenuRole> roleList1 = new ArrayList<>();
        for(Map<String,Object> map:roleList){
            MenuRole menuRole = new MenuRole();
            menuRole.setMenu_id(String.valueOf(map.get("MENU_ID")));
            menuRole.setRole_id(String.valueOf(map.get("ROLE_ID")));
            roleList1.add(menuRole);
        }
        return roleList1;
    }
}
