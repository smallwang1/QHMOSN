package com.piesat.busiclogic.busic.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Lass3HourPreImpWeaService extends ImpWeaService {

	@Override
	protected String getFirstConfigKey() {
		return "over100";
	}

	@Override
	protected void setConfig(Map<String, Map<String, Object>> config) {


		Map<String, Object> over50 = new HashMap<String, Object>();
		over50.put(minValue, 50);
		over50.put(maxValue, 100);
		over50.put(type, "过去3小时降水量达到50毫米");
		over50.put(name, "强降水");
		over50.put(unit, "mm");
		over50.put(image, "strongprec3.png");
		over50.put(colKey, "V13020");

		Map<String, Object> over100 = new HashMap<String, Object>();
		over100.put(minValue, 100);
		over100.put(maxValue, 9999);
		over50.put(unit, "mm");
		over100.put(next, "over50");
		over100.put(type, "过去3小时降水量达到100毫米");
		over100.put(name, "过去3小时降水量");
		over100.put(image, "strongprec4.png");
		over100.put(colKey, "V13020");

		config.put("over50", over50);
		config.put("over100", over100);
	}

	@Override
	protected String getCols() {
		return "D_DATETIME,V01301,V13020,v05001,v06001";
	}

	@Override
	protected String getTableName() {
		return "SURF_WEA_CHN_MUL_HOR_TAB";
	}

	@Override
	protected String getValueKey() {
		return "V13020";
	}

	@Override
	protected String getDBName() {
		return "cmadaasDataSource";
	}

	@Override
	protected String getImportWeatherName() {
		return "过去3小时降水";
	}
}
