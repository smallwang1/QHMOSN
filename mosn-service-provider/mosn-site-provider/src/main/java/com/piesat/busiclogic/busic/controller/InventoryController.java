package com.piesat.busiclogic.busic.controller;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.busiclogic.busic.supports.responseExtractor.ForWordResponseExtractor;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.CallInterfaceUtils;
import com.piesat.common.util.SignGenUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;

@RestController
@RequestMapping("/noAuth/list")
@CrossOrigin("*")
public class InventoryController {

	
	private static Map<String, String> urlMap = new HashMap<String, String>();
	
	@SuppressWarnings("rawtypes")
	private static Map<String, Class> classTypeMap = new HashMap<String, Class>();
	
	static {
		urlMap.put("dfca5923-021b-4e6c-980c-b81410ae1c6f", "http://10.129.64.46:8088/cmadaas/dataList/getTreeInfo.action");
		urlMap.put("48dcf9c6-5277-49a7-8287-d334539f6d59", "http://10.129.90.120:28008/cmadaas/music/api");
		urlMap.put("ea162e26-26fa-414b-931e-12587446549c", "http://10.129.90.120:28028/cmadaas/sod/dm/dataClass/getDataClassCoreInfo");
		urlMap.put("419882e0-69a9-4733-8997-c71404bc7f06", "http://10.129.90.120:28028/cmadaas/sod/dm/datastatistics/getOnlineTime");
		urlMap.put("f4f404cb-5cae-414d-801d-d7c08451b580", "http://10.129.64.46:8088/cmadaas/api/rest/resourceShare/getCtsDpc");
		urlMap.put("bb29afdb-4291-432d-bb30-41ec80ef9368", "http://10.129.90.120:28008/cmadaas/music/api");
		urlMap.put("06bd51e1-5608-432f-bbe7-2a9f4d3c27d4", "http://10.129.90.120:28028/cmadaas/sod/dm/dataTable/getByClassId");
		urlMap.put("97a34293-d879-4266-ada2-3b282a5c5e88", "http://10.129.90.120:28028/cmadaas/sod/dm/dataTable/getMultiDataInfoByClassId");
		
		classTypeMap.put("dfca5923-021b-4e6c-980c-b81410ae1c6f", ArrayList.class);
		classTypeMap.put("48dcf9c6-5277-49a7-8287-d334539f6d59", HashMap.class);
		classTypeMap.put("ea162e26-26fa-414b-931e-12587446549c", HashMap.class);
		classTypeMap.put("419882e0-69a9-4733-8997-c71404bc7f06", HashMap.class);
		classTypeMap.put("f4f404cb-5cae-414d-801d-d7c08451b580", HashMap.class);
		classTypeMap.put("bb29afdb-4291-432d-bb30-41ec80ef9368", HashMap.class);
		classTypeMap.put("06bd51e1-5608-432f-bbe7-2a9f4d3c27d4", HashMap.class);
		classTypeMap.put("97a34293-d879-4266-ada2-3b282a5c5e88", HashMap.class);
		
	}
	@SuppressWarnings({"unchecked", "rawtypes" })
	@ApiOperation(value = "根据时间和产品ID查询实况阈值", notes = "")
    @ApiImplicitParams({
    	@ApiImplicitParam(name = "id", value = "资料id", required = true),
    	@ApiImplicitParam(name = "params", value = "参数", required = true)})
    @ApiResponse(code = 200, message = "标准返回结果。")
    @RequestMapping("/getInventoryForCmadaas")
	public  Wrapper getInventoryForCmadaas(String id,String params)   {
		
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			RestTemplate restTemplate = new RestTemplate();
			
			MultiValueMap<String,String> multiValueMap = new HttpHeaders();
			multiValueMap.add("Content-Type", "application/json; charset=UTF-8");
			multiValueMap.add("Accept", "*/*");
			multiValueMap.add("Accept", MediaType.APPLICATION_JSON.toString());
			String url  = urlMap.get(id);
			HashMap<String,String> paramsMap = resolveJSON(objectMapper,params);
			paramsMap.put("nonce", UUID.randomUUID().toString());
			paramsMap.put("timestamp", System.currentTimeMillis()+"");
			paramsMap.put("userId", "USR_DuoYuanSys");
			paramsMap.put("pwd", "DuoYuan123");
			paramsMap.put("sign", SignGenUtil.getSign(paramsMap));
			paramsMap.remove("pwd");
			encodeParams(paramsMap);
			url = url + (StringUtils.endsWith(url, "?") ? "":"?") + CallInterfaceUtils.getParams(paramsMap);
			HttpEntity<String> httpEntity = new HttpEntity<String>("",multiValueMap);
			RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity);
			ForWordResponseExtractor forWordResponseExtractor = new ForWordResponseExtractor();
			String data = restTemplate.execute(new URI(url),  HttpMethod.GET, requestCallback, forWordResponseExtractor);
			return WrapMapper.ok(objectMapper.readValue(data, classTypeMap.get(id)));
		} catch (Exception e) {
			e.printStackTrace();
			return WrapMapper.error();
		}
	}
	
	private static void encodeParams(HashMap<String, String> paramsMap) {
		
		for (Entry<String, String> entry : paramsMap.entrySet()) {
			paramsMap.put(entry.getKey(), URLEncoder.encode(entry.getValue()));
		}
	}
	

	
	private static HashMap<String, String> resolveJSON(ObjectMapper objectMapper,String json) {
        HashMap<String, String> result = new HashMap<>();
        try {
            JsonNode root = objectMapper.readTree(json);
            for (Iterator<Map.Entry<String, JsonNode>> iterator = root.fields(); iterator.hasNext(); ) {
                Map.Entry<String, JsonNode> nodeEntry = iterator.next();
                JsonNode node = nodeEntry.getValue();
                if (node.isTextual()) {
                    result.put(nodeEntry.getKey(), node.textValue());
                } else {
                    result.put(nodeEntry.getKey(), node.toString());
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return result;
    }
}
