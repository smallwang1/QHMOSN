package com.piesat.busiclogic.busic.repositories;

import com.piesat.busiclogic.busic.entities.ServiceAlgParam;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.dataSource.DBContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class ServiceAlgRepository {

    @Autowired
    private JdbcPageHelper pageHelper;


    public List<Map<String, Object>> getAlgConfig(ServiceAlgParam serviceAlgParam) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ALGOR_SERVICE_CONFIG WHERE STATUS = '1' ORDER BY SORT");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());

    }

    public List<Map<String, Object>> getBscl(String startime, String endtime) {
        String sql = "select * from JY_INFO where date >= '"+ startime +"' and date <= '"+endtime+"'";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }

    public List<Map<String, Object>> getAlgorData(String stationId, String sTime, String eTime) {
        StringBuffer sb = new StringBuffer();
        sb.append("select * from algor_15_PRE where 1=1");
        sb.append(" and stationid = '"+stationId+"'");
        sb.append(" and start_time >='"+ sTime+"'");
        sb.append(" and start_time <='"+eTime+"'");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }

    public List<Map<String,Object>> getTemp(String stationId, String sTime, String eTime) throws Exception{
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmmSS");
        sb.append("select D_DATETIME,V01301,V_AT,V_CIOHB from SURF_WEA_CHN_MUL_ALG_HOR_TAB WHERE 1=1 ");
        sb.append(" and V01301 = '"+stationId+"'");
        sb.append(" and D_DATETIME >='"+ sf.format(sf1.parse(sTime))+"'");
        sb.append(" and D_DATETIME <='"+sf.format(sf1.parse(eTime))+"'");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }

    public List<Map<String,Object>> getStationInfo(){
        String sql = "SELECT stationnumber, otherstatnme\n" +
                "FROM WEBGIS_STATION_INFO\n" +
                "WHERE otherstatnme IS NOT NULL\n" +
                "\tAND otherstatnme <> 'null'";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }

    public List<Map<String, Object>> getExtremeData(String stationId, String startTime, String endTime) throws Exception {
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmmSS");
        sb.append("select D_DATETIME,V01301,V12011,V12012,V13305,V11042 from SURF_WEA_CHN_MUL_DAY_TAB WHERE 1=1 ");
        sb.append(" and V01301 = '"+stationId+"'");
        sb.append(" and D_DATETIME >='"+ sf.format(sf1.parse(startTime))+"'");
        sb.append(" and D_DATETIME <='"+sf.format(sf1.parse(endTime))+"'");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }

    public List<Map<String,Object>> getDayDataByElements(String stationId,String startTime,String endTime,String elements) throws Exception{
        StringBuffer sb = new StringBuffer();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmmSS");
        sb.append("select D_DATETIME,V01301,");
        sb.append(elements);
        sb.append(" from SURF_WEA_CHN_MUL_DAY_TAB WHERE 1=1");
        if(PublicUtil.isEmpty(stationId)){
            sb.append(" and V01301 like '58%'");
        }else{
            sb.append(" and V01301 = '"+stationId+"'");
        }
        sb.append(" and  " + elements + " < 999000" );
        sb.append(" and  V_ACODE like '34%'");
        sb.append(" and D_DATETIME >='"+ sf.format(sf1.parse(startTime))+"'");
        sb.append(" and D_DATETIME <='"+sf.format(sf1.parse(endTime))+"'");
        List<Map<String,Object>> dataList = new ArrayList<>();
        DBContextHolder.setDataSourceName("cmadaasDataSource");
        try {
            dataList = pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
        }catch (Exception e){
        }finally {
            DBContextHolder.clear();
        }
        return dataList;
    }
}
