package com.piesat.busiclogic.busic.productMgr.dao;

import com.piesat.busiclogic.busic.productMgr.entity.Option;
import com.piesat.busiclogic.busic.productMgr.entity.ProductHeadUnifed;

import java.util.List;
import java.util.Map;

public interface ProductHeadUDao {
    int addUnified(ProductHeadUnifed productHeadUnifed);

    int editUnified(ProductHeadUnifed productHeadUnifed);

    void deleteUnified(String ids);

    ProductHeadUnifed getUnifiedById(String id);

    void batchAdd(List<Option> addOptionList,ProductHeadUnifed productHeadUnifed);

    void batchEdit(List<Option> addOptionList, ProductHeadUnifed productHeadUnifed);

    List<Map<String, Object>> getSelects(String product_id);

    List<Map<String, Object>> getOptions(String product_id,String select_id);

    List<Map<String,Object>>  getRepeatData(String select_id,List<String> keyList,List<String> valueList);
}
