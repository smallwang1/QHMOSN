package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.RecommendDto;
import com.piesat.site.datasearch.service.entity.MosnRecommend;

import java.util.List;

public interface IMosnRecommendService {

    List<RecommendDto> selectRecommendAll();

    Integer updateRecommend(MosnRecommend recommend);

    Integer deleteRecommend(Long id);

    Integer insertRecommend(MosnRecommend recommend);

    Integer add(String recommendName, String detailId, String imageStr);

    Integer edit(Long id, String imageStr, String recommendName, String detailId);
}
