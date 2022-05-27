package com.piesat.busiclogic.busic.recommend.controller;

import com.piesat.busiclogic.busic.productMgr.entity.ProductHead;
import com.piesat.busiclogic.busic.recommend.entity.Recommend;
import com.piesat.busiclogic.busic.recommend.service.RecommendService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/recommendMgr/noAuth")
public class RecommendController extends BaseController {

    @Autowired
    private RecommendService recommendService;

    @Description("增加推荐信息")
    @RequestMapping(value = "/addRecommend", method = RequestMethod.POST)
    public Wrapper addRecommend(Recommend recommend) {
        recommendService.addRecommend(recommend);
        return WrapMapper.ok();
    }

    @Description("删除推荐服务")
    @RequestMapping(value = "/deleteRecommend", method = RequestMethod.POST)
    public Wrapper deleteRecommend(Recommend recommend) {
        recommendService.deleteRecommend(recommend);
        return WrapMapper.ok();
    }

    @Description("编辑推荐服务信息")
    @RequestMapping(value = "/editRecommend", method = RequestMethod.POST)
    public Wrapper editRecommend(Recommend recommend) {
        recommendService.editRecommend(recommend);
        return WrapMapper.ok();
    }

    @Description("获取推荐服务信息列表")
    @RequestMapping(value = "/getRecommendList", method = RequestMethod.GET)
    public Wrapper getProductHeadById(Recommend recommend) {
        return this.handleResult( recommendService.getRecommendList(recommend));
    }
}
