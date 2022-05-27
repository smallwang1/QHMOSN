package com.piesat.busiclogic.busic.recommend.service.impl;

import com.piesat.busiclogic.busic.recommend.dao.RecommendDao;
import com.piesat.busiclogic.busic.recommend.entity.Recommend;
import com.piesat.busiclogic.busic.recommend.service.RecommendService;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class RecommendServiceImpl  implements RecommendService {

    @Autowired
    private RecommendDao recommendDao;

    @Override
    public void addRecommend(Recommend recommend) {
        int i =  recommendDao.addRecommend(recommend);
        if(i<=0){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void deleteRecommend(Recommend recommend) {
        int i =   recommendDao.deleteRecommend(recommend);
        if(i<=0){
            throw new BusinessException("删除失败");
        }
    }

    @Override
    public void editRecommend(Recommend recommend) {
        if(PublicUtil.isEmpty(recommend.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = recommendDao.editRecommend(recommend);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public List<Map<String,Object>> getRecommendList(Recommend recommend) {
        return recommendDao.getRecommendList(recommend);
    }
}
