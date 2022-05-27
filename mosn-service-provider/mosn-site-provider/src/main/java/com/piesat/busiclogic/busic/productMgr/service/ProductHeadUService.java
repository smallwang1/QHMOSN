package com.piesat.busiclogic.busic.productMgr.service;

import com.piesat.busiclogic.busic.productMgr.entity.ProductHeadUnifed;

import java.util.List;
import java.util.Map;

public interface ProductHeadUService {
    void addUnified(ProductHeadUnifed productHeadUnifed);

    void editUnified(ProductHeadUnifed productHeadUnifed);

    void deleteUnified(String ids);

    void addoreditUnified(ProductHeadUnifed productHeadUnifed) throws Exception;

    ProductHeadUnifed getUnifiedById(String id);

    List<Map<String,Object>> getSelects(ProductHeadUnifed productHeadUnifed);

    List<Map<String,Object>> getOptions(ProductHeadUnifed productHeadUnifed);
}
