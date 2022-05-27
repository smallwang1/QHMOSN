package com.piesat.busiclogic.common.Util;

import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Map;

@Repository
public class HttpUtil {

    private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);

    private RestTemplate restTemplate;
    private HttpHeaders httpHeaders;

    @PostConstruct
    public void init() {
        restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
    }

    /**
     * post - json
     * @param map
     * @return
     */
    public String post(Map<String,String> map, String url) {
        JSONObject params = JSONObject.fromObject(map);
        logger.info("请求参数：{}", params.toString());
        ResponseEntity<String> response;
        try {
            response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(params, httpHeaders),
                    String.class);
        } catch (Exception e) {
            logger.error("请求异常", e);
            return "请求异常";
        }
        logger.info("响应结果：{}", JSONObject.fromObject(response).toString());

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            logger.info("请求成功，返回结果：{}", response.getBody());
            return response.getBody();
        }

        logger.error("请求失败，Http状态码：{}，返回结果：{}", response.getStatusCode(), response.getBody());
        return response.getBody();
    }

    public  String get( Map<String,String> map,String url){
        logger.info("请求参数：{}", map.toString());
        ResponseEntity<String> response;
        try{
            url = URLDecoder.decode(url +"?" + Misc.createLinkStringByGet(map),"utf-8");
        response = restTemplate.getForEntity(url,String.class);
        } catch (Exception e){
            logger.error("请求异常", e);
            return "请求异常";
        }
        logger.info("响应结果：{}", JSONObject.fromObject(response).toString());

        if (response.getStatusCode().equals(HttpStatus.OK)) {
            logger.info("请求成功，返回结果：{}", response.getBody());
            return response.getBody();
        }
        logger.error("请求失败，Http状态码：{}，返回结果：{}", response.getStatusCode(), response.getBody());
        return response.getBody();
    }
}