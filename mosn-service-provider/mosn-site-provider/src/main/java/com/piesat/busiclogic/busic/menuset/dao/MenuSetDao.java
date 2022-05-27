package com.piesat.busiclogic.busic.menuset.dao;

import java.util.List;
import java.util.Map;

/**
 * 获取指定code的select选项
 */
public interface MenuSetDao {

    List<Map<String,Object>> getMenuSetByCode(String code, String roleIds);

    List<Map<String,Object>> getMenuSetProByCode(String code, String roleIds);

    Map<String,Object> getRootIdByCode(String code);

    List<String> getRoleList(String userid);

    List<Map<String, Object>> getCodeData(String code);

    List<Map<String, Object>> getMenuSetNoRule();
}
