package com.piesat.busiclogic.busic.repositories;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Repository
public class ProcedureRepository {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public List<Map<String,Object>> getProduceData(String elements,String startTime,String endTime,String adminCodes){
        StringBuffer sb = new StringBuffer();
        sb.append( "select  D_DATETIME as DataTime,t1.v02301 as Station_levl, t1.V01301 as  Station_Id_C,V_ACODE Admin_Code_CHN,");
        sb.append( this.getElementByKey(elements)[0]);
        sb.append(" t1.v05001 Lat, t1.v06001 Lon");
        sb.append(" FROM SURF_WEA_CHN_MUL_HOR_TAB_VIEW t1 ");
        sb.append(" where t1.d_datetime > '");
        sb.append(startTime +"' and t1.d_datetime <= '" +endTime);
        if("340000".equals(adminCodes)){
            sb.append("'  and t1.v_acode like '34%' and ");
        }else{
            String cityCode = adminCodes.substring(0,4);
            sb.append("'  and t1.v_acode like '"+cityCode+"%' and ");
        }
        sb.append(" v02301 in ('11','12','13','14') and");
        sb.append(this.getElementByKey(elements)[1]);
        return jdbcTemplate.queryForList(sb.toString());
    }


    public String[] getElementByKey(String elements){
        String[] sqlField = new String[2];
        if("MAX_PRE_1h".equals(elements)){
            sqlField[0] = " t1.V13019 MAX_PRE_1h,";
            sqlField[1] = " t1.V13019 < '900000' and t1.V13019 > '0'";
        }else if("MAX_TEM".equals(elements)){
            sqlField[0] = "  t1.V12011 as MAX_TEM,\n" +
                    "    t1.V12011_052 as MAX_TEM_OTime,";
            sqlField[1] = " V12011 < '900000'";
        }else if("MIN_TEM".equals(elements)){
            sqlField[0] = " t1.V12012 as MIN_TEM, t1.V12012_052 as MIN_TEM_OTime,";
            sqlField[1] = " V12012 < '900000'";
        }else if("MIN_VIS".equals(elements)){
            sqlField[0] = " t1.V20059 AS MIN_VIS, t1.V20059_052 as MIN_VIS_OTime,";
            sqlField[1] = "V20059 < '900000' and V20059 > '0'";
        }else if("MAX_PRS".equals(elements)){
            sqlField[0] = " t1.V10301 as MAX_PRS, t1.V10301_052 as MAX_PRE_OTime,";
            sqlField[1] = "V10301 < '900000'";
        }else if("MIN_PRS".equals(elements)){
            sqlField[0] = " t1.V10302 as MIN_PRS,t1.V10302_052 as MIN_PRS_OTime,";
            sqlField[1] = "V10302 < '900000'";
        }else  if("MAX_WIN_S_Max".equals(elements)){
            sqlField[0] = " t1.V11042 as MAX_WIN_S_Max, t1.V11042_052 as MAX_WIN_S_Max_OTime,";
            sqlField[1] = " V11042 < '900000'";
        }else if("MAX_WIN_S_Inst_Max".equals(elements)){
            sqlField[0] = " t1.V11046 as MAX_WIN_S_Inst_Max, t1.V11046_052 as MAX_WIN_S_Inst_Max_OTime,";
            sqlField[1] = " V11046 < '900000'";
        }
        return sqlField;
    }
}
