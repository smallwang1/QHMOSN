/**
 * 项目名称:analysis
 * 类名称:JyInfo.java
 * 包名称:com.piesat.busiclogic.busic.analysis.rowmapper
 *
 * 修改履历:
 *       日期                   修正者      主要内容
 *  Thu Apr 02 16:30:26 CST 2020       admin         初版做成
 *
 * Copyright (c) 2007-2018
 */

package com.piesat.busiclogic.busic.analysis.rowmapper;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.jdbc.core.RowMapper;

import com.piesat.busiclogic.busic.analysis.entity.JyInfo;

/**
 * JyInfo  实体映射类 <br>
 * 表 JY_INFO 的实体映射类. <br>
 * @author admin
 * @version 1.0
 */
public class JyInfoRowMapper implements RowMapper<JyInfo>{
    
    /*
     * (non-Javadoc)
     * @see org.springframework.jdbc.core.RowMapper
     */
    @Override
    public JyInfo mapRow(ResultSet rs, int rowNum)
        throws SQLException {

        JyInfo jyInfo=new JyInfo();

        jyInfo.setDate(rs.getString("date"));
        jyInfo.setDescription(rs.getString("description"));
        jyInfo.setFilename(rs.getString("filename"));
        jyInfo.setFiletype(rs.getString("filetype"));
        jyInfo.setId(rs.getString("id"));
        jyInfo.setTime(rs.getString("time"));

        return jyInfo;
    }
    
}