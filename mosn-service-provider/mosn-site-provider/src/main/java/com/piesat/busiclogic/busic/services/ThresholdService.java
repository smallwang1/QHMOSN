package com.piesat.busiclogic.busic.services;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.piesat.busiclogic.busic.repositories.ThresholdRepository;

@Service
public class ThresholdService {

	@Resource
	private ThresholdRepository thresholdRepository;
	
	
	public List<Map<String, String>> findAllSceceDataByCondition(String tableName,String dataSourceName,String cols,String condtion) {
		return thresholdRepository.findAllSceceDataByCondition(tableName, dataSourceName, cols, condtion);
	}
}
