package com.piesat.busiclogic.busic.productMgr.service;

import com.piesat.busiclogic.busic.productMgr.entity.ProductParams;

public interface ProductTransitService {
    Object getProducts(ProductParams productParams);

    Object getProduct(String url);
}
