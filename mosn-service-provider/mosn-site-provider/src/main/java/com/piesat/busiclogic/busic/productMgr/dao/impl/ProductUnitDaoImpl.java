package com.piesat.busiclogic.busic.productMgr.dao.impl;

import com.piesat.busiclogic.busic.productMgr.dao.ProductUnitDao;
import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProductUnitDaoImpl implements ProductUnitDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public int addProductUnit(ProductUnit productUnit) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  MGR_PRODUCT_UNIT");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("SCALE,\n");
        sb.append("DESCRIBTION,\n");
        sb.append("FMS_ID,\n");
        sb.append("PARAM,\n");
        sb.append("PARAM_POINT,\n");
        sb.append("NAME,\n");
        sb.append("URL,\n");
        sb.append("TIME_ZONE,\n");
        sb.append("URL_TYPE,\n");
        sb.append("FORMAT,\n");
        sb.append("ANALYSIS_DATA,\n");
        sb.append("TIME_FIELD,\n");
        sb.append("TIME_FIELD_POINT,\n");
        sb.append("TIME_TYPE,\n");
        sb.append("ROUTER,\n");
        sb.append("SHOW_PATTERN,\n");
        sb.append("TIME_FORMAT,\n");
        sb.append("REQUEST_TYPE,\n");
        sb.append("DEFAULT_TIME,\n");
        sb.append("DEFAULT_TIME_POINT,\n");
        sb.append("RELATEID \n");
        sb.append(" ) values (");
        sb.append(":ID,\n");
        sb.append(":SCALE,\n");
        sb.append(":DESCRIBTION,\n");
        sb.append(":FMS_ID,\n");
        sb.append(":PARAM,\n");
        sb.append(":PARAM_POINT,\n");
        sb.append(":NAME,\n");
        sb.append(":URL,\n");
        sb.append(":TIME_ZONE,\n");
        sb.append(":URL_TYPE,\n");
        sb.append(":FORMAT,\n");
        sb.append(":ANALYSIS_DATA,\n");
        sb.append(":TIME_FIELD,\n");
        sb.append(":TIME_FIELD_POINT,\n");
        sb.append(":TIME_TYPE,\n");
        sb.append(":ROUTER,\n");
        sb.append(":SHOW_PATTERN,\n");
        sb.append(":TIME_FORMAT,\n");
        sb.append(":REQUEST_TYPE,\n");
        sb.append(":DEFAULT_TIME,\n");
        sb.append(":DEFAULT_TIME_POINT,\n");
        sb.append(":RELATEID) \n");


        Map<String,Object> map = new HashMap<>();
        map.put("ID", productUnit.getId());
        map.put("SCALE", productUnit.getScale());
        map.put("DESCRIBTION", productUnit.getDescribtion());
        map.put("FMS_ID",productUnit.getFms_id());
        map.put("PARAM", productUnit.getParam());
        map.put("PARAM_POINT", productUnit.getParam_point());
        map.put("NAME", productUnit.getName());
        map.put("URL", productUnit.getUrl());
        map.put("TIME_ZONE", productUnit.getTime_zone());
        map.put("URL_TYPE", productUnit.getUrl_type());
        map.put("REQUEST_TYPE", productUnit.getRequest_type());
        map.put("RELATEID",productUnit.getRelateid());
        map.put("FORMAT", productUnit.getFormat());
        map.put("SHOW_PATTERN",productUnit.getShow_pattern());
        map.put("TIME_FORMAT",productUnit.getTime_format());
        map.put("ANALYSIS_DATA", productUnit.getAnalysis_data());

        map.put("DEFAULT_TIME_POINT",productUnit.getDefault_time_point());
        map.put("DEFAULT_TIME", productUnit.getDefault_time());

        map.put("TIME_FIELD",productUnit.getTime_field());
        map.put("TIME_FIELD_POINT",productUnit.getTime_field_point());
        String[] time_type = productUnit.getTime_type();
        map.put("TIME_TYPE",StringUtils.join(time_type, ","));
        map.put("ROUTER",productUnit.getRouter());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int deleteProductUnit(ProductUnit productUnit) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("delete from MGR_PRODUCT_UNIT where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", productUnit.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int editProductUnit(ProductUnit productUnit) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update MGR_PRODUCT_UNIT set");
        if(!PublicUtil.isEmpty(productUnit.getScale())){
            sb.append(" SCALE=:SCALE ,");
        }
        sb.append(" DESCRIBTION=:DESCRIBTION,");
        if(!PublicUtil.isEmpty(productUnit.getFms_id())){
            sb.append(" FMS_ID=:FMS_ID, ");
        }
        sb.append(" NAME=:NAME, ");
        if(!PublicUtil.isEmpty(productUnit.getUrl())){
            sb.append(" URL=:URL, ");
        }
        if(!PublicUtil.isEmpty(productUnit.getRequest_type())){
            sb.append(" REQUEST_TYPE=:REQUEST_TYPE, ");
        }
        if(!PublicUtil.isEmpty(productUnit.getStatus())){
            sb.append(" RELATEID=:RELATEID,");
        }
        sb.append(" PARAM=:PARAM,");
        sb.append(" PARAM_POINT=:PARAM_POINT,");
        sb.append(" TIME_ZONE=:TIME_ZONE,");
        if(!PublicUtil.isEmpty(productUnit.getFormat())){
            sb.append(" FORMAT=:FORMAT, ");
        }
        sb.append(" ANALYSIS_DATA=:ANALYSIS_DATA, ");
        sb.append(" TIME_FIELD=:TIME_FIELD, ");
        sb.append(" TIME_FIELD_POINT=:TIME_FIELD_POINT, ");
        sb.append(" SHOW_PATTERN=:SHOW_PATTERN, ");
        sb.append(" TIME_FORMAT=:TIME_FORMAT, ");
        if(!PublicUtil.isEmpty(productUnit.getRouter())){
            sb.append(" ROUTER=:ROUTER,");
        }
        if(!PublicUtil.isEmpty(productUnit.getTime_type())){
            sb.append(" TIME_TYPE=:TIME_TYPE,");
        }
        if(!PublicUtil.isEmpty(productUnit.getUrl_type())){
            sb.append(" URL_TYPE=:URL_TYPE,");
        }
        sb.append(" DEFAULT_TIME=:DEFAULT_TIME,");
        sb.append(" DEFAULT_TIME_POINT=:DEFAULT_TIME_POINT,");
        sb.append(" STATUS=:STATUS");
        sb.append(" WHERE ID =:ID ");

        Map<String,Object> map = new HashMap<>();
        map.put("SCALE", productUnit.getScale());
        map.put("DESCRIBTION", productUnit.getDescribtion());
        map.put("FMS_ID", productUnit.getFms_id());
        map.put("RELATEID", productUnit.getRelateid());
        map.put("NAME", productUnit.getName());
        map.put("TIME_ZONE", productUnit.getTime_zone());
        map.put("PARAM", productUnit.getParam());
        map.put("PARAM_POINT", productUnit.getParam_point());
        map.put("URL", productUnit.getUrl());
        map.put("REQUEST_TYPE", productUnit.getRequest_type());
        map.put("STATUS", productUnit.getStatus());
        map.put("SHOW_PATTERN", productUnit.getShow_pattern());
        map.put("TIME_FORMAT",productUnit.getTime_format());
        map.put("ID", productUnit.getId());
        map.put("DEFAULT_TIME", productUnit.getDefault_time());
        map.put("DEFAULT_TIME_POINT", productUnit.getDefault_time_point());
        map.put("FORMAT", productUnit.getFormat());
        map.put("ANALYSIS_DATA", productUnit.getAnalysis_data());
        map.put("TIME_FIELD", productUnit.getTime_field());
        map.put("TIME_FIELD_POINT", productUnit.getTime_field_point());
        String timeType = "";
        if(!PublicUtil.isEmpty(productUnit.getTime_type())){
            String[] time_type = productUnit.getTime_type();
            map.put("TIME_TYPE",  StringUtils.join(time_type,","));
        }else{
            map.put("TIME_TYPE", productUnit.getTime_type());
        }
        map.put("URL_TYPE", productUnit.getUrl_type());
        map.put("ROUTER", productUnit.getRouter());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public ProductUnit getProductUnitById(ProductUnit productUnit) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" MGR_PRODUCT_UNIT where ID='"+productUnit.getId()+"'");
        List<Map<String,Object>> listMap= pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
        ProductUnit productUnit1 = new ProductUnit();
        if(listMap.size()>0){
            Map map = listMap.get(0);
            productUnit1.setId(String.valueOf(map.get("ID")));
            productUnit1.setScale(String.valueOf(map.get("SCALE")));
            productUnit1.setDescribtion(String.valueOf(map.get("DESCRIBTION")));
            productUnit1.setFms_id(String.valueOf(map.get("FMS_ID")));
            productUnit1.setParam(String.valueOf(map.get("PARAM")));
            productUnit1.setRelateid(String.valueOf(map.get("RELATEID")));
            productUnit1.setStatus(String.valueOf(map.get("STATUS")));
            productUnit1.setName(String.valueOf(map.get("NAME")));
            productUnit1.setUrl(String.valueOf(map.get("URL")));
            productUnit1.setRequest_type(String.valueOf(map.get("REQUEST_TYPE")));
            productUnit1.setFormat(String.valueOf(map.get("FORMAT")));
            productUnit1.setAnalysis_data(String.valueOf(map.get("analysis_data")));
            productUnit1.setTime_field(String.valueOf(map.get("TIME_FIELD")));
            productUnit1.setRouter(String.valueOf(map.get("ROUTER")));
            productUnit1.setUrl_type(String.valueOf(map.get("URL_TYPE")));
            productUnit1.setShow_pattern(String.valueOf(map.get("SHOW_PATTERN")));
            productUnit1.setTime_format(String.valueOf(map.get("TIME_FORMAT")));

            productUnit1.setDefault_time(String.valueOf(map.get("DEFAULT_TIME")));
            productUnit1.setDefault_time_point(String.valueOf(map.get("DEFAULT_TIME_POINT")));

            if(!PublicUtil.isEmpty(map.get("TIME_TYPE"))){
                productUnit1.setTime_type(String.valueOf(map.get("TIME_TYPE")).split(","));
            }
            productUnit1.setTime_zone(String.valueOf(map.get("TIME_ZONE")));
            productUnit1.setParam_point(String.valueOf(map.get("PARAM_POINT")));
            productUnit1.setTime_field_point(String.valueOf(map.get("TIME_FIELD_POINT")));
        }
        return  productUnit1;
    }

    @Override
    public List<Map<String, Object>> getHtmlTemplate(String tempId) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from REPORT_TEMPLATE where TEMPLATE_ID =:TEMPLATE_ID");
        Map<String,Object> map = new HashMap<>();
        map.put("TEMPLATE_ID", tempId);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map);
    }

    @Override
    public List<Map<String, Object>> getHtmlContent(String htmlName, String time) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from REPORT_INFO where REPORT_TITLE=:htmlName AND REPORT_TIME like '%'+:time+'%'  order by REPORT_TIME desc");
        Map<String,Object> map = new HashMap<>();
        map.put("htmlName",htmlName);
        map.put("time",time);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map);
    }
}
