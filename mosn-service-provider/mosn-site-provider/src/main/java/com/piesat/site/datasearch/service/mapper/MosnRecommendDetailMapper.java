package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnRecommendDetail;

import java.util.List;

public interface MosnRecommendDetailMapper {

    List<MosnRecommendDetail> selectRecommendDetailAll();

    Integer insertRecommendDetail(MosnRecommendDetail recommendDetail);

    Integer updateRecommendDetail(MosnRecommendDetail recommendDetail);
}
