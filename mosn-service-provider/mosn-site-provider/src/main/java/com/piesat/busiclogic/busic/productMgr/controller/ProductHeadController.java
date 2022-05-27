package com.piesat.busiclogic.busic.productMgr.controller;

import com.piesat.busiclogic.busic.productMgr.entity.ProductHead;
import com.piesat.busiclogic.busic.productMgr.service.ProductHeadService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.statistics.annotation.Statistics;
import com.piesat.common.statistics.constant.CensusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/pdutHeadMgr")
public class ProductHeadController extends BaseController {

    @Autowired
    private ProductHeadService productHeadService;

    @Description("获取产品维度树结构")
    @RequestMapping(value = "/noAuth/getProDuctHeadData", method = RequestMethod.GET)
    @Statistics(censusType = CensusType.SERVER, remark = "产品服务访问统计")
    public Wrapper getProductHeadData(HttpServletRequest request, @RequestParam String productid) {
        List<Map<String, Object>> list = productHeadService.getProductHeadData(productid);
        return WrapMapper.ok(list);
    }

    @Description("获取产品维度树结构")
    @RequestMapping(value = "/noAuth/getProDuctHeadAllData", method = RequestMethod.GET)
    public Wrapper getProDuctHeadAllData(HttpServletRequest request, @RequestParam String productid) {
        List<Map<String, Object>> list = productHeadService.getProDuctHeadAllData(productid);
        return WrapMapper.ok(list);
    }

    @Description("增加产品头配置信息")
    @RequestMapping(value = "/addProductHead", method = RequestMethod.POST)
    public Wrapper addProductHead(ProductHead productHead) {
        productHeadService.addproductHeadInfo(productHead);
        return WrapMapper.ok();
    }

    @Description("删除产品头配置信息")
    @RequestMapping(value = "/deleteProductHead", method = RequestMethod.POST)
    public Wrapper deleteProductHead(ProductHead productHead) {
        productHeadService.deleteProductHead(productHead);
        return WrapMapper.ok();
    }

    @Description("编辑产品头配置信息")
    @RequestMapping(value = "/editProductHead", method = RequestMethod.POST)
    public Wrapper editProductHead(ProductHead productHead) {
        productHeadService.editProductHead(productHead);
        return WrapMapper.ok();
    }

    @Description("由ID查产品头配置信息")
    @RequestMapping(value = "/noAuth/getProductHeadById", method = RequestMethod.POST)
    public Wrapper getProductHeadById(ProductHead productHead) {
        return this.handleResult( productHeadService.getProductHeadById(productHead));
    }

    @Description("移动维度")
    @RequestMapping(value = "/moveProductHead", method = RequestMethod.POST)
    public Wrapper moveProductHead(@RequestParam String id,@RequestParam String targetid,@RequestParam String flag) {
        productHeadService.moveProductHead(id,targetid,flag);
        return WrapMapper.ok();
    }
}
