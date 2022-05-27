/**
 * 项目名称:analysis
 * 类名称:JyInfo.java
 * 包名称:com.piesat.busiclogic.busic.analysis.dao.impl
 *
 * 修改履历:
 *       日期                   修正者      主要内容
 *  Thu Apr 02 16:30:26 CST 2020       admin         初版做成
 *
 * Copyright (c) 2007-2018
 */
package com.piesat.busiclogic.busic.analysis.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.busiclogic.busic.analysis.rowmapper.JyInfoRowMapper;
import com.piesat.busiclogic.busic.analysis.entity.JyInfo;
import com.piesat.busiclogic.busic.analysis.dao.IJyInfoDao;

/**
 * JyInfoDao接口实现类
 * @author admin
 * @version 1.0
 */
@Repository
public class JyInfoDaoImpl implements IJyInfoDao {
    
    // DAO类统一注入pageHelper类
    @Autowired
    private JdbcPageHelper pageHelper;
    
    /* (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysisdao.IJyInfoDao
     * insertJyInfo(com.piesat.busiclogic.busic.analysisentity.JyInfo)
     */
    @Override
    public int[] batchInsertJyInfo(List<JyInfo> jyInfoList) {

        StringBuilder sb = new StringBuilder(512);
        
        sb.append("INSERT INTO JY_INFO \n");
        sb.append("  (date, \n");
        sb.append("   description, \n");
        sb.append("   filename, \n");
        sb.append("   filetype, \n");
        sb.append("   id, \n");
        sb.append("   time) \n");
        sb.append("VALUES \n");
        sb.append("  (:date, \n");
        sb.append("   :description, \n");
        sb.append("   :filename, \n");
        sb.append("   :filetype, \n");
        sb.append("   :id, \n");
        sb.append("   :time) \n");

        Map<String,Object> []  paramsMap = new HashMap[jyInfoList.size()];
        for(int i=0;i<jyInfoList.size();i++){
            paramsMap[i]= getObjectMap(jyInfoList.get(i));
        }
        return pageHelper.batchUpdate(sb.toString(),paramsMap);
    }

    private Map<String,Object> getObjectMap(JyInfo jyInfo) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date",jyInfo.getDate());
        map.put("description",jyInfo.getDescription());
        map.put("filename",jyInfo.getFilename());
        map.put("filetype",jyInfo.getFiletype());
        map.put("id",jyInfo.getId());
        map.put("time",jyInfo.getTime());
        return map;
    }

    /* (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysisdao.IJyInfoDao
     updateJyInfo(com.piesat.busiclogic.busic.analysisentity.JyInfo)
     */
    @Override
    public int updateJyInfo (JyInfo jyInfo) {
        StringBuilder sb = new StringBuilder(1024);
        
        sb.append("  UPDATE JY_INFO  \n");
        sb.append("   SET date = :date, \n");
        sb.append("       description = :description, \n");
        sb.append("       filename = :filename, \n");
        sb.append("       filetype = :filetype, \n");
        sb.append("       id = :id, \n");
        sb.append("       time = :time \n");
        sb.append("  WHERE 1=1 \n");
        sb.append("");
        
        Map<String, Object> map = new HashMap<String, Object>();
         
        map.put("date",jyInfo.getDate());
        map.put("description",jyInfo.getDescription());
        map.put("filename",jyInfo.getFilename());
        map.put("filetype",jyInfo.getFiletype());
        map.put("id",jyInfo.getId());
        map.put("time",jyInfo.getTime());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }
    
    /* (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysisdao.IJyInfoDao
     * deleteJyInfo(java.lang.String)
     */
    @Override
    public int deleteJyInfo() {

        StringBuilder sb = new StringBuilder(512);
        
        sb.append("  DELETE FROM JY_INFO WHERE 1=1  \n");

        Map<String, Object> map = new HashMap<String, Object>();
        

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }
    
    /* (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysisdao.IJyInfoDao
     * findJyInfoById(java.lang.String)
     */
    @Override
    public JyInfo findJyInfoById() {
        
        StringBuilder sb = new StringBuilder(512);
        
        sb.append("  SELECT date , \n");
        sb.append("         description , \n");
        sb.append("         filename , \n");
        sb.append("         filetype , \n");
        sb.append("         id , \n");
        sb.append("         time  \n");
        sb.append("    FROM JY_INFO \n");
        sb.append("   WHERE 1=1 \n");
        sb.append("");

        Map<String, Object> map = new HashMap<String, Object>();
        

        List<JyInfo> listJyInfo = pageHelper.getJdbcTempalte().query(sb.toString(), map, new JyInfoRowMapper());
        // 按ID取，仅取到一条记录时正常。 否则返回空，由调用方判断。不自定义异常。
        if (listJyInfo.size()==1) {
            return listJyInfo.get(0);
        } else {
            return null;
        }
    }
    
    /* (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysisdao.IJyInfoDao
     * findPageJyInfo(long, int)
     */
    @Override
    public Page<JyInfo> findPageJyInfo(long currentPage, int pageSize,JyInfo jyInfo) {

        StringBuilder sb = new StringBuilder(512);
        
        sb.append("  SELECT date, \n");
        sb.append("         description, \n");
        sb.append("         filename, \n");
        sb.append("         filetype, \n");
        sb.append("         id, \n");
        sb.append("         time \n");
        sb.append("    FROM JY_INFO \n");
        sb.append("   WHERE 1=1 \n");
        sb.append("");

        Map<String, Object> map = new HashMap<String, Object>();

        
        return pageHelper.pagingFind(sb.toString(), map, new JyInfoRowMapper(), currentPage, pageSize);
    }
    
}