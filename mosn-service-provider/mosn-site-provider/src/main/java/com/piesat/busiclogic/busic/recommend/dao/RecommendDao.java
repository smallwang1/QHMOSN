package com.piesat.busiclogic.busic.recommend.dao;

import com.piesat.busiclogic.busic.recommend.entity.Recommend;

import java.util.List;
import java.util.Map;

public interface RecommendDao {

    int addRecommend(Recommend recommend);

    int deleteRecommend(Recommend recommend);

    int editRecommend(Recommend recommend);

    List<Map<String, Object>> getRecommendList(Recommend recommend);
}
