package com.piesat.busiclogic.busic.supports.responseExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

import com.piesat.common.util.JsonUtils;

public class CmadaasResponseExtractor implements ResponseExtractor<Map<String, String>> {

	private static Logger logger = LoggerFactory.getLogger(CmadaasResponseExtractor.class);
	
	@Override
	public Map<String, String> extractData(ClientHttpResponse response) throws IOException {
		
		InputStream inputStream = null;
		StringBuilder sb = new StringBuilder();   
		try {
			inputStream = response.getBody();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));   
		     String line = null;   
		     while ((line = reader.readLine()) != null) { 
		    	 sb.append(line);   
		     }
		} catch (Exception e) {
			logger.error("read return data error. fail message is : {}",e.getMessage(),e);
		}finally {
			if(inputStream != null ) {
				inputStream.close();
			}
		}
		
		String json = sb.toString();
		if(StringUtils.isBlank(json)) {
			return null;
		}
		HashMap<String,String> hashMap = JsonUtils.resolveJSON(json);
		
		return null;
	}
}
