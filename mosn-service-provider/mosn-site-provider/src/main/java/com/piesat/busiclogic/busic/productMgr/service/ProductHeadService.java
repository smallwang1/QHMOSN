package com.piesat.busiclogic.busic.productMgr.service;

import com.piesat.busiclogic.busic.productMgr.entity.ProductHead;

import java.util.List;
import java.util.Map;

public interface ProductHeadService {
    void addproductHeadInfo(ProductHead productHead);

    void deleteProductHead(ProductHead productHead);

    void editProductHead(ProductHead productHead);

    ProductHead getProductHeadById(ProductHead productHead);

    List<Map<String, Object>> getProductHeadData(String productid);

    void moveProductHead(String id, String targetid, String flag);

    List<Map<String, Object>> getProDuctHeadAllData(String productid);
}
