package com.piesat.busiclogic.busic.productMgr.dao;

import com.piesat.busiclogic.busic.productMgr.entity.ProductHead;

import java.util.List;
import java.util.Map;

public interface ProductHeadDao {
    int addProductInfo(ProductHead productHead);

    int deleteProductHead(ProductHead productHead);

    int editProductHead(ProductHead productHead);

    ProductHead getProductHeadById(ProductHead productHead);

    List<Map<String, Object>> getProductHeadTree(String productid);

    List<Map<String,Object>> getNameByPid(String pid,String name,String productid);

    List<Map<String, Object>> getProductHeadList(String pid, String sort, String flag);

    int updateSort(String sort, String id);

    List<Map<String, Object>> getProDuctHeadAllData(String productid);
}
