package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnRecommend;
import com.piesat.site.datasearch.service.vo.RecommendVo;

import java.util.List;

public interface MosnRecommendMapper {

    List<RecommendVo> selectRecommendAll();

    MosnRecommend selectRecommendResourceById(Long id);

    Integer insertRecommend(MosnRecommend recommend);

    Integer updateRecommend(MosnRecommend recommend);

    Integer deleteRecommend(Long id);
}
