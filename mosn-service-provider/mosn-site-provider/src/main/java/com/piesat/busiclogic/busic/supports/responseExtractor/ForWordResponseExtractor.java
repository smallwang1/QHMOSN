package com.piesat.busiclogic.busic.supports.responseExtractor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseExtractor;

public class ForWordResponseExtractor implements ResponseExtractor<String> {

	private static Logger logger = LoggerFactory.getLogger(ForWordResponseExtractor.class);
	
	@Override
	public String extractData(ClientHttpResponse response) throws IOException {
		
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
		return sb.toString();
	}
}
