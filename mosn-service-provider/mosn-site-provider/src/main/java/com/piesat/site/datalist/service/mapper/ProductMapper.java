package com.piesat.site.datalist.service.mapper;

import com.piesat.site.datalist.service.entity.Product;

import java.util.List;

public interface ProductMapper {

    Product selectProductById(String id);

    List<Product> selectProductAll();
}
