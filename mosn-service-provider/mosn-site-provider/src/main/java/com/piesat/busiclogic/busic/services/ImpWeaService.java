package com.piesat.busiclogic.busic.services;

import com.piesat.busiclogic.busic.entities.ImportWeaConfig;
import com.piesat.busiclogic.busic.entities.StationInfo;
import com.piesat.busiclogic.busic.repositories.ImpWeaConfigRepository;
import com.piesat.busiclogic.busic.repositories.StationInfoRepository;
import com.piesat.busiclogic.busic.supports.responseExtractor.ForWordResponseExtractor;
import com.piesat.common.util.SignGenUtil;
import com.piesat.jdbc.dataSource.DBContextHolder;
import com.piesat.jdbc.repository.CommonRepository;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


public abstract class ImpWeaService {

	@Resource
	private ImpWeaConfigRepository impWeaConfigRepository;

	@Resource
	private StationInfoRepository stationInfoRepository;

	@Resource
	private CommonRepository commonRepository;

	private static Logger logger = LoggerFactory.getLogger(ImpWeaService.class);

	private  Map<String, Map<String, Object>> config = new HashMap<String, Map<String,Object>>();

	private Map<String, Map<String, Object>> data = new ConcurrentHashMap<String, Map<String,Object>>();

	protected static final String dateTimeKey = "D_DATETIME";

	protected static final String colKey = "key";

	protected static final String minValue = "minValue";

	protected static final String maxValue = "maxValue";

	protected static final String image = "image";

	protected static final String name = "name";

	protected static final String tips_name = "tips_name";

	protected static final String unit = "unit";

	protected static final String type = "type";

	protected static final String next = "next";

	private static final String v01301 = "V01301";

	protected abstract String getFirstConfigKey();

	protected abstract void setConfig(Map<String, Map<String, Object>> config);

	protected abstract String getCols();

	protected abstract String getTableName();

	protected abstract String getImportWeatherName();



	protected  String getCondition(Date dateTime) {

//		return " D_DATETIME between '2021-01-01 00:00:00' and '2021-01-02 01:00:00' and v01301 like '5%' ";
		return String.format(" D_DATETIME = '%s' and v01301 like '%s'  ", DateFormatUtils.format(dateTime, "yyyy-MM-dd HH")+":mm:ss","5%");
	}

	protected abstract String getDBName();

	protected abstract String getValueKey();

	public Map<String, Map<String, Object>> getData() {
		return data;
	}


	public Map<String, Map<String, Object>> selectHourSurfImportData(Date date) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		logger.info("开始识别 {} 类型重要天气！",getImportWeatherName());
		Map<String, Map<String, Object>> tempData = new ConcurrentHashMap<String, Map<String,Object>>();
		setConfig(config);
		DBContextHolder.setDataSourceName(getDBName());
		List<Map<String,Object>> list = commonRepository.queryDataForList(getCols(), getTableName(), getCondition(date == null?new Date():date));
		DBContextHolder.setDataSourceName("dataSource");
		Map<String, StationInfo> stationInfos = stationInfoRepository.getAllStationInfo();
		for (int i = 0; i <list.size(); i++) {
			Map<String, Object> map = list.get(i);
			Object station = map.get(v01301);
			if(station == null) {
				continue;
			}
			String stationId = station.toString();
			Map<String, Object> result = new HashMap<String, Object>();
			String firstConfigKey = getFirstConfigKey();
			compareValue(firstConfigKey, map, result);
			if(!result.isEmpty()) {
				StationInfo stationInfo = stationInfos.get(stationId);
				if(stationInfo == null) {
					logger.info("识别出重要天气，但未找到站点信息，站点: {}",station);
					continue;
				}
				Map<String,String> describe = BeanUtils.describe(stationInfo);
				result.putAll(describe);
				tempData.put(station.toString(), result);
			}
		}
		if (date == null) {
			data.putAll(tempData);
		}
		logger.info(" {} 类型重要天气识别完成！",getImportWeatherName());
		return tempData;
	}

	private void compareValue(String configKey,Map<String, Object> map,Map<String, Object> result) {

		if(StringUtils.isBlank(configKey)) {
			return;
		}
		Map<String, Object> configMap = config.get(configKey);
		if(configMap == null || configMap.isEmpty()) {
			return;
		}
		Object valueObject = map.get(configMap.get(colKey));
		if(valueObject != null) {
			BigDecimal valueBigDecimal = getValueBigDecimal(valueObject);
			BigDecimal minBigDecimal = getValueBigDecimal(configMap.get(minValue));
			BigDecimal maxBigDecimal = getValueBigDecimal(configMap.get(maxValue));
			if(valueBigDecimal.compareTo(minBigDecimal) > 0 && valueBigDecimal.compareTo(maxBigDecimal) <= 0) {
				result.putAll(map);
				result.put("data", valueObject);
				result.put("key", configKey);
				result.put(name, configMap.get(name));
				if (configMap.containsKey(type)) {
					result.put(type, configMap.get(type));
				}
				result.put(image, configMap.get(image));
				return;
			}else {
				Object nextObject = configMap.get(next);
				compareValue(nextObject == null?null:nextObject.toString(), map, result);
			}
		}
	}

	private static BigDecimal getValueBigDecimal(Object obj) {
		try {
			return new BigDecimal(obj.toString());
		} catch (Exception e) {
			return BigDecimal.valueOf(999999);
		}
	}

	public Map<String,List<Map<String,String>>> getAllImportWeaInfo(Date dateTime) throws RestClientException, URISyntaxException {

		List<ImportWeaConfig> weaConfig = impWeaConfigRepository.getAllImportWeaConfig();

		for (int i = 0; i < weaConfig.size(); i++) {
			ImportWeaConfig importWeaConfig = weaConfig.get(i);

			RestTemplate restTemplate = new RestTemplate();

			MultiValueMap<String,String> multiValueMap = new HttpHeaders();
			multiValueMap.add("Content-Type", "application/json; charset=UTF-8");
			multiValueMap.add("Accept", "*/*");
			multiValueMap.add("Accept", MediaType.APPLICATION_JSON.toString());
			String url  = importWeaConfig.getTargetInterface();
			HashMap<String,String> paramsMap = importWeaConfig.getParams();
			paramsMap.put("nonce", UUID.randomUUID().toString());
			paramsMap.put("timestamp", System.currentTimeMillis()+"");
			paramsMap.put("userId", "USR_DuoYuanSys");
			paramsMap.put("pwd", "DuoYuan123");
			paramsMap.put("sign", SignGenUtil.getSign(paramsMap));
			paramsMap.remove("pwd");
			encodeParams(paramsMap);
			url = url + (StringUtils.endsWith(url, "?") ? "":"?") + getParams(paramsMap);
			HttpEntity<String> httpEntity = new HttpEntity<String>("",multiValueMap);
			RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity);
			ForWordResponseExtractor forWordResponseExtractor = new ForWordResponseExtractor();
			String data = restTemplate.execute(new URI(url),  HttpMethod.GET, requestCallback, forWordResponseExtractor);
		}
		return null;
	}

	private static void encodeParams(HashMap<String, String> paramsMap) {

		for (Entry<String, String> entry : paramsMap.entrySet()) {
			paramsMap.put(entry.getKey(), URLEncoder.encode(entry.getValue()));
		}
	}

	private static String getParams(Map< String, String> map) {

		List<String> list = new ArrayList<String>();
		for (Entry<String, String> entry : map.entrySet()) {
			list.add(String.format("%s=%s", entry.getKey(),entry.getValue()));
		}
		return StringUtils.join(list,"&");
	}
}
