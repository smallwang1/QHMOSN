package com.piesat.busiclogic.busic.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class VisiImpWeaService extends ImpWeaService {

	@Override
	protected String getFirstConfigKey() {
		return "over50";
	}

	@Override
	protected void setConfig(Map<String, Map<String, Object>> config) {



		Map<String, Object> over500 = new HashMap<String, Object>();
		over500.put(minValue, 200);
		over500.put(maxValue, 500);
		over500.put(type, "最小能见度在500米以下");
		over500.put(name, "能见度");
		over500.put(unit, "米");
		over500.put(image, "vis3.png");
		over500.put(colKey, "V20059");


		Map<String, Object> over200 = new HashMap<String, Object>();
		over200.put(minValue, 50);
		over200.put(maxValue, 200);
		over200.put(colKey, "V20059");
		over200.put(type, "最小能见度在200米以下");
		over200.put(name, "能见度");
		over500.put(unit, "米");
		over200.put(image, "vis2.png");
		over200.put(next, "over500");

		Map<String, Object> over50 = new HashMap<String, Object>();
		over50.put(minValue, 0);
		over50.put(maxValue, 50);
		over50.put(colKey, "V20059");
		over50.put(type, "最小能见度在50米以下");
		over50.put(name, "能见度");
		over500.put(unit, "米");
		over50.put(image, "vis1.png");
		over50.put(next, "over200");


		config.put("over50", over50);
		config.put("over200", over200);
		config.put("over500", over500);


	}

	@Override
	protected String getCols() {
		return "D_DATETIME,V01301,V20059,v05001,v06001";
	}

	@Override
	protected String getTableName() {
		return "SURF_WEA_CHN_MUL_HOR_TAB";
	}

	@Override
	protected String getValueKey() {
		return "V20059";
	}

	@Override
	protected String getDBName() {
		return "cmadaasDataSource";
	}

	@Override
	protected String getImportWeatherName() {
		return "能见度";
	}

}
