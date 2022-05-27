package com.piesat.site.datasearch.service;

import java.util.Map;

public interface IRemoteObsService {

    /**
     * 调用天擎接口获取服务数据
     * @param paramMap
     * @return
     */
    String queryObsData(Map<String, Object> paramMap, Long interfaceId);

    /**
     * 调用天擎接口获取服务数据
     * @param paramMap
     * @return
     */
    String queryObsData(Map<String, Object> paramMap);

    /**
     * 调用天擎接口获取服务数据
     * @param paramMap
     * @return
     */
    String getInventoryForCmadaas(Map<String, Object> paramMap);
}
