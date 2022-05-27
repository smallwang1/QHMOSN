package com.piesat.busiclogic.busic.recommend.service;

import com.piesat.busiclogic.busic.recommend.entity.Recommend;

import java.util.List;
import java.util.Map;

public interface RecommendService {
    void addRecommend(Recommend recommend);

    void deleteRecommend(Recommend recommend);

    void editRecommend(Recommend recommend);

    List<Map<String,Object>> getRecommendList(Recommend recommend);
}
