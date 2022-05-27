package com.piesat.busiclogic.busic.services;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class HailImpWeaService extends ImpWeaService {

	@Override
	protected String getFirstConfigKey() {
		return "exit";
	}

	@Override
	protected void setConfig(Map<String, Map<String, Object>> config) {

		Map<String, Object> exit = new HashMap<String, Object>();
		exit.put(minValue, 0);
		exit.put(maxValue, 1);
		exit.put(name, "冰雹");
		exit.put(image, "hail.png");
		exit.put(colKey, "V20302_089");
		exit.put("unit","123");

		config.put("exit", exit);
	}

	@Override
	protected String getCols() {
		return "D_DATETIME,V01301,V20302_089";
	}

	@Override
	protected String getTableName() {
		return "SURF_WEA_CHN_MUL_DAY_TAB";
	}

	@Override
	protected String getValueKey() {
		return "V20302_089";
	}

	@Override
	protected String getDBName() {
		return "cmadaasDataSource";
	}

	@Override
	protected String getImportWeatherName() {
		return "冰雹";
	}

	@Override
	protected String getCondition(Date dateTime) {
		dateTime = DateUtils.addDays(dateTime, -1);
		return " D_DATETIME  = '"+DateFormatUtils.format(dateTime, "yyyy-MM-dd")+"' and v01301 like '5%' ";
	}

}
