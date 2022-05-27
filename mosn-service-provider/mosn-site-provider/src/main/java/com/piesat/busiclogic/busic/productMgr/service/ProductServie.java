package com.piesat.busiclogic.busic.productMgr.service;

import com.piesat.busiclogic.busic.productMgr.entity.Product;
import com.piesat.busiclogic.busic.productMgr.entity.ProductSet;
import com.piesat.common.core.dto.LoginAuthDto;

import java.util.List;
import java.util.Map;

public interface ProductServie {
    public List<Map<String,Object>> getProductData(Product product, LoginAuthDto loginAuthDto) throws Exception;

    void addproductInfo(Product product);

    void deleteProductInfo(Product product);

    void editProduct(Product product);

    Product getProductById(Product product);

    List<Map<String,Object>> getProducts(Product product);

    ProductSet getProductSet(Product product);

    void moveProduct(String id,String targetid,String flag);

    public List<Map<String,Object>> getProductTreeAll(Product product);

    List<Map<String,Object>> getProductNode();

    List<Map<String, Object>> getProductDataByCode(String code, LoginAuthDto loginAuthDto) throws Exception;
}
