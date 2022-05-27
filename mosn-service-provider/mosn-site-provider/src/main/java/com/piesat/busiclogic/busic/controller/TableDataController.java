package com.piesat.busiclogic.busic.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;
import com.piesat.busiclogic.busic.productMgr.service.ProductUnitService;
import com.piesat.busiclogic.busic.supports.ExtractorConfig;
import com.piesat.busiclogic.busic.supports.responseExtractor.CimissResponseExtractor;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.SignGenUtil;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.*;
import java.util.Map.Entry;

@RestController
@RequestMapping("/noAuth/product")
public class TableDataController {

	@Autowired
    private ProductUnitService productUnitService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据时间和产品ID查询实况阈值", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "id", value = "单元ID", required = true),
        @ApiImplicitParam(name = "url", value = "请求接口的URL", required = true),
        @ApiImplicitParam(name = "httpMethod", value = "请求类型", required = true),
        @ApiImplicitParam(name = "headParams", value = "请求头参数", required = false),
        @ApiImplicitParam(name = "body", value = "body参数", required = false)})
    @ApiResponse(code = 200, message = "标准返回结果。")
    @RequestMapping("/findThresholdByTimeAndProId")
	public Wrapper findInterfaceDataByCondition(String id, String url, String httpMethod,
												String headParams, String body, HttpServletRequest request)   {
		try {
			HttpMethod requestType = HttpMethod.resolve(httpMethod);
				RestTemplate restTemplate = new RestTemplate();
			ExtractorConfig extractorConfig = new ExtractorConfig();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
			objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			ProductUnit pro = new ProductUnit();
			pro.setId(id);
			ProductUnit productUnitById = productUnitService.getProductUnitById(pro);
			String analysis_data = productUnitById.getAnalysis_data();
			if(!StringUtils.isBlank(analysis_data)) {
				extractorConfig = objectMapper.readValue(analysis_data, ExtractorConfig.class);
			}
		    MultiValueMap<String,String> multiValueMap = new HttpHeaders();
			if(!StringUtils.isBlank(headParams)) {
				Map<String,String> map = objectMapper.readValue(headParams, HashMap.class);
				if(map != null) {
					for (Entry<String,String> entry:
							map.entrySet()) {
						multiValueMap.add(entry.getKey(),entry.getValue());
					}
				}
			}
			// 遍历请求头
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				multiValueMap.add(key, value);
			}
			multiValueMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			if(requestType != HttpMethod.POST) {
				HashMap<String,String> map = objectMapper.readValue(body, HashMap.class);
				String noce = UUID.randomUUID().toString();
				String timestamp = String.valueOf(System.currentTimeMillis());
				//天擎接口添加参数
				if(url.indexOf("music-ws")!=-1 || url.indexOf("tqApiQuery")!=-1 ){
					map.put("nonce", noce);
					map.put("timestamp", timestamp);
					map.put("sign", SignGenUtil.getSign(map));
				}
				url = url + (StringUtils.endsWith(url, "?") ? "":"?") + getParams(map);
			}
			multiValueMap.remove("accept-encoding");
			String bodyParams = body != null?body:"";
			HttpEntity<String> httpEntity = new HttpEntity<String>(bodyParams,multiValueMap);
			RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity);
			ResponseExtractor<Map<String, Object>> responseExtractor = new CimissResponseExtractor(extractorConfig);
			UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
			URI uri = builder.build().encode().toUri();
			Map<String, Object> execute = restTemplate.execute(uri,  requestType, requestCallback, responseExtractor);
			if(execute.containsKey("result")){
				if("a13aaf1f-739a-4be3-bb7d-39d6643314da".equals(id)){ // 红外筛选，02 08 14 20
					List<Map> dList = JSONObject.parseArray(String.valueOf(execute.get("result")),Map.class);
					dList = this.handleData(dList);
					execute.put("result", dList);
				}else if("ce0d752b-eb48-4f00-942e-0209af1f1fb4".equals(id)){
					List<Map> dList = JSONObject.parseArray(String.valueOf(execute.get("result")),Map.class);
					dList = this.excludeData(dList);
					execute.put("result", dList);
				}else{
					execute.put("result", JSONObject.parseArray(String.valueOf(execute.get("result")),Map.class));
				}
			}
			return WrapMapper.ok(execute);
		} catch (Exception e) {
			e.printStackTrace();
			return WrapMapper.error();
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@ApiOperation(value = "根据时间和产品ID查询实况阈值", notes = "")
	@ApiImplicitParams({@ApiImplicitParam(name = "id", value = "单元ID", required = true),
			@ApiImplicitParam(name = "url", value = "请求接口的URL", required = true),
			@ApiImplicitParam(name = "httpMethod", value = "请求类型", required = true),
			@ApiImplicitParam(name = "headParams", value = "请求头参数", required = false),
			@ApiImplicitParam(name = "body", value = "body参数", required = false)})
	@ApiResponse(code = 200, message = "标准返回结果。")
	@RequestMapping("/findThresholdforIndex")
	public Wrapper findThresholdforIndex(String id, String url, String httpMethod,
												String headParams, String body, HttpServletRequest request)   {
		try {
			HttpMethod requestType = HttpMethod.resolve(httpMethod);
			RestTemplate restTemplate = new RestTemplate();
			ExtractorConfig extractorConfig = new ExtractorConfig();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
			objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			ProductUnit pro = new ProductUnit();
			pro.setId(id);
			ProductUnit productUnitById = productUnitService.getProductUnitById(pro);
			String analysis_data = productUnitById.getAnalysis_data();
			if(!StringUtils.isBlank(analysis_data)) {
				extractorConfig = objectMapper.readValue(analysis_data, ExtractorConfig.class);
			}
			MultiValueMap<String,String> multiValueMap = new HttpHeaders();
			if(!StringUtils.isBlank(headParams)) {
				Map<String,String> map = objectMapper.readValue(headParams, HashMap.class);
				if(map != null) {
					for (Entry<String,String> entry:
							map.entrySet()) {
						multiValueMap.add(entry.getKey(),entry.getValue());
					}
				}
			}
			// 遍历请求头
			Enumeration headerNames = request.getHeaderNames();
			while (headerNames.hasMoreElements()) {
				String key = (String) headerNames.nextElement();
				String value = request.getHeader(key);
				multiValueMap.add(key, value);
			}
			multiValueMap.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
			if(requestType != HttpMethod.POST) {
				HashMap<String,String> map = objectMapper.readValue(body, HashMap.class);
				String noce = UUID.randomUUID().toString();
				String timestamp = String.valueOf(System.currentTimeMillis());
				//天擎接口添加参数
				if(url.indexOf("music-ws")!=-1 || url.indexOf("tqApiQuery")!=-1 ){
					map.put("nonce", noce);
					map.put("timestamp", timestamp);
					map.put("sign", SignGenUtil.getSign(map));
				}
				url = url + (StringUtils.endsWith(url, "?") ? "":"?") + getParams(map);
			}
			multiValueMap.remove("accept-encoding");
			String bodyParams = body != null?body:"";
			HttpEntity<String> httpEntity = new HttpEntity<String>(bodyParams,multiValueMap);
			RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity);
			ResponseExtractor<Map<String, Object>> responseExtractor = new CimissResponseExtractor(extractorConfig);
			Map<String, Object> execute = restTemplate.execute(new URI(url),  requestType, requestCallback, responseExtractor);
			if(execute.containsKey("result")){
				execute.put("result", JSONObject.parseArray(String.valueOf(execute.get("result")),Map.class));
			}
			return WrapMapper.ok(execute);
		} catch (Exception e) {
			e.printStackTrace();
			return WrapMapper.error();
		}
	}


	private List<Map> excludeData(List<Map> dList) {
		List<Map> list = new ArrayList<>();
		for (int i = 0; i < dList.size(); i++) {
			Map<String,String> map = dList.get(i);
			String content = String.valueOf(map.get("ProductContent"));
			if(content.indexOf("重大气象")!=-1){
				list.add(map);
			}
		}
		return list;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据时间和产品ID查询实况阈值", notes = "")
    @ApiImplicitParams({@ApiImplicitParam(name = "signParams", value = "sign参数", required = true)})
    @ApiResponse(code = 200, message = "标准返回结果。")
    @RequestMapping("/getSign")
	public Wrapper getSign(HashMap<String, String> signParams)   {

		String sign = SignGenUtil.getSign(signParams);
		return WrapMapper.ok(sign);
	}

	private static String getParams(Map< String, String> map) {

		List<String> list = new ArrayList<String>();
		for (Entry<String, String> entry : map.entrySet()) {
			list.add(String.format("%s=%s", entry.getKey(),entry.getValue()));
		}
		return StringUtils.join(list,"&");
	}

	public List<Map> handleData(List<Map> list){
		Map<String,Integer> indexMap = new HashMap<>();
		List<Map> returunList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			Map<String,String> map = list.get(i);
			String Day = String.valueOf(map.get("Day"));
			String Hour = String.valueOf(map.get("Hour"));
			if(Hour.equals("18") || Hour.equals("0") || Hour.equals("6") || Hour.equals("12")){
				String index = Day + Hour;
				if(!indexMap.containsKey(index)){
					indexMap.put(Day+Hour,i);
					returunList.add(map);
				}
			}
		}
		return returunList;
	}
}
