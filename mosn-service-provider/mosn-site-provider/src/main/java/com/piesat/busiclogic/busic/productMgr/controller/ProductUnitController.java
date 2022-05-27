package com.piesat.busiclogic.busic.productMgr.controller;

import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;
import com.piesat.busiclogic.busic.productMgr.service.ProductUnitService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/pdutUnitMgr")
public class ProductUnitController extends BaseController {

    @Autowired
    private ProductUnitService productUnitService;


    @Description("新增单元信息")
    @RequestMapping(value = "/addProductUnit", method = RequestMethod.POST)
    public Wrapper addProductUnit(ProductUnit productUnit) {
        productUnitService.addproductUnitInfo(productUnit);
        return WrapMapper.ok();
    }

    @Description("删除单元信息")
    @RequestMapping(value = "/deleteProductUnit", method = RequestMethod.POST)
    public Wrapper deleteProductHead(ProductUnit productUnit) {
        productUnitService.deleteProductUnit(productUnit);
        return WrapMapper.ok();
    }

    @Description("编辑单元信息")
    @RequestMapping(value = "/editProductUnit", method = RequestMethod.POST)
    public Wrapper editProductHead(ProductUnit productUnit) {
        productUnitService.editProductUnit(productUnit);
        return WrapMapper.ok();
    }

    @Description("由ID查询单元信息")
    @RequestMapping(value = "/noAuth/getProductUnitById", method = RequestMethod.POST)
    public Wrapper getProductHeadById(ProductUnit productUnit) {
        return WrapMapper.ok( productUnitService.getProductUnitById(productUnit));
    }


    @Description("获取html类型产品信息")
    @RequestMapping(value = "/noAuth/getProductHtml", method = RequestMethod.GET)
    public Wrapper getHtmlProduct(@RequestParam String htmlName,@RequestParam String times,@RequestParam String tempId){
        try{
         return  WrapMapper.ok(productUnitService.getHtmlProducts(htmlName,times,tempId));
        }catch (Exception e){
           return WrapMapper.error(e.getMessage());
        }
    }
}
