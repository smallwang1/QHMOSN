package com.piesat.busiclogic.busic.productMgr.controller;

import com.piesat.busiclogic.busic.productMgr.entity.Product;
import com.piesat.busiclogic.busic.productMgr.service.ProductServie;
import com.piesat.common.anno.Description;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/pdutMgr")
public class ProductController extends BaseController {


    @Autowired
    private ProductServie productServie;

    @Description("产品树信息查询")
    @RequestMapping(value = "/getProductTree", method = RequestMethod.GET)
    public Wrapper getProductData(Product product) {
        try{
            return WrapMapper.ok(productServie.getProductData(product,getLoginAuthDto()));
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @Description("产品树非登录用户")
    @RequestMapping(value = "/noAuth/getProductTree", method = RequestMethod.GET)
    public Wrapper getProductDataNoLogin(Product product) {
        // 非登录用户默认赋予游客权限
        LoginAuthDto loginAuthDto = new LoginAuthDto();
        try{
            return WrapMapper.ok(productServie.getProductData(product,loginAuthDto));
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @Description("业务功能获取产品树所有信息")
    @RequestMapping(value = "/getProductTreeAll", method = RequestMethod.GET)
    public Wrapper getProductTreeAll(Product product) {
        return this.handleResult(productServie.getProductTreeAll(product));
    }

    @Description("增加产品配置信息")
    @RequestMapping(value = "/addProduct", method = RequestMethod.POST)
    public Wrapper addProduct(Product product) {
        productServie.addproductInfo(product);
        return WrapMapper.ok();
    }

    @Description("删除产品配置信息")
    @RequestMapping(value = "/deleteProduct", method = RequestMethod.POST)
    public Wrapper deleteProduct(Product product) {
        productServie.deleteProductInfo(product);
        return WrapMapper.ok();
    }

    @Description("编辑产品配置信息")
    @RequestMapping(value = "/editProduct", method = RequestMethod.POST)
    public Wrapper editRole(Product product) {
        productServie.editProduct(product);
        return WrapMapper.ok();
    }

    @Description("由ID查产品配置信息")
    @RequestMapping(value = "/getProductById", method = RequestMethod.POST)
    public Wrapper getProductById(Product product) {
        return this.handleResult( productServie.getProductById(product));
    }


    @Description("获取产品的id与name 列表")
    @RequestMapping(value = "/getProducts", method = RequestMethod.GET)
    public Wrapper getProducts(Product product) {
        return this.handleResult( productServie.getProducts(product));
    }


    @Description("获取产品和产品维度信息")
    @RequestMapping(value = "/getProductSet", method = RequestMethod.GET)
    public Wrapper getProductSet(Product product) {
        return this.handleResult( productServie.getProductSet(product));
    }


    @Description("移动产品")
    @RequestMapping(value = "/moveProduct", method = RequestMethod.POST)
    public Wrapper moveProduct(@RequestParam String id,@RequestParam String targetid,@RequestParam String flag) {
        productServie.moveProduct(id,targetid,flag);
        return WrapMapper.ok();
    }

    @Description("查询产品目录根节点")
    @RequestMapping(value = "/node", method = RequestMethod.GET)
    public Wrapper getProductNode() {
        return WrapMapper.ok(productServie.getProductNode());
    }
}
