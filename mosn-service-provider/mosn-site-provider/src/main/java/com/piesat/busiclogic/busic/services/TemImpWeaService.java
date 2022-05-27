package com.piesat.busiclogic.busic.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TemImpWeaService extends ImpWeaService {

	@Override
	protected String getFirstConfigKey() {
		return "over40";
	}

	@Override
	protected void setConfig(Map<String, Map<String, Object>> config) {

		Map<String, Object> over35 = new HashMap<String, Object>();
		over35.put(minValue, 35);
		over35.put(maxValue, 37);
		over35.put(type, "1小时最高温达到35℃");
		over35.put(name, "最高气温");
		over35.put(image, "hightemp1.png");
		over35.put(colKey, "V12011");

		Map<String, Object> over37 = new HashMap<String, Object>();
		over37.put(minValue, 37);
		over37.put(maxValue, 40);
		over37.put(colKey, "V12011");
		over37.put(type, "1小时最高温达到37℃");
		over37.put(name, "最高气温");
		over37.put(image, "hightemp2.png");
		over37.put(next, "over35");

		Map<String, Object> over40 = new HashMap<String, Object>();
		over40.put(minValue, 40);
		over40.put(maxValue, 200);
		over40.put(next, "over37");
		over40.put(type, "1小时最高温达到40℃");
		over40.put(name, "最高气温");
		over40.put(image, "hightemp3.png");
		over40.put(colKey, "V12011");

		config.put("over35", over35);
		config.put("over37", over37);
		config.put("over40", over40);
	}

	@Override
	protected String getCols() {
		return "D_DATETIME,V01301,V12011,v05001,v06001";
	}

	@Override
	protected String getTableName() {
		return "SURF_WEA_CHN_MUL_HOR_TAB";
	}

	@Override
	protected String getValueKey() {
		return "V12011";
	}

	@Override
	protected String getDBName() {
		return "cmadaasDataSource";
	}

	@Override
	protected String getImportWeatherName() {
		return "最高温";
	}

}
