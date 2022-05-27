package com.piesat.busiclogic.busic.controller;

import com.piesat.busiclogic.busic.services.ObsService;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.PublicUtil;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/noAuth/obs")
public class ObsController {


	@Resource
	private ObsService obsService;

	@RequestMapping("/getObsData")
	public  Wrapper<Map<String, Object>> getObsData(String interFaceParams,String range,String elementKey,String stationKey,String replaceKey)   {

		try {
			Map<String, Object> map = obsService.getAllObsDataByCondition(interFaceParams, range, elementKey, stationKey, replaceKey);
			if(!PublicUtil.isEmpty(map)){
				return WrapMapper.ok(map);
			} else {
				return WrapMapper.ok(new HashMap<>());
			}
		} catch (Exception e) {
			return WrapMapper.error("查询数据失败"+e.getMessage());
		}
	}
}
