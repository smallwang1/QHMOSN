package com.piesat.busiclogic.busic.menuset.dao.impl;

import com.piesat.busiclogic.busic.menuset.dao.MenuMgrDao;
import com.piesat.busiclogic.busic.menuset.entity.MenuInfo;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MenuMgrDaoImpl implements MenuMgrDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public Page<MenuInfo> getMenuData(long currentPage, int pageSize, MenuInfo menuInfo) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from ");
        sb.append("    SYS_MENU_SELECT");
        sb.append("    where 1 = 1 ");

        Map map = new HashMap();
        if(!PublicUtil.isEmpty(menuInfo.getName())){
            sb.append(" and name like '%'+:name+'%' ");
            map.put("name",menuInfo.getName());
        }
        if(!PublicUtil.isEmpty(menuInfo.getStatus())){
            sb.append(" and STATUS=:STATUS");
            map.put("STATUS",menuInfo.getStatus());
        }
        if(!PublicUtil.isEmpty(menuInfo.getAppcode())){
            sb.append(" and appcode=:appcode");
            map.put("appcode",menuInfo.getAppcode());
        }
        return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(MenuInfo.class),currentPage,pageSize);
    }

    @Override
    public int addMenuInfo(MenuInfo menuInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  SYS_MENU_SELECT");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("NAME,\n");
        sb.append("PID,\n");
        sb.append("CODE,\n");
        sb.append("APPCODE,\n");
        sb.append("STATUS,\n");
        sb.append("HASNEXT,\n");
        sb.append("URL,\n");
        sb.append("HEAD,\n");
        sb.append("INITPARAM,\n");
        sb.append("SORT,\n");
        sb.append("FORMAT,\n");
        sb.append("PRODUCT_ID,\n");
        sb.append("IS_LINK,\n");
        sb.append("CREATE_TIME,\n");
        sb.append("TYPE_NAME,\n");
        sb.append("TYPE_VALUE,\n");
        sb.append("TYPE_CODE,\n");
        sb.append("LEVEL_NAME,\n");
        sb.append("LEVEL_VALUE,\n");
        sb.append("LEVEL_CODE\n");
        sb.append(" ) values (");
        sb.append(":ID,\n");
        sb.append(":NAME,\n");
        sb.append(":PID,\n");
        sb.append(":CODE,\n");
        sb.append(":APPCODE,\n");
        sb.append(":STATUS,\n");
        sb.append(":HASNEXT,\n");
        sb.append(":URL,\n");
        sb.append(":HEAD,\n");
        sb.append(":INITPARAM,\n");
        sb.append(":SORT,\n");
        sb.append(":FORMAT,\n");
        sb.append(":PRODUCT_ID,\n");
        sb.append(":IS_LINK,\n");
        sb.append(":CREATE_TIME,\n");
        sb.append(":TYPE_NAME,\n");
        sb.append(":TYPE_VALUE,\n");
        sb.append(":TYPE_CODE,\n");
        sb.append(":LEVEL_NAME,\n");
        sb.append(":LEVEL_VALUE,\n");
        sb.append(":LEVEL_CODE)\n");

        Map map = new HashMap<>();
        map.put("ID", UUID.randomUUID().toString());
        map.put("NAME",menuInfo.getName());
        map.put("PID", menuInfo.getPid());
        map.put("CODE", menuInfo.getCode());
        map.put("APPCODE",menuInfo.getAppcode());
        map.put("STATUS", menuInfo.getStatus());
        map.put("HASNEXT",menuInfo.getHasnext());
        map.put("URL", menuInfo.getUrl());
        map.put("HEAD",menuInfo.getHead());
        map.put("INITPARAM", menuInfo.getInitparam());
        map.put("PRODUCT_ID", menuInfo.getProduct_id());
        map.put("IS_LINK", menuInfo.getIs_link());
        map.put("CREATE_TIME",System.currentTimeMillis());
        map.put("SORT", menuInfo.getSort());
        map.put("FORMAT", menuInfo.getFormat());
        map.put("TYPE_NAME",menuInfo.getType_name());
        map.put("TYPE_VALUE",menuInfo.getType_value());
        map.put("TYPE_CODE",menuInfo.getType_code());
        map.put("LEVEL_VALUE",menuInfo.getLevel_value());
        map.put("LEVEL_CODE",menuInfo.getLevel_code());
        map.put("LEVEL_NAME",menuInfo.getLevel_name());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public void deleteMenuInfo(MenuInfo menuInfo) {
        List<String> idList = Arrays.asList(menuInfo.getId().split(","));
        String sql = "delete from SYS_MENU_SELECT where id =?";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public int editStation(MenuInfo menuInfo) {

        StringBuffer sb = new StringBuffer(100);
        sb.append("update SYS_MENU_SELECT set");
        sb.append(" NAME=:NAME ,");
        if(!PublicUtil.isEmpty(menuInfo.getCode())){
            sb.append(" APPCODE=:APPCODE,");
        }
        sb.append(" STATUS=:STATUS, ");
        sb.append(" URL=:URL ,");
        if(!PublicUtil.isEmpty(menuInfo.getPid())){
            sb.append(" PID=:PID ,");
        }
        if(!PublicUtil.isEmpty(menuInfo.getHasnext())){
            sb.append(" HASNEXT=:HASNEXT,");
        }
        if(!PublicUtil.isEmpty(menuInfo.getSort())){
            sb.append(" SORT=:SORT ,");
        }
        if(!PublicUtil.isEmpty(menuInfo.getProduct_id())){
            sb.append(" PRODUCT_ID=:PRODUCT_ID ,");
        }
        sb.append(" IS_LINK=:IS_LINK,");
        sb.append(" FORMAT=:FORMAT  ");
        sb.append(" WHERE ID =:ID ");

        Map map = new HashMap();
        map.put("NAME", menuInfo.getName());
        map.put("APPCODE", menuInfo.getAppcode());
        map.put("STATUS", menuInfo.getStatus());
        map.put("URL", menuInfo.getUrl());
        map.put("HASNEXT", menuInfo.getHasnext());
        map.put("SORT", menuInfo.getSort());
        map.put("PID", menuInfo.getPid());
        map.put("FORMAT", menuInfo.getFormat());
        map.put("PRODUCT_ID",menuInfo.getProduct_id());
        map.put("IS_LINK",menuInfo.getIs_link());
        map.put("ID",menuInfo.getId());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public MenuInfo getMenuInfoById(MenuInfo menuInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select SYS_MENU_SELECT.*,MGR_PRODUCT.PRODUCT_NAME from ");
        sb.append(" SYS_MENU_SELECT LEFT JOIN MGR_PRODUCT on SYS_MENU_SELECT.PRODUCT_ID = MGR_PRODUCT.ID  where SYS_MENU_SELECT.ID=:ID ");
        Map map = new HashMap();
        map.put("ID", menuInfo.getId());
        MenuInfo singleData= pageHelper.findSingleData(sb.toString(), map, new CommonMapper(MenuInfo.class));
        return  singleData;
    }

    @Override
    public List<Map<String, Object>> getMenuSetByCode(String code) {
        String qrySql = "select * from SYS_MENU_SELECT  order by sort";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(qrySql);
    }

    @Override
    public List<Map<String, Object>> getMenuHead() {
        String qrySql = "select * from SYS_MENU_SELECT where appcode = 'root' order by sort";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(qrySql);
    }

    @Override
    public List<Map<String, Object>> getTheLowSortList(String pid, String sort, String flag) {
//        String sql ="select * from SYS_MENU_SELECT where pid = '"+pid+"' and SORT > '"+sort+"'";
        String sql ="select * from SYS_MENU_SELECT where pid=:pid and SORT>:sort";
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        map.put("sort",sort);
//        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
        return pageHelper.getJdbcTempalte().queryForList(sql.toString(),map);
    }

    @Override
    public void updateSort(String sort, String id) {
        String sql = "update SYS_MENU_SELECT set sort=:sort where id=:id";
        Map<String,Object> map = new HashMap<>();
        map.put("sort",sort);
        map.put("id",id);
        int i = pageHelper.getJdbcTempalte().update(sql.toString(), map);
    }
}
