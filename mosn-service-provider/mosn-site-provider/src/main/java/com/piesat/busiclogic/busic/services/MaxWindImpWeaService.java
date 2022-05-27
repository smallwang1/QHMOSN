package com.piesat.busiclogic.busic.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MaxWindImpWeaService extends ImpWeaService {

	@Override
	protected String getFirstConfigKey() {
		return "over40";
	}

	@Override
	protected void setConfig(Map<String, Map<String, Object>> config) {

		Map<String, Object> over17 = new HashMap<String, Object>();
		over17.put(minValue, 17);
		over17.put(maxValue, 24);
		over17.put(type, "1小时极大风速在17m/s以上");
		over17.put(name, "极大风速");
		over17.put(image, "bigwind1.png");
		over17.put(colKey, "V11046");

		Map<String, Object> over24 = new HashMap<String, Object>();
		over24.put(minValue, 24);
		over24.put(maxValue, 32);
		over24.put(colKey, "V11046");
		over24.put(type, "1小时极大风速在24m/s以上");
		over24.put(name, "极大风速");
		over24.put(image, "bigwind2.png");
		over24.put(next, "over17");

		Map<String, Object> over32 = new HashMap<String, Object>();
		over32.put(minValue, 32);
		over32.put(maxValue, 40);
		over32.put(colKey, "V11046");
		over32.put(type, "1小时极大风速在32m/s以上");
		over32.put(name, "极大风速");
		over32.put(image, "bigwind3.png");
		over32.put(next, "over24");

		Map<String, Object> over40 = new HashMap<String, Object>();
		over40.put(minValue, 40);
		over40.put(maxValue, 9999);
		over40.put(colKey, "V11046");
		over40.put(type, "1小时极大风速在40m/s以上");
		over40.put(name, "极大风速");
		over40.put(image, "bigwind4.png");
		over40.put(next, "over32");


		config.put("over40", over40);
		config.put("over32", over32);
		config.put("over24", over24);
		config.put("over17", over17);

	}

	@Override
	protected String getCols() {
		return "D_DATETIME,V01301,V11046,v05001,v06001";
	}

	@Override
	protected String getTableName() {
		return "SURF_WEA_CHN_MUL_HOR_TAB";
	}

	@Override
	protected String getValueKey() {
		return "V11046";
	}

	@Override
	protected String getDBName() {
		return "cmadaasDataSource";
	}

	@Override
	protected String getImportWeatherName() {
		return "极大风速";
	}

}
