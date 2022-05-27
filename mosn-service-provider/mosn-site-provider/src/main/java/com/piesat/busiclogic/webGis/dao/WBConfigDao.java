package com.piesat.busiclogic.webGis.dao;

import com.piesat.busiclogic.webGis.entity.PlaneConfig;
import com.piesat.busiclogic.webGis.entity.TableConfig;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class WBConfigDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    public List<Map<String, Object>> getPointTree() {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from WEB_GIS_POINT WHERE STATUS = '1' order by sort ");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }

    public List<Map<String, Object>> getPointTreeLogin(String role_ids) {
        String sql = "";
        if("1".equals(role_ids)){ // 查询领导专用配置gis要素表
            sql = "select * from  WEB_GIS_POINT_LINDAO where status = '1' order by sort";
            return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
        } else {
            sql = "SELECT DISTINCT ID, NAME, PID, TAB_SET, IS_CHECK\n" +
                    "\t, SHOW_TYPE, REQ_TYPE, REQ_URL, SORT, SHOW_SELECT\n" +
                    "\t, FLAG, REQ_PARAM, HAX_NEXT, VALUE, TIME_TYPE\n" +
                    "\t, TIME_SCALE, STATUS, TIME_FIELD, SHOW_APP, ICON\n" +
                    "\t, SHOW_TPYE, SHOW_STAINS\n" +
                    "FROM WEB_GIS_POINT, WEB_GIS_POINT_ROLE\n" +
                    "WHERE  STATUS = '1' and  id = GIS_ID\n" +
                    "\tAND role_id IN (:role_ids) order by sort";
            Map<String,Object> map = new HashMap<>();
            map.put("role_ids",role_ids);
            return pageHelper.getJdbcTempalte().queryForList(sql,map);
        }
    }








    public PlaneConfig getPlaneData(String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" WEB_GIS_PLANE where ID=:ID ");
        Map map = new HashMap();
        map.put("ID", id);
        return  pageHelper.findSingleData(sb.toString(), map, new CommonMapper(PlaneConfig.class));
    }

    public TableConfig getTableData(String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" WEB_GIS_TABLE where ID=:ID ");
        Map map = new HashMap();
        map.put("ID", id);
        return  pageHelper.findSingleData(sb.toString(), map, new CommonMapper(TableConfig.class));
    }

    public List<Map<String, Object>> getAhStation() {
        String sql = "SELECT stationnumber, stationname, latitude, longitude, areaname\n" +
                "\t, bigtype\n" +
                "FROM WEBGIS_STATION_INFO ORDER BY SORT";
        return pageHelper.getJdbcTempalte().queryForList(sql,new HashMap<>());
    }

    public List<Map<String, Object>> getPointTreeApp() {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from WEB_GIS_POINT_APP WHERE STATUS = '1'  order by sort ");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }
}
