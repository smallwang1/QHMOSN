package com.piesat.busiclogic.busic.productMgr.controller;

import com.piesat.busiclogic.busic.productMgr.entity.ProductHeadUnifed;
import com.piesat.busiclogic.busic.productMgr.service.ProductHeadUService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/pdutUnified")
public class ProductHeadUController {

    @Autowired
    private ProductHeadUService productHeadUService;

    @Description("查询下拉框信息")
    @RequestMapping(value = "/noAuth/getSelects", method = RequestMethod.GET)
    public Wrapper getSelects(ProductHeadUnifed productHeadUnifed) {
        return WrapMapper.ok(productHeadUService.getSelects(productHeadUnifed));
    }

    @Description("查询下拉选项信息")
    @RequestMapping(value = "/noAuth/getOptions", method = RequestMethod.GET)
    public Wrapper getOptions(ProductHeadUnifed productHeadUnifed) {
        return WrapMapper.ok( productHeadUService.getOptions(productHeadUnifed));
    }

    @Description("增加下拉框信息")
    @RequestMapping(value = "/addUnified", method = RequestMethod.POST)
    public Wrapper addUnified(ProductHeadUnifed productHeadUnifed) {
        productHeadUService.addUnified(productHeadUnifed);
        return WrapMapper.ok();
    }

    @Description("编辑下拉框信息")
    @RequestMapping(value = "/editUnified", method = RequestMethod.POST)
    public Wrapper editUnified(ProductHeadUnifed productHeadUnifed) {
        productHeadUService.editUnified(productHeadUnifed);
        return WrapMapper.ok();
    }

    @Description("删除下拉框信息、key_value 键值信息")
    @RequestMapping(value = "/deleteUnified", method = RequestMethod.POST)
    public Wrapper deleteUnified(String ids) {
        productHeadUService.deleteUnified(ids);
        return WrapMapper.ok();
    }

    @Description("编辑下拉框信息")
    @RequestMapping(value = "/noAuth/getUnifiedById", method = RequestMethod.POST)
    public Wrapper getUnifiedById(String id) {
        return WrapMapper.ok(productHeadUService.getUnifiedById(id));
    }

    @Description("增加或编辑键值信息")
    @RequestMapping(value = "/addoreditUnified", method = RequestMethod.POST)
    public Wrapper addoreditUnified(ProductHeadUnifed productHeadUnifed) {
        try{
            productHeadUService.addoreditUnified(productHeadUnifed);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return  WrapMapper.error(e.getMessage());
        }
    }
}
