package com.piesat.busiclogic.busic.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.piesat.busiclogic.busic.repositories.BaseAlgStaRepository;
import com.piesat.busiclogic.common.Util.BaseStaConfigReader;
import com.piesat.busiclogic.common.entity.AlgEntity;
import com.piesat.busiclogic.common.entity.ElementEntity;
import com.piesat.busiclogic.common.entity.TabEntity;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.jdbc.dataSource.DBContextHolder;

@RestController
@RequestMapping("/noAuth/baseAlgSta")
public class BaseAlgStaController {

	private static Logger logger = LoggerFactory.getLogger(BaseAlgStaController.class);

	private static List<TabEntity> list = new ArrayList<TabEntity>();
	
	@Resource
	private BaseAlgStaRepository baseAlgStaRepository;
	
	private static final String parsePatterns = "yyyyMMdd";
	
	private static final String sql_parsePattern = "yyyy-MM-dd";


	@RequestMapping("/getAllBaseAlgInfo")
	public  Wrapper<List<TabEntity>> getAllBaseAlgInfo() {
		try {
			if(list.isEmpty()) {
				scheduledTask();
			}
			return WrapMapper.ok(list);
		} catch (Exception e) {
			logger.error("获取算法信息失败!",e);
			return WrapMapper.error("获取算法信息失败");
		}
	}
	
	
	@RequestMapping("/getDataByBaseAlg")
	public  Wrapper<List<Map<String,String>>> getDataByBaseAlg(String tabId,String cols,String time,String station) {
		try {
			if(list.isEmpty()) {
				scheduledTask();
			}
			String tableName = getTableName(tabId);
			List<String> allCols = getAllCols(tabId, Arrays.asList(cols.split(",")));
			
			String checkAndFormatTime = checkAndFormatTime(time);
			if(StringUtils.isBlank(checkAndFormatTime)) {
				return WrapMapper.error("时间参数错误，统计失败!");
			}
			
			if(StringUtils.isBlank(tableName) || allCols == null || allCols.isEmpty()) {
				return WrapMapper.error("无法获取正确的统计参数，统计失败!");
			}
			List<Map<String,String>> allData = baseAlgStaRepository.getAllData(tableName, 
					StringUtils.join(allCols,","), checkAndFormatTime, StringUtils.isBlank(station)?null:Arrays.asList(station.split(",")));
			return WrapMapper.ok(allData);
		} catch (Exception e) {
			logger.error("获取统计结果失败!",e);
			return WrapMapper.error("获取统计结果失败!");
		}finally {
			DBContextHolder.setDataSourceName("dataSource");
		}
	}
	
	private static String checkAndFormatTime(String time) {
		try {
			Date parseDate = DateUtils.parseDate(time, parsePatterns);
			return DateFormatUtils.format(parseDate, sql_parsePattern);
		} catch (ParseException e) {
			return null;
		}
	}

	private static List<String> getAllCols(String tabId,List<String> cols) {
		
		for (int i = 0; i < list.size(); i++) {
			TabEntity tabEntity = list.get(i);
			if(StringUtils.equals(tabEntity.getId(), tabId)) {
				return getAllCols(tabEntity, cols);
			}
		}
		return null;
	}
	
	private static String getTableName(String tabId) {
		
		for (int i = 0; i < list.size(); i++) {
			TabEntity tabEntity = list.get(i);
			if(StringUtils.equals(tabEntity.getId(), tabId)) {
				return tabEntity.getTableName();
			}
		}
		return null;
	}
	
	
	private static List<String> getAllCols(TabEntity tabEntity,List<String> cols) {
		
		List<String> allCols = new ArrayList<String>();
		List<AlgEntity> algs = tabEntity.getAlgs();
		for (int i = 0; i < algs.size(); i++) {
			AlgEntity algEntity = algs.get(i);
			List<ElementEntity> elements = algEntity.getElements();
			for (int j = 0; j < elements.size(); j++) {
				ElementEntity elementEntity = elements.get(j);
				if(cols.contains(elementEntity.getId())) {
					allCols.add(elementEntity.getCols());
				}
			}
		}
		return allCols;
	}

	@Scheduled(cron = "0 */3 * * * ?")
	public void scheduledTask() {
		list = BaseStaConfigReader.getTabInfoByFilePath("config/baseAlgConfig.xml");
	}
}
