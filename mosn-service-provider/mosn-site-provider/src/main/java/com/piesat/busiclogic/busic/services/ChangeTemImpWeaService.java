package com.piesat.busiclogic.busic.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChangeTemImpWeaService extends ImpWeaService {

	@Override
	protected String getFirstConfigKey() {
		return "under14";
	}

	@Override
	protected void setConfig(Map<String, Map<String, Object>> config) {

		Map<String, Object> under8 = new HashMap<String, Object>();
		under8.put(minValue, -11);
		under8.put(maxValue, -8);
		under8.put(type, "24小时变温-11℃≤X<-8℃");
		under8.put(name, "24小时变温");
		under8.put(image, "cooling1.png");
		under8.put(colKey, "V12405");

		Map<String, Object> under11 = new HashMap<String, Object>();
		under11.put(minValue, -14);
		under11.put(maxValue, -11);
		under11.put(colKey, "V12405");
		under8.put(type, "24小时变温-14℃≤X<-11℃");
		under8.put(name, "24小时变温");
		under11.put(image, "cooling2.png");
		under11.put(next, "under8");

		Map<String, Object> under14 = new HashMap<String, Object>();
		under14.put(minValue, -999);
		under14.put(maxValue, -14);
		under14.put(next, "under11");
		under8.put(type, "24小时变温X<-14℃");
		under8.put(name, "24小时变温");
		under14.put(image, "cooling3.png");
		under14.put(colKey, "V12405");

		config.put("under8", under8);
		config.put("under11", under11);
		config.put("under14", under14);
	}

	@Override
	protected String getCols() {
		return "D_DATETIME,V01301,V12405,v05001,v06001";
	}

	@Override
	protected String getTableName() {
		return "SURF_WEA_CHN_MUL_HOR_TAB";
	}

	@Override
	protected String getValueKey() {
		return "V12405";
	}

	@Override
	protected String getDBName() {
		return "cmadaasDataSource";
	}

	@Override
	protected String getImportWeatherName() {
		return "24小时变温";
	}

}
