package com.piesat.busiclogic.busic.dataset.dao;

import java.util.List;
import java.util.Map;

public interface DataSetDao {

    List<Map<String, Object>> getDamageData(String starttime, String endtime);
}
