package com.piesat.busiclogic.busic.recommend.dao.impl;

import com.piesat.busiclogic.busic.recommend.dao.RecommendDao;
import com.piesat.busiclogic.busic.recommend.entity.Recommend;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class RecommendDaoImpl implements RecommendDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public int addRecommend(Recommend recommend) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  MGR_RECOMMEND");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("NAME,\n");
        sb.append("URL,\n");
        sb.append("IMGDATA,\n");
        sb.append("SORT,\n");
        sb.append("STATUS,\n");
        sb.append("CREATE_TIME \n");
        sb.append(" ) values (");

        sb.append(":ID,\n");
        sb.append(":NAME,\n");
        sb.append(":URL,\n");
        sb.append(":IMGDATA,\n");
        sb.append(":SORT,\n");
        sb.append(":STATUS,\n");
        sb.append(":CREATE_TIME) \n");


        Map<String,Object> map = new HashMap<>();
        map.put("ID", UUID.randomUUID().toString());
        map.put("NAME", recommend.getName());
        map.put("URL", recommend.getUrl());
        map.put("IMGDATA",recommend.getImgData());
        map.put("SORT", recommend.getSort());
        map.put("STATUS", recommend.getStatus());
        map.put("CREATE_TIME",recommend.getCreate_time());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int deleteRecommend(Recommend recommend) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("delete from MGR_RECOMMEND where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", recommend.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int editRecommend(Recommend recommend) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update MGR_RECOMMEND set");
        if(!PublicUtil.isEmpty(recommend.getName())){
            sb.append(" NAME=:NAME ,");
        }
        if(!PublicUtil.isEmpty(recommend.getUrl())){
            sb.append(" URL=:URL,");
        }
        if(!PublicUtil.isEmpty(recommend.getImgData())){
            sb.append(" IMGDATA=:IMGDATA, ");
        }
        if(!PublicUtil.isEmpty(recommend.getSort())){
            sb.append(" SORT=:SORT, ");
        }
        sb.append(" STATUS=:STATUS");
        sb.append(" WHERE ID =:ID ");

        Map<String,Object> map = new HashMap<>();
        map.put("NAME", recommend.getName());
        map.put("URL", recommend.getUrl());
        map.put("IMGDATA", recommend.getImgData());
        map.put("SORT", recommend.getSort());
        map.put("STATUS", recommend.getStatus());
        map.put("ID", recommend.getId());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public List<Map<String, Object>> getRecommendList(Recommend recommend) {
        return null;
    }
}
