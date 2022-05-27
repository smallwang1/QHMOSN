package com.piesat.site.datalist.service.impl;

import com.piesat.site.datalist.service.IProductService;
import com.piesat.site.datalist.service.entity.Product;
import com.piesat.site.datalist.service.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productService")
public class ProductServiceImpl implements IProductService {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public Product selectProductById(String id) {
        return productMapper.selectProductById(id);
    }

    @Override
    public List<Product> selectProductAll() {
        return productMapper.selectProductAll();
    }
}
