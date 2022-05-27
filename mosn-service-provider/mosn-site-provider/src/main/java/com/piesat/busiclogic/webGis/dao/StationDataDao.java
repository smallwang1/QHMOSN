package com.piesat.busiclogic.webGis.dao;

import com.piesat.jdbc.JdbcPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class StationDataDao {
    @Autowired
    private JdbcPageHelper pageHelper;

    public List<Map<String, Object>> getTypoonData() {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from TYPHOON_NAME_INFO ");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }
}
