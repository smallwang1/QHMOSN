package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnRecommendDetailService;
import com.piesat.site.datasearch.service.entity.MosnRecommendDetail;
import com.piesat.site.datasearch.service.mapper.MosnRecommendDetailMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mosnRecommendDetailService")
public class MosnRecommendDetailServiceImpl implements IMosnRecommendDetailService {

    @Autowired
    private MosnRecommendDetailMapper mosnRecommendDetailMapper;

    @Override
    public List<MosnRecommendDetail> selectRecommendDetailAll() {
        return mosnRecommendDetailMapper.selectRecommendDetailAll();
    }

    @Override
    public Integer insertRecommendDetail(MosnRecommendDetail recommendDetail) {
        return mosnRecommendDetailMapper.insertRecommendDetail(recommendDetail);
    }

    @Override
    public Integer updateRecommendDetail(MosnRecommendDetail recommendDetail) {
        return mosnRecommendDetailMapper.updateRecommendDetail(recommendDetail);
    }
}
