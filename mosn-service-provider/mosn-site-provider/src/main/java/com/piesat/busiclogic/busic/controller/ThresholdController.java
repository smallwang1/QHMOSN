package com.piesat.busiclogic.busic.controller;

import com.piesat.busiclogic.busic.entities.SceceProInfoEntrity;
import com.piesat.busiclogic.busic.services.ThresholdService;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("/noAuth/threshold")
public class ThresholdController {

	@Resource
	private ThresholdService thresholdService;
	
	private Map<String, SceceProInfoEntrity> sceceMap = new HashMap<String, SceceProInfoEntrity>();
	
	private static final String STAND_PATTERND = "yyyy-MM-dd HH:mm:ss";
	
	private static final String PARAMS_PATTERN = "yyyyMMddHHmmss";
	
	private static final String alias = "value";
	
	
    @SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据时间和产品ID查询实况阈值", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "beginTime", value = "开始时间", required = true),
        @ApiImplicitParam(name = "endTime", value = "结束时间", required = true),
        @ApiImplicitParam(name = "proId", value = "产品ID", required = true)})
    @ApiResponse(code = 200, message = "标准返回结果。")
    @RequestMapping("/findThresholdByTimeAndProId")
    public Wrapper findThresholdByTimeAndProId(@RequestParam(value = "beginTime",required = true) String beginTime,
    								@RequestParam(value = "endTime",required = true) String endTime,
                                   @RequestParam(value = "proId",required = true) String proId) {
    	
    	if(StringUtils.isBlank(proId) || !sceceMap.containsKey(proId)) {
    		return WrapMapper.error("未知的产品ID，请重新选择！");
    	}
    	
    	SceceProInfoEntrity sceceProInfoEntrity = sceceMap.get(proId);
    	
    	if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)) {
    		return WrapMapper.error("时间参数不能为空，请重新选择！");
    	}
    	
    	Date beginDate = null;
    	Date endDate = null;
    	try {
			 beginDate = DateUtils.parseDate(beginTime, PARAMS_PATTERN);
			 endDate = DateUtils.parseDate(endTime, PARAMS_PATTERN);
		} catch (Exception e) {
			return WrapMapper.error("时间参数格式错误，请重新选择！");
		}
    	
    	String timeCondtion = String.format("%s between '%s' and '%s' ", DateFormatUtils.format(beginDate, STAND_PATTERND),
    			DateFormatUtils.format(endDate, STAND_PATTERND));
    	
    	String minAndMaxCondtion = "" + sceceProInfoEntrity.getMinValue() == null?"":String.format(" %s > %s ", sceceProInfoEntrity.getProColName(),sceceProInfoEntrity.getMinValue())+
    			sceceProInfoEntrity.getMaxValue() == null?"":String.format(" %s > %s  ", sceceProInfoEntrity.getProColName(),sceceProInfoEntrity.getMaxValue());
    	
    	List<BigDecimal> excludeValue = sceceProInfoEntrity.getExcludeValue();
    	String notInCondition = "" + excludeValue == null || excludeValue.isEmpty() ? "":" ("+StringUtils.join(excludeValue,",")+") ";
    	
    	String groupBy = "GROUP BY V01301";
    	
    	List<String> condtions = new ArrayList<String>();
    	addNotNullValue(condtions, timeCondtion);
    	addNotNullValue(condtions, minAndMaxCondtion);
    	addNotNullValue(condtions, notInCondition);
    	
    	try {
    		List<Map<String, String>> list = thresholdService.findAllSceceDataByCondition(sceceProInfoEntrity.getProTableName(), sceceProInfoEntrity.getDataSourceName(),
    				getAggCondtion(sceceProInfoEntrity.getAggType(), sceceProInfoEntrity.getProColName()), StringUtils.join(condtions," AND ")+groupBy);
    		return WrapMapper.ok(list);
		} catch (Exception e) {
			 return WrapMapper.error("查询数据异常，请重试！");
		}
    }
	
	private static void addNotNullValue(List<String> list,String value) {
		
		if(!StringUtils.isBlank(value)) {
			list.add(value);
		}
	}
	
	private static String getAggCondtion(String agg,String colName) {
		
		if(StringUtils.isBlank(agg)) {
			return colName;
		}
		
		return String.format("%s(%s) as %s", agg,colName,alias);
	}
}
