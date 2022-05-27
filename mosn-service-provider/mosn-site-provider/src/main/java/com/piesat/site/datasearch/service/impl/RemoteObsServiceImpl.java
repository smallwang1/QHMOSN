package com.piesat.site.datasearch.service.impl;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.piesat.site.datasearch.service.IMosnInterfaceService;
import com.piesat.site.datasearch.service.IRemoteObsService;
import com.piesat.site.datasearch.service.util.OkHttpUtils;
import com.piesat.site.datasearch.service.util.SignUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service("remoteObsService")
public class RemoteObsServiceImpl implements IRemoteObsService {

    @Value("${mosn.obsUrl}")
    private String url;
    @Value("${mosn.userId}")
    private String userId;
    @Value("${mosn.pwd}")
    private String pwd;

    @Autowired
    private Environment environment;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private IMosnInterfaceService mosnInterfaceService;

    @Override
    public String queryObsData(Map<String, Object> paramMap, Long interfaceId) {
        Map<String, Object> params = mosnInterfaceService.selectInfoById(interfaceId);
        params.remove("dataType");
        params.putAll(paramMap);
        return OkHttpUtils.get(url, integratParam(params));
    }

    @Override
    public String queryObsData(Map<String, Object> paramMap) {
        return OkHttpUtils.get(url, integratParam(paramMap));
    }

    @Override
    public String getInventoryForCmadaas(Map<String, Object> paramMap) {
        String url = environment.getProperty(String.valueOf(paramMap.get("id")));
        paramMap.remove("id");
        Map<String, Object> params = JSON.parseObject(JSON.toJSON(paramMap.get("data")).toString(), Map.class);
        return OkHttpUtils.get(url, integratParam(params));
    }

    /**
     * 接口每次调用均需从新拼接
     * @param paramMap
     * @return
     */
    private Map<String, Object> integratParam(Map<String, Object> paramMap) {
        paramMap.put("userId", userId);
        paramMap.put("pwd", pwd);
        paramMap.put("nonce", UUID.randomUUID().toString());
        paramMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        paramMap.put("sign", SignUtils.getSign(paramMap));
        paramMap.remove("pwd");
        return paramMap;
    }
}
