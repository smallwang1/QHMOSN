package com.piesat.busiclogic.busic.productMgr.service.impl;

import com.piesat.busiclogic.busic.productMgr.dao.ProductUnitDao;
import com.piesat.busiclogic.busic.productMgr.entity.DataSet;
import com.piesat.busiclogic.busic.productMgr.entity.ProductParams;
import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;
import com.piesat.busiclogic.busic.productMgr.service.ProductTransitService;
import com.piesat.busiclogic.common.Util.HttpUtil;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProductTransitServiceImpl implements ProductTransitService {

    @Autowired
    private ProductUnitDao productUnitDao;

    @Override
    public Object getProducts(ProductParams productParams) {
        if(PublicUtil.isEmpty(productParams.getId())){
            throw new BusinessException("缺少参数id");
        }
        ProductUnit productUnitParam = new ProductUnit();
        productUnitParam.setId(productParams.getId());
        ProductUnit productUnit = productUnitDao.getProductUnitById(productUnitParam);

        if("get".equalsIgnoreCase(productUnit.getRequest_type())){
            // 参数拼接
            // 固定参数
            String params = productUnit.getParam();
            Map<String,String> paramMap = new HashMap<>();











        }else if("post".equalsIgnoreCase(productUnit.getRequest_type())){

        }



















        return null;
    }

    @Override
    public Object getProduct(String url) {
        try {
            URL url1 = new URL(url);
            String result = PublicUtil.getResultData(url1);
            JSONObject json = JSONObject.fromObject(result);
            DataSet dataSet = new DataSet();
            dataSet.setData(json.get("DS")==null?"":json.get("DS"));
            dataSet.setFieldNames(json.get("fieldNames")==null?"":json.get("fieldNames"));
            return dataSet;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return  null;
        }
    }
}
