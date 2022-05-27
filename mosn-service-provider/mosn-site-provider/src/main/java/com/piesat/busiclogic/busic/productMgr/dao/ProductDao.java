package com.piesat.busiclogic.busic.productMgr.dao;

import com.piesat.busiclogic.busic.productMgr.entity.Product;

import java.util.List;
import java.util.Map;

public interface ProductDao {
    List<Map<String,Object>> getProductData(Product product, String roleIds);

    int addProductInfo(Product product);

    int deleteProductInfo(Product product);

    int editProduct(Product product);

    Product getProductById(Product product);

    List<Map<String, Object>> getProducts(Product product);

    int updateSort(String sort,String id);

    List<Map<String,Object>> getNameByPid(String pid,String product_name);

    List<Map<String, Object>> getProductList(String pid, String sort,String flag);

    List<String> getRoleList(String userid);

    List<Map<String, Object>> getProductTreeAll(Product product);

    List<Map<String, Object>> getProductNode();

    List<Map<String,Object>> getAppProduct(String code,String roleIds);
}
