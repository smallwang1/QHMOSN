package com.piesat.busiclogic.busic.repositories;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import com.piesat.jdbc.dataSource.DBContextHolder;

@Repository
public class BaseAlgStaRepository {

	@Resource
	private JdbcTemplate jdbcTemplate;
	
	private static final String QUERY_DATA_SQL = "SELECT %s FROM %s WHERE D_DATETIME = ? ";
	
	private static final String ADD_CONDITION_PART = " AND V01301 IN (%s) ";
	
	public List<Map<String, String>> getAllData(String tableName,String cols,String time,List<String> stations) {
		
		DBContextHolder.setDataSourceName("cmadaasDataSource");
		String perpared = getPerpared(stations);
		String sql = String.format(QUERY_DATA_SQL, cols, tableName);
		
		Object[] objects = new Object[] {time};
		
		if(StringUtils.isNotBlank(perpared)) {
			sql = sql + String.format(ADD_CONDITION_PART, perpared);
			objects = new Object[stations.size() + 1];
			objects[0] = time;
			for (int i = 0; i < stations.size(); i++) {
				objects[i + 1] = stations.get(i);
			}
		}
		
		return jdbcTemplate.query(sql, objects, new ResultSetExtractor<List<Map<String, String>>>(){

			@Override
			public List<Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				ResultSetMetaData metaData = rs.getMetaData();
			    int columnCount = metaData.getColumnCount();
			    List<String> colNames = new ArrayList<>();
			    for (int i = 1; i <= columnCount; i++) {
			    	colNames.add(metaData.getColumnName(i)); 
			    }
			    List<Map<String, String>> list = new ArrayList<Map<String, String>>();
			    while (rs.next()) {
			    	Map<String, String> map = new HashMap<String, String>();
					for (int i = 0; i < colNames.size(); i++ ) {
						String col = colNames.get(i);
						map.put(col, rs.getString(col));
					}
					list.add(map);
				}
			    return list;
			}});
	}
	
	
	private static String getPerpared(List<String> stations) {
		
		if(stations == null) {
			return null;
		}
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < stations.size(); i++) {
			list.add("?");
		}
		return StringUtils.join(list,",");
	}
}
