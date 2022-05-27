package com.piesat.busiclogic.busic.repositories;

import com.piesat.busiclogic.busic.entities.IndexAdvert;
import com.piesat.busiclogic.busic.entities.IndexColum;
import com.piesat.busiclogic.busic.entities.IndexRotation;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class IndexRepository {

    @Autowired
    private JdbcPageHelper pageHelper;

    public Map<String, Object> getLatAndLon(String stationId) {
        String sql = "select LAT,LON from WEBGIS_STATION_INFO where STATIONNUMBER =:stationId";
        Map map = new HashMap<>();
        map.put("stationId",stationId);
        List<Map<String,Object>> resultList =  pageHelper.getJdbcTempalte().queryForList(sql,map);
        if(!PublicUtil.isEmpty(resultList)){
            return resultList.get(0);
        }else{
            return new HashMap<>();
        }
    }


    public Map<String, Object> getLatAndLons(String stationId) {
        String sql = "select LATITUDE as LAT,LONGITUDE as LON from WEBGIS_STATION_INFO where STATIONNUMBER =:stationId";
        Map map = new HashMap<>();
        map.put("stationId",stationId);
        List<Map<String,Object>> resultList =  pageHelper.getJdbcTempalte().queryForList(sql,map);
        if(!PublicUtil.isEmpty(resultList)){
            return resultList.get(0);
        }else{
            return new HashMap<>();
        }
    }

    public void addIndexAdvert(IndexAdvert indexAdvert) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  MGR_INDEX_ADVERT");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("NAME,\n");
        sb.append("BASE64DATA,");
        sb.append("DESCRIPTION,");
        sb.append("TYPE,");
        sb.append("LINK,\n");
        sb.append("CREATE_TIME\n");
        sb.append(" ) values (");

        sb.append(":ID,\n");
        sb.append(":NAME,\n");
        sb.append(":BASE64DATA,\n");
        sb.append(":DESCRIPTION,\n");
        sb.append(":TYPE,\n");
        sb.append(":LINK,\n");
        sb.append(":CREATE_TIME)\n");

        Map map = new HashMap<>();
        map.put("ID",UUID.randomUUID().toString());
        map.put("NAME",indexAdvert.getName());
        map.put("BASE64DATA", indexAdvert.getBase64data());
        map.put("CREATE_TIME",System.currentTimeMillis());
        map.put("DESCRIPTION",indexAdvert.getDescription());
        map.put("TYPE",indexAdvert.getType());
        map.put("LINK",indexAdvert.getLink());
        pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    public void editIndexAdvert(IndexAdvert indexAdvert) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update MGR_INDEX_ADVERT set ");
        sb.append(" NAME=:NAME,");
        sb.append(" BASE64DATA=:BASE64DATA,");
        sb.append(" LINK=:LINK,");
        sb.append(" DESCRIPTION=:DESCRIPTION,");
        sb.append(" TYPE=:TYPE,");
        sb.append(" STATUS=:STATUS");
        sb.append(" where ID=:ID");

        Map map = new HashMap<>();
        map.put("ID",indexAdvert.getId());
        map.put("NAME",indexAdvert.getName());
        map.put("BASE64DATA", indexAdvert.getBase64data());
        map.put("STATUS",indexAdvert.getStatus());
        map.put("DESCRIPTION",indexAdvert.getDescription());
        map.put("LINK",indexAdvert.getLink());
        map.put("TYPE",indexAdvert.getType());
        pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    public void deleteIndexAdvert(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        String sql = "delete from MGR_INDEX_ADVERT where id = ? ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }


    public Page<IndexAdvert> getIndexAdvertList(long currentPage, int pageSize, IndexAdvert indexAdvert) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from MGR_INDEX_ADVERT where 1 =1 ");
        Map map = new HashMap();
        sb.append(" AND TYPE=:TYPE");
        map.put("TYPE",indexAdvert.getType());
        if(!PublicUtil.isEmpty(indexAdvert.getStatus())){
            sb.append(" AND STATUS =:STATUS");
            map.put("STATUS",indexAdvert.getStatus());
        }
        sb.append(" order by sort desc");
        return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(IndexAdvert.class),currentPage,pageSize);
    }

    public void addIndexRotation(IndexRotation indexRotation) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  MGR_INDEX_ROTATION");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("ROTATION_NAME,\n");
        sb.append("UNIT_ID,\n");
        sb.append("PRODUCT_ID,\n");
        sb.append("MENU_ID,\n");
        sb.append("ROUTE,\n");
        sb.append("PRODUCT_NAME,\n");
        sb.append("CREATE_TIME\n");
        sb.append(" ) values (");

        sb.append(":ID,\n");
        sb.append(":ROTATION_NAME,\n");
        sb.append(":UNIT_ID,\n");
        sb.append(":PRODUCT_ID,\n");
        sb.append(":MENU_ID,\n");
        sb.append(":ROUTE,\n");
        sb.append(":PRODUCT_NAME,\n");
        sb.append(":CREATE_TIME)\n");

        Map map = new HashMap<>();
        map.put("ID",UUID.randomUUID().toString());
        map.put("ROTATION_NAME",indexRotation.getRotation_name());
        map.put("UNIT_ID", indexRotation.getUnit_id());
        map.put("PRODUCT_ID", indexRotation.getProduct_id());
        map.put("MENU_ID", indexRotation.getMenu_id());
        map.put("ROUTE", indexRotation.getRoute());
        map.put("PRODUCT_NAME", indexRotation.getProduct_name());
        map.put("CREATE_TIME",System.currentTimeMillis());
        pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    public void editIndexRotation(IndexRotation indexRotation) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update MGR_INDEX_ROTATION set ");
        sb.append(" PRODUCT_NAME=:PRODUCT_NAME");
        sb.append(" where ID = :ID");

        Map<String,String> map = new HashMap<>();
        map.put("PRODUCT_NAME",indexRotation.getProduct_name());
        map.put("ID",indexRotation.getId());
        pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    public void deleteIndexRotation(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        String sql = "delete from MGR_INDEX_ROTATION where id = ? ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }


    public List<Map<String,Object>> getIndexProduct(){
        String sql = "select product_id,menu_id,product_name from MGR_INDEX_SHOW  where status = '1' order by sort ";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }

    public List<Map<String,Object>> getHotProduct(){
        String sql = "select product_id,menu_id,product_name from MGR_INDEX_SHOW  where status = '1' and type = 'hot' order by sort desc ";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }


    public List<Map<String,Object>> getIndexRotationList(IndexRotation indexRotation) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select ID,ROTATION_NAME,PRODUCT_NAME,PRO_ID,UNIT_ID,CREATE_TIME,MENU_ID,PRODUCT_ID,URL,ROUTE from MGR_INDEX_ROTATION where 1 = 1  ");

        Map map = new HashMap<>();
        if(!PublicUtil.isEmpty(indexRotation.getStatus())){
            sb.append(" AND STATUS =:STATUS ");
            map.put("STATUS",indexRotation.getStatus());
        }
        sb.append(" order by sort");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }

    public void deleteIndexCalum() {
        String sql = "delete from MGR_INDEX_DATA";
        pageHelper.getJdbcTempalte().update(sql,new HashMap<>());
    }

    public void insertIndexCalum(List<IndexColum> indexColumList) {
        String sql = "insert into MGR_INDEX_DATA (id,type,pid,name,sort) values(?,?,?,?,?)";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < indexColumList.size(); i++){
            IndexColum indexColum = indexColumList.get(i);
            batchArgs.add(new Object[]{indexColum.getId(),indexColum.getType(),indexColum.getPid(),indexColum.getName(),indexColum.getSort()});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    public List<Map<String, Object>> getIndexDataColumn() {
        String sql = "SELECT mgr_index_data.*, mgr_index_data_config.base64\n" +
                " FROM mgr_index_data left join\n" +
                " mgr_index_data_config ON mgr_index_data.id = mgr_index_data_config.id ";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }

    public void addDataBase(String id, String base64) {
        StringBuffer sb = new StringBuffer();
        sb.append("insert into mgr_index_data_config ");
        sb.append(" (id,base64) values (:ID,:BASE64)");

        Map map = new HashMap();
        map.put("ID",id);
        map.put("BASE64",base64);
        pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    public List<Map<String, Object>> getIndexData() {
        String sql = "SELECT id, name, type, pid, sort\n" +
                "\t, mgr_index_data_config.BASE64\n" +
                "FROM mgr_index_data\n" +
                "\tLEFT JOIN mgr_index_data_config ON mgr_index_data.id = mgr_index_data_config.id\n" +
                "ORDER BY sort";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }

    public void setIndexAdvert(String status, String id) {
        String sql = " update MGR_INDEX_ADVERT set status=:status where id=:id";
        Map map = new HashMap();
        map.put("status",status);
        map.put("id",id);
        pageHelper.getJdbcTempalte().update(sql,map);
    }

    public void batchAddIndexRotation(List<IndexRotation> indexRotationList) {
        String sql =" insert into MGR_INDEX_ROTATION (id,rotation_name,unit_id,create_time,product_id,menu_id) values (?,?,?,?,?,?) ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < indexRotationList.size(); i++){
            IndexRotation indexRotation = indexRotationList.get(i);
            batchArgs.add(new Object[]{UUID.randomUUID(),indexRotation.getRotation_name(),indexRotation.getUnit_id(),String.valueOf(System.currentTimeMillis()),indexRotation.getProduct_id(),indexRotation.getMenu_id()});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }
}
