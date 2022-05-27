package com.piesat.busiclogic.app.dao;

import com.piesat.jdbc.JdbcPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class AppIndexDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    public List<Map<String, Object>> getAreaData() {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from APP_AREA_CODE order by sort ");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
    }
}
