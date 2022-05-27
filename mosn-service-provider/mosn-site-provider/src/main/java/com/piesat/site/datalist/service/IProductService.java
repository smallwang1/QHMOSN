package com.piesat.site.datalist.service;

import com.piesat.site.datalist.service.entity.Product;

import java.util.List;

public interface IProductService {

    Product selectProductById(String id);

    List<Product> selectProductAll();
}
