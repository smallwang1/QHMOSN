package com.piesat.busiclogic.busic.supports.responseExtractor;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeBase;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.piesat.busiclogic.busic.supports.ExtractorConfig;
import com.piesat.busiclogic.busic.supports.ValueConfig;
import com.piesat.common.util.PublicUtil;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CimissResponseExtractor implements ResponseExtractor<Map<String, Object>> {

	
	private ExtractorConfig config;
	
	public CimissResponseExtractor(ExtractorConfig config) {
		
		this.config = config;
		
	}
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public Map<String, Object> extractData(ClientHttpResponse response) throws IOException {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		InputStream inputStream = null;
		try {
			inputStream = response.getBody();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));   
			 StringBuilder sb = new StringBuilder();   
		     String line = null;   
		     while ((line = reader.readLine()) != null) { 
		    	 sb.append(line + "\n");   
		     }
			Map<String, String> json = resolveJSON(sb.toString());
			if(config != null&&config.getValueConfigs() != null && !config.getValueConfigs().isEmpty()) {
				List<ValueConfig> valueConfigs = config.getValueConfigs();
				for (int i = 0; i < valueConfigs.size(); i++) {
					ValueConfig valueConfig = valueConfigs.get(i);
					String key = valueConfig.getKey();
					String jsonValue = json.get(key);
					if(PublicUtil.isEmpty(jsonValue)){
						continue;
					}
					resultMap.put(key, jsonToObject(jsonValue,valueConfig));
				}
				if(resultMap.isEmpty()){
					resultMap.putAll(json);
				}
			}else {
				resultMap.putAll(json);
			}
		} catch (Exception e) {
			resultMap.put("code", "-1001");
		}finally {
			if(inputStream != null ) {
				inputStream.close();
			}
		}
		return resultMap;
	}
	
	private  Object jsonToObject(String json,ValueConfig valueConfig) throws JsonParseException, JsonMappingException, IOException {
		
		TypeFactory typeFactory = objectMapper.getTypeFactory();
		TypeBase type = null;
		switch (valueConfig.getType()) {
		case collectionLike:
			type = typeFactory.constructCollectionLikeType(valueConfig.getCollectionClass(), valueConfig.getElementClass());
			break;
		case mapLike:
			type = typeFactory.constructMapLikeType(valueConfig.getMapClass(), valueConfig.getKeyClass(), valueConfig.getValueClass());
		default:
			return json;
		}
		return objectMapper.readValue(json, type);
	}
	
    private  Map<String, String> resolveJSON(String json) {
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
