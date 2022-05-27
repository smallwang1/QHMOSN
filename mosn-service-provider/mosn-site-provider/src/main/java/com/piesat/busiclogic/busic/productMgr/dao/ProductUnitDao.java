package com.piesat.busiclogic.busic.productMgr.dao;

import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;

import java.util.List;
import java.util.Map;

public interface ProductUnitDao {
    int addProductUnit(ProductUnit productUnit);

    int deleteProductUnit(ProductUnit productUnit);

    int editProductUnit(ProductUnit productUnit);

    ProductUnit getProductUnitById(ProductUnit productUnit);

    List<Map<String, Object>> getHtmlTemplate(String tempId);

    List<Map<String, Object>> getHtmlContent(String htmlName, String time);
}
