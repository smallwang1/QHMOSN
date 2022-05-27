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
public class ThresholdRepository {
	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	private final static String BASE_SQL = "SELECT V01301,%s FROM %s WHERE %s";
	
	/**
	 * 
	 * @param tableName  查询数据表 表名
	 * @param dataSourceName 查询数据源名称（必须在dataSource.xml中配置才能生效，否则采用默认数据源）
	 * @param cols  查询字段名称
	 * @param condtion 查询条件，包括条件、排序、分组，如果cols里面包含聚合函数，必须包含GROUP BY V01301
	 * @return 返回按站的符合条件的自动站阈值
	 */
	public List<Map<String, String>> findAllSceceDataByCondition(String tableName,
			String dataSourceName,String cols,String condtion) {
		
		if(!StringUtils.isBlank(dataSourceName)) {
			DBContextHolder.setDataSourceName(dataSourceName);
		}
		return jdbcTemplate.query(String.format(BASE_SQL, cols,tableName,condtion), new ResultSetExtractor<List<Map<String, String>>>() 
				{

					@Override
					public List<Map<String, String>> extractData(ResultSet rs) throws SQLException, DataAccessException {
						
						ResultSetMetaData metaData = rs.getMetaData();
						int columnCount = metaData.getColumnCount();
						List<String> colNames = new ArrayList<String>();
						for (int i = 1; i <= columnCount; i++) {
							colNames.add(metaData.getColumnName(i));
						}
						
						List<Map<String, String>> list = new ArrayList<Map<String,String>>();
						while (rs.next()) {
							
							Map<String, String> map = new HashMap<String, String>(32);
							for (int j = 0; j < colNames.size(); j++) {
								
								String colName = colNames.get(j);
								map.put(colName, rs.getString(colName));
							}
							list.add(map);
						}
						return list;
					}
				}
		);
	}

}
