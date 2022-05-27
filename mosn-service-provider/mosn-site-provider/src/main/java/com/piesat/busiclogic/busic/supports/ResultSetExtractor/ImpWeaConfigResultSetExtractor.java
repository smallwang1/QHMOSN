package com.piesat.busiclogic.busic.supports.ResultSetExtractor;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.piesat.busiclogic.busic.entities.ImportWeaConfig;
import com.piesat.busiclogic.busic.entities.InterfaceType;
import com.piesat.busiclogic.busic.entities.MashupType;
import com.piesat.busiclogic.busic.entities.WeaDiscernConfig;

public class ImpWeaConfigResultSetExtractor implements ResultSetExtractor<List<ImportWeaConfig>> {

	private ObjectMapper objectMapper;
	
	private static Logger logger = LoggerFactory.getLogger(ImpWeaConfigResultSetExtractor.class);
	
	public ImpWeaConfigResultSetExtractor() {
		this.objectMapper = new ObjectMapper();
	}
	
	@Override
	public List<ImportWeaConfig> extractData(ResultSet rs) throws SQLException, DataAccessException {
		
		Map<String, ImportWeaConfig> config = new HashMap<String, ImportWeaConfig>();
		while (rs.next()) {
			rowMapper(rs, config);
		}
		return new ArrayList<ImportWeaConfig>(config.values());
	}
	
	public void rowMapper(ResultSet rs,Map<String, ImportWeaConfig> config) throws SQLException {
		
		String id = rs.getString("ID");
		ImportWeaConfig importWeaConfig = config.get(id);
		if(importWeaConfig == null) {
			importWeaConfig = new ImportWeaConfig();
			importWeaConfig.setId(id);
			importWeaConfig.setWeaElement(rs.getString("WEA_ELEMENT"));
			importWeaConfig.setWeaElementName(rs.getString("WEA_ELEMENT_NAME"));
			importWeaConfig.setLevelId(rs.getString("LEVEL_ID"));
			importWeaConfig.setMacthMon(rs.getString("MATCH_MONTH"));
			importWeaConfig.setEnabled(rs.getBoolean("ENABLED"));
			importWeaConfig.setImagePath(rs.getString("IMAGE_PATH"));
			importWeaConfig.setTargetInterface(rs.getString("TARGET_INTERFACE"));
			importWeaConfig.setInterfaceType(InterfaceType.valueOf(rs.getString("INTERFACE_TYPE")));
			HashMap<String, String> paramMap = new HashMap<String, String>();
			String params = rs.getString("PARAMS");
			try {
				MapLikeType type = objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, String.class);
				paramMap = objectMapper.readValue(params, type);
			} catch (IOException e) {
				logger.error("params trun to map fail .",e);
			}
			importWeaConfig.setParams(StringUtils.isBlank(params)?new HashMap<String, String>():paramMap);
		}
		WeaDiscernConfig weaDiscernConfig = new WeaDiscernConfig();
		weaDiscernConfig.setId("DIS_ID");
		weaDiscernConfig.setTargetElement(rs.getString("TARGET_ELEMENT"));
		weaDiscernConfig.setBeginTimeOffset(rs.getInt("BEGIN_TIME_OFFSET"));
		weaDiscernConfig.setEndTimeOffset(rs.getInt("END_TIME_OFFSET"));
		weaDiscernConfig.setMinValue(rs.getBigDecimal("MIN_VALUE"));
		weaDiscernConfig.setMaxValue(rs.getBigDecimal("MAX_VALUE"));
		weaDiscernConfig.setMashupType(MashupType.valueOf(rs.getString("MASHUP_TYPE")));
		
		List<WeaDiscernConfig> weaDiscernConfigs = importWeaConfig.getWeaDiscernConfigs();
		if(weaDiscernConfigs == null) {
			weaDiscernConfigs = new ArrayList<WeaDiscernConfig>();
		}
		weaDiscernConfigs.add(weaDiscernConfig);
		config.put(id, importWeaConfig);
	}

}
