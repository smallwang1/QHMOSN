package com.piesat.busiclogic.busic.productMgr.service.impl;

import com.piesat.busiclogic.busic.productMgr.dao.ProductUnitDao;
import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;
import com.piesat.busiclogic.busic.productMgr.service.ProductUnitService;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

@Service
public class ProductUnitServiceImpl implements ProductUnitService {
    @Autowired
    private ProductUnitDao productUnitDao;

    @Override
    public void addproductUnitInfo(ProductUnit productUnit) {

        int i =  productUnitDao.addProductUnit(productUnit);
        if(i<=0){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public int deleteProductUnit(ProductUnit productUnit) {
        int i =   productUnitDao.deleteProductUnit(productUnit);
        return i;
    }

    @Override
    public void editProductUnit(ProductUnit productUnit) {
        if(PublicUtil.isEmpty(productUnit.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = productUnitDao.editProductUnit(productUnit);
    }

    @Override
    public ProductUnit getProductUnitById(ProductUnit productUnit) {
        ProductUnit productUnit1 =productUnitDao.getProductUnitById(productUnit);
        return productUnit1;
    }

    @Override
    public List<Map<String,Object>> getHtmlProducts(String htmlName, String time,String tempId) {
        // 时间格式处理
        String year = time.substring(0,4);
        String month = time.substring(4,6);
        List<Map<String,Object>> htmlTemplate =  productUnitDao.getHtmlTemplate(tempId);
        List<Map<String,Object>> htmlContent = productUnitDao.getHtmlContent(htmlName,year+"-"+month);
        if(htmlTemplate.size() > 0 && htmlContent.size() > 0){
            for (int i = 0; i < htmlContent.size(); i++) {
                String htemplate = String.valueOf(htmlTemplate.get(0).get("template_text"));
                String htmlCont = String.valueOf(htmlContent.get(i).get("report_text"));
                htmlCont = htemplate.replace("#replace",htmlCont);
                htmlContent.get(i).put("report_text",htmlCont);
                htmlContent.get(i).put("REPORT_TIME",new SimpleDateFormat("yyyy-MM-dd HH").format(htmlContent.get(i).get("REPORT_TIME")));
            }
        }
        return htmlContent;
    }
}
