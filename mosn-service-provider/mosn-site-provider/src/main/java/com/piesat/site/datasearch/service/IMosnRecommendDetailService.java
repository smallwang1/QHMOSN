package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.entity.MosnRecommendDetail;

import java.util.List;

public interface IMosnRecommendDetailService {

    List<MosnRecommendDetail> selectRecommendDetailAll();

    Integer insertRecommendDetail(MosnRecommendDetail recommendDetail);

    Integer updateRecommendDetail(MosnRecommendDetail recommendDetail);
}
