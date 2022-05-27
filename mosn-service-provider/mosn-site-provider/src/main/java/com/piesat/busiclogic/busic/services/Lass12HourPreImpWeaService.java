package com.piesat.busiclogic.busic.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class Lass12HourPreImpWeaService extends ImpWeaService {

	@Override
	protected String getFirstConfigKey() {
		return "over50";
	}

	@Override
	protected void setConfig(Map<String, Map<String, Object>> config) {


		Map<String, Object> over50 = new HashMap<String, Object>();
		over50.put(minValue, 50);
		over50.put(maxValue, 9999);
		over50.put(type, "过去12小时降水量达到50毫米");
		over50.put(name, "强降水");
		over50.put(image, "strongprec1.png");
		over50.put(colKey, "V13021");


		config.put("over50", over50);

	}

	@Override
	protected String getCols() {
		return "D_DATETIME,V01301,V13021,v05001,v06001";
	}

	@Override
	protected String getTableName() {
		return "SURF_WEA_CHN_MUL_HOR_TAB";
	}

	@Override
	protected String getValueKey() {
		return "V13021";
	}

	@Override
	protected String getDBName() {
		return "cmadaasDataSource";
	}

	@Override
	protected String getImportWeatherName() {
		return "过去12小时降水";
	}

}
