package com.piesat.busiclogic.busic.controller;

import com.piesat.busiclogic.busic.services.*;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.jdbc.holder.SpringContextHolder;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.Map.Entry;

@RestController
@RequestMapping("/noAuth/importWeather")
public class ImportWeatherController {

	private static Logger logger = LoggerFactory.getLogger(ImportWeatherController.class);

	private static Map<String, ImpWeaService> serviceMap = new HashMap<String, ImpWeaService>();

	private static final String parsePatterns = "yyyyMMddHHmmss";

	static {
		ApplicationContext applicationContext = SpringContextHolder.getApplicationContext();
		serviceMap.put("cooling", applicationContext.getBean(ChangeTemImpWeaService.class));
		serviceMap.put("strongprec", applicationContext.getBean(Lass12HourPreImpWeaService.class));
		serviceMap.put("strongprec", applicationContext.getBean(Lass3HourPreImpWeaService.class));
		serviceMap.put("strongprec", applicationContext.getBean(Lass6HourPreImpWeaService.class));
		serviceMap.put("bigwind", applicationContext.getBean(MaxWindImpWeaService.class));
		serviceMap.put("hightemp", applicationContext.getBean(TemImpWeaService.class));
		serviceMap.put("vis", applicationContext.getBean(VisiImpWeaService.class));
		serviceMap.put("thunder", applicationContext.getBean(ThunderImpWeaService.class));
		serviceMap.put("hail", applicationContext.getBean(HailImpWeaService.class));
	}

	@RequestMapping("/importWeatherByType")
	public  Wrapper<Map<String, List<Map<String, Object>>>> getObsData(String[] types)   {
		try {
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String,Object>>>();
			if(types == null || types.length == 0) {
				Set<String> keySet = serviceMap.keySet();
				types = keySet.toArray(new String[keySet.size()]);
			}
			for (int i = 0; i < types.length; i++) {
				ImpWeaService impWeaService = serviceMap.get(types[i]);
				if(impWeaService == null) {
					return WrapMapper.error(String.format("未知重要天气类型: {}",types[i]));
				}
				Map<String, Map<String, Object>> data = impWeaService.getData();
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				list.addAll(data.values());
				if (map.containsKey(types[i])) {
					list.addAll(map.get(types[i]));
				}
				map.put(types[i], list);
			}
			return WrapMapper.ok(map);
		} catch (Exception e) {
			logger.error("重要天气识别失败!",e);
			return WrapMapper.error("重要天气识别失败");
		}
	}


	@RequestMapping("/importWeatherByTypeAndTime")
	public  Wrapper<Map<String, List<Map<String, Object>>>> getObsDataByTime(String[] types,String times)   {
		try {
			Map<String, List<Map<String, Object>>> map = new HashMap<String, List<Map<String,Object>>>();
			Date date = DateUtils.parseDate(times, parsePatterns);
			if(types == null || types.length == 0) {
				Set<String> keySet = serviceMap.keySet();
				types = keySet.toArray(new String[keySet.size()]);
			}
			for (int i = 0; i < types.length; i++) {
				ImpWeaService impWeaService = serviceMap.get(types[i]);
				if(impWeaService == null) {
					return WrapMapper.error(String.format("未知重要天气类型: {}",types[i]));
				}
				Map<String, Map<String, Object>> data = impWeaService.selectHourSurfImportData(date);
				List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
				list.addAll(data.values());
				if (map.containsKey(types[i])) {
					list.addAll(map.get(types[i]));
				}
				map.put(types[i], list);
			}
			return WrapMapper.ok(map);
		} catch (Exception e) {
			logger.error("重要天气识别失败!",e);
			return WrapMapper.error("重要天气识别失败");
		}
	}


//	@Scheduled(cron = "0 */3 * * * ?")
	public void scheduledTask() {
		for (Entry<String, ImpWeaService> entry : serviceMap.entrySet()) {
			ImpWeaService impWeaService = entry.getValue();
			try {
				impWeaService.selectHourSurfImportData(null);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}
}
