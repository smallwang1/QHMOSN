package com.piesat.busiclogic.busic.menuset.service;

import com.piesat.common.core.dto.LoginAuthDto;

import java.util.List;
import java.util.Map;

/**
 * @author sjc
 * @date 2020/7/30
 */
public interface MenuSetService {

    public List<Map<String,Object>> getMenuSetData(String code, LoginAuthDto loginAuthDto) throws Exception;

    public List<Map<String,Object>> getMenuSetProData(String code, LoginAuthDto loginAuthDto) throws Exception;

    List<Map<String, Object>> getMenuSetDataByTree(String code, LoginAuthDto loginAuthDto) throws Exception;

    List<Map<String, Object>> getCodeData(String code);

    List<Map<String, Object>> getMenuDataNoRule();
}
