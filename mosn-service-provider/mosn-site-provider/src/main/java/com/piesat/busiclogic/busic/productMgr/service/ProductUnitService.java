package com.piesat.busiclogic.busic.productMgr.service;

import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;

import java.util.List;
import java.util.Map;

public interface ProductUnitService {
    void addproductUnitInfo(ProductUnit productUnit);

    int deleteProductUnit(ProductUnit productUnit);

    void editProductUnit(ProductUnit productUnit);

    ProductUnit getProductUnitById(ProductUnit productUnit);

    List<Map<String,Object>> getHtmlProducts(String htmlName, String time, String tempId);
}
