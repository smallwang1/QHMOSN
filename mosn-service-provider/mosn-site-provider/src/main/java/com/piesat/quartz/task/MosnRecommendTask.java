package com.piesat.quartz.task;

import com.piesat.site.datalist.service.IMenuService;
import com.piesat.site.datalist.service.IProductService;
import com.piesat.site.datalist.service.entity.MenuInfo;
import com.piesat.site.datalist.service.entity.Product;
import com.piesat.site.datalist.service.vo.MenuVo;
import com.piesat.site.datasearch.service.IMosnRecommendDetailService;
import com.piesat.site.datasearch.service.entity.MosnRecommendDetail;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.List;

@Component("mosnRecommendTask")
public class MosnRecommendTask {

    @Autowired
    private IProductService productService;
    @Autowired
    private IMenuService menuService;
    @Autowired
    private IMosnRecommendDetailService mosnRecommendDetailService;

    public void generateRecommend() {
        List<Product> productList = productService.selectProductAll();
        if (StringUtils.isEmpty(productList)) return;

        for (Product product : productList) {
            Product rootProduct = getRootProduct(product);
            if (null == rootProduct)
                continue;

            MenuVo menu = new MenuVo();
            menu.setProductId(rootProduct.getId());
            List<MenuInfo> menuInfos = menuService.selectMenuList(menu);
            MosnRecommendDetail recommendDetail = new MosnRecommendDetail();
            if (StringUtils.isEmpty(menuInfos)) continue;
            MenuInfo menuInfo = menuInfos.get(0);
            if (StringUtils.isEmpty(menuInfo.getProductId())) continue;
            recommendDetail.setUrl(menuInfo.getUrl());
            recommendDetail.setIsLink(menuInfo.getIsLink());
            recommendDetail.setProductId(menuInfo.getProductId());
            recommendDetail.setValue(menuInfo.getSort());
            recommendDetail.setId(product.getId());
            recommendDetail.setPid(product.getPid());
            recommendDetail.setName(product.getProductName());

            try {
                mosnRecommendDetailService.insertRecommendDetail(recommendDetail);
            } catch (Exception e) {
                mosnRecommendDetailService.updateRecommendDetail(recommendDetail);
            }
        }
    }

    public Product getRootProduct(Product product) {
        if (null == product) return null;
        if ("R".equals(product.getPid()))
            return product;
        else
            return getRootProduct(productService.selectProductById(product.getPid()));
    }
}
