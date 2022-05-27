package com.piesat.busiclogic.busic.dataset.dao.impl;

import com.piesat.busiclogic.busic.dataset.dao.DataSetDao;
import com.piesat.jdbc.JdbcPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class DataSetDaoImpl implements DataSetDao {

    @Autowired
    private JdbcPageHelper pageHelper;
    @Override
    public List<Map<String, Object>> getDamageData(String starttime, String endtime) {
        String querySql = "select * from AI_PRODATA_WIND where start_time >= :starttime and start_time < :endtime";
        Map<String,Object> map = new HashMap<>();
        map.put("starttime",starttime);
        map.put("endtime",endtime);
        return pageHelper.getJdbcTempalte().queryForList(querySql,map);
    }
}
