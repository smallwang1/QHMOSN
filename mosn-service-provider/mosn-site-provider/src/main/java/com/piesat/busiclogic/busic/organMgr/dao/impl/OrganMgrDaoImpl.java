package com.piesat.busiclogic.busic.organMgr.dao.impl;

import com.piesat.busiclogic.busic.organMgr.dao.OrganMgrDao;
import com.piesat.busiclogic.busic.organMgr.entity.OrganInfo;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class OrganMgrDaoImpl implements OrganMgrDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public Page<OrganInfo> getOrganList(long currentPage, int pageSize, OrganInfo organInfo) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from ");
        sb.append("    SYS_ORG");
        sb.append("    where 1 = 1 ");

        Map map = new HashMap();
        if(!PublicUtil.isEmpty(organInfo.getOrgan_name())){
            sb.append(" and organ_name like '%'+:organ_name+'%' ");
            map.put("organ_name",organInfo.getOrgan_name());
        }
        if(!PublicUtil.isEmpty(organInfo.getParent_organ())){
            sb.append(" and PARENT_ORGAN =:PARENT_ORGAN");
            map.put("PARENT_ORGAN",organInfo.getParent_organ());
        }


        return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(OrganInfo.class),currentPage,pageSize);
    }

    @Override
    public int addOrgan(OrganInfo organInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  SYS_ORG");
        sb.append(" (");
        sb.append("ORGAN_NAME,\n");
        sb.append("GW_NAME,\n");
        sb.append("ORGAN_STATE,\n");
        sb.append("ORGAN_STATE_NAME,\n");
        sb.append("ORDERING,\n");
        sb.append("PARENT_ORGAN,\n");
        sb.append("OBSERVATORY \n");
        sb.append(" ) values (");

        sb.append(":ORGAN_NAME,\n");
        sb.append(":GW_NAME,\n");
        sb.append(":ORGAN_STATE,\n");
        sb.append(":ORGAN_STATE_NAME,\n");
        sb.append(":ORDERING,\n");
        sb.append(":PARENT_ORGAN,\n");
        sb.append(":OBSERVATORY) \n");

        Map map = new HashMap<>();
        map.put("ORGAN_NAME", organInfo.getOrgan_name());
        map.put("GW_NAME", organInfo.getGw_name());
        map.put("ORGAN_STATE",organInfo.getOrgan_state());
        map.put("ORGAN_STATE_NAME", organInfo.getOrgan_state_name());
        map.put("ORDERING",organInfo.getOrdering());
        map.put("PARENT_ORGAN",organInfo.getParent_organ());
        map.put("OBSERVATORY",organInfo.getObservatory());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public void deleteOrgan(OrganInfo organInfo) {
        List<String> idList = Arrays.asList(organInfo.getId().split(","));
        String sql = "delete from SYS_ORG where id = ? ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public int editOrgan(OrganInfo organInfo) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update SYS_ORG set");
        sb.append(" ORGAN_NAME=:ORGAN_NAME ,");
        sb.append(" GW_NAME=:GW_NAME,");
        sb.append(" ORGAN_STATE=:ORGAN_STATE, ");
        sb.append(" ORGAN_STATE_NAME=:ORGAN_STATE_NAME ,");
        sb.append(" ORDERING=:ORDERING ,");
        sb.append(" PARENT_ORGAN=:PARENT_ORGAN,");
        sb.append(" OBSERVATORY=:OBSERVATORY ");
        sb.append(" WHERE ID =:ID ");

        Map map = new HashMap();
        map.put("ORGAN_NAME", organInfo.getOrgan_name());
        map.put("GW_NAME", organInfo.getGw_name());
        map.put("ORGAN_STATE",  organInfo.getOrgan_state());
        map.put("ORGAN_STATE_NAME", organInfo.getOrgan_state_name());
        map.put("ORDERING", organInfo.getOrdering());
        map.put("PARENT_ORGAN", organInfo.getParent_organ());
        map.put("OBSERVATORY",organInfo.getObservatory());
        map.put("ID",organInfo.getId());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public OrganInfo getOrganById(OrganInfo organInfo) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" SYS_ORG where ID=:ID ");
        Map map = new HashMap();
        map.put("ID", organInfo.getId());
        return  pageHelper.findSingleData(sb.toString(), map, new CommonMapper(OrganInfo.class));

    }

    @Override
    public List<Map<String, Object>> getOrganTreeData() {
        String qrySql = "select id,organ_name,ordering,parent_organ as pid from SYS_ORG order by ordering";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(qrySql);
    }
}
