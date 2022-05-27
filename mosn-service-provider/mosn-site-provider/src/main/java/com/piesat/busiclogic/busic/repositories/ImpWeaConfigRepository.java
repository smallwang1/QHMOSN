package com.piesat.busiclogic.busic.repositories;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.piesat.busiclogic.busic.entities.ImportWeaConfig;
import com.piesat.busiclogic.busic.supports.ResultSetExtractor.ImpWeaConfigResultSetExtractor;

@Repository
public class ImpWeaConfigRepository {

	
	@Resource
	private JdbcTemplate jdbcTemplate;
	
	private static final String QUERT_SQL = "SELECT A.ID,A.WEA_ELEMENT,A.WEA_ELEMENT_NAME,A.LEVEL_ID,A.MATCH_MONTH,"
			+ "A.ENABLED,A.IMAGE_PATH,A.PARAMS,B.ID AS DIS_ID,B.TARGET_ELEMENT,A.TARGET_INTERFACE,B.BEGIN_TIME_OFFSET,"
			+ "B.END_TIME_OFFSET,B.MIN_VALUE,B.MAX_VALUE,A.INTERFACE_TYPE,B.MASHUP_TYPE" + 
			"  FROM CONFIG_IMPORTANT_WEATHER A,CONFIG_WEA_DISCERN B WHERE A.ID=B.IMP_WEA_ID";
	
	public List<ImportWeaConfig> getAllImportWeaConfig() {
		return jdbcTemplate.query(QUERT_SQL, new ImpWeaConfigResultSetExtractor());
	}
}
