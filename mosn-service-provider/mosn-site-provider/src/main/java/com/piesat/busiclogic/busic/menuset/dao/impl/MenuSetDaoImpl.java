package com.piesat.busiclogic.busic.menuset.dao.impl;


import com.piesat.busiclogic.busic.menuset.dao.MenuSetDao;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MenuSetDaoImpl implements MenuSetDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public List<Map<String, Object>> getMenuSetByCode(String code, String roleIds) {
        String sql = "select * from SYS_MENU_SELECT where status = '1' and  appcode =:code order by sort";
        Map<String,Object> map = new HashMap<>();
        map.put("code",code);
        System.out.println(sql);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }

    @Override
    public List<Map<String, Object>> getMenuSetProByCode(String code, String roleIds) {
        StringBuffer sb = new StringBuffer();
        sb.append(" SELECT distinct(id),");
        sb.append("     name,pid,code,");
        sb.append("     appcode,status,");
        sb.append("     hasnext,url,");
        sb.append("     head,initparam,");
        sb.append("     sort,format,");
        sb.append("     product_id,page_url,");
        sb.append("     is_link,create_time");
        sb.append(" FROM sys_menu_select,sys_role_menu");
        sb.append(" WHERE sys_menu_select.id = sys_role_menu.MENU_ID ");
        Map<String,Object> map = new HashMap<>();
        if(!PublicUtil.isEmpty(code)){
            sb.append(" AND appcode =:code ");
            map.put("code",code);
        }
        sb.append("  AND status = '1'  AND sys_role_menu.ROLE_ID IN (:ROLEIDS)");
        sb.append("  order by sort");
        map.put("ROLEIDS",roleIds);
        System.out.println(sb.toString());
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map);
    }

    @Override
    public Map<String, Object> getRootIdByCode(String code) {
        String qrySql = "select  id from SYS_MENU_SELECT where pid = 'R' and code =:code";
        Map<String,Object> map = new HashMap<>();
        map.put("code",code);
        return pageHelper.getJdbcTempalte().queryForMap(qrySql,map);
    }

    @Override
    public List<String> getRoleList(String userid) {
        String sql = "select role_id from sys_user_role where user_id=:user_id";
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userid);
        return pageHelper.getJdbcTempalte().queryForList(sql,map,String.class);
    }

    @Override
    public List<Map<String, Object>> getCodeData(String code) {
        String sql = "SELECT sys_dict_item.*\n" +
                " FROM sys_dict\n" +
                " LEFT JOIN sys_dict_item\n" +
                "    ON sys_dict.DICT_CODE = sys_dict_item.dict_code\n" +
                " WHERE dict_code =:code " +
                " ORDER BY  sys_dict_item.order";
        Map<String,Object> map = new HashMap<>();
        map.put("code",code);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }

    @Override
    public List<Map<String, Object>> getMenuSetNoRule() {
        String sql = "SELECT id,\n" +
                "        name,\n" +
                "        pid,\n" +
                "        code,\n" +
                "        appcode,\n" +
                "        status,\n" +
                "        hasnext,\n" +
                "        url,\n" +
                "        head,\n" +
                "        initparam,\n" +
                "        sort,\n" +
                "        format,\n" +
                "        product_id,\n" +
                "        page_url,\n" +
                "        is_link,\n" +
                "        create_time\n" +
                " FROM sys_menu_select \n" +
                " order by sort";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }
}
