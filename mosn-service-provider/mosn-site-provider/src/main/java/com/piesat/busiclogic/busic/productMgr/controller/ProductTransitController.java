package com.piesat.busiclogic.busic.productMgr.controller;


import com.piesat.busiclogic.busic.productMgr.entity.ProductParams;
import com.piesat.busiclogic.busic.productMgr.service.ProductTransitService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 产品数据获取统一管理controller
 */
@RestController
@CrossOrigin
@RequestMapping("/pdutTransit/noAuth")
public class ProductTransitController {

       @Autowired
       private ProductTransitService productTransitService;

       public Wrapper getProducts(ProductParams productParams){
            return  WrapMapper.ok(productTransitService.getProducts(productParams));
       }


    @Description("获取url数据")
    @RequestMapping(value = "/getProductData", method = RequestMethod.GET)
    public Wrapper getProduct(@RequestParam String url){
        return  WrapMapper.ok(productTransitService.getProduct(url));
    }
}
