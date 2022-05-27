package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnRecommendService;
import com.piesat.site.datasearch.service.dto.RecommendDetailDto;
import com.piesat.site.datasearch.service.dto.RecommendDto;
import com.piesat.site.datasearch.service.entity.MosnRecommend;
import com.piesat.site.datasearch.service.mapper.MosnRecommendMapper;
import com.piesat.site.datasearch.service.util.StringUtils;
import com.piesat.site.datasearch.service.vo.RecommendVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("mosnRecommendService")
public class MosnRecommendServiceImpl implements IMosnRecommendService {

    @Autowired
    private MosnRecommendMapper mosnRecommendMapper;

    @Override
    public List<RecommendDto> selectRecommendAll() {
        List<RecommendDto> recommendDtos = new ArrayList<>();
        List<RecommendVo> recommendList = mosnRecommendMapper.selectRecommendAll();
        if (StringUtils.isNotEmpty(recommendList)) {
            recommendList.forEach(recommend -> {
                RecommendDto recommendDto = new RecommendDto();
                recommendDto.setId(recommend.getId());
                recommendDto.setDetailName(recommend.getDetailName());
                recommendDto.setRecommendName(recommend.getRecommendName());
                recommendDto.setRecommendIcon(recommend.getRecommendIcon());
                recommendDto.setRecommendDetail(new RecommendDetailDto(recommend));
                recommendDtos.add(recommendDto);
            });
        }
        return recommendDtos;
    }

    @Override
    public Integer updateRecommend(MosnRecommend recommend) {
        return mosnRecommendMapper.updateRecommend(recommend);
    }

    @Override
    public Integer deleteRecommend(Long id) {
        return mosnRecommendMapper.deleteRecommend(id);
    }

    @Override
    public Integer insertRecommend(MosnRecommend recommend) {
        return mosnRecommendMapper.insertRecommend(recommend);
    }

    @Override
    public Integer add(String recommendName, String detailId, String imageStr) {
        MosnRecommend recommend = new MosnRecommend();
        recommend.setRecommendIcon(imageStr);
        recommend.setRecommendName(recommendName);
        recommend.setDetailId(detailId);
        return mosnRecommendMapper.insertRecommend(recommend);
    }

    @Override
    public Integer edit(Long id, String imageStr, String recommendName, String detailId) {
        MosnRecommend recommend = new MosnRecommend();
        recommend.setId(id);
        recommend.setRecommendIcon(imageStr);
        recommend.setRecommendName(recommendName);
        recommend.setDetailId(detailId);
        return mosnRecommendMapper.updateRecommend(recommend);
    }
}
