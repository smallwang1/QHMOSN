package com.piesat.busiclogic.busic.productMgr.service.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.piesat.busiclogic.busic.productMgr.dao.ProductHeadUDao;
import com.piesat.busiclogic.busic.productMgr.entity.Option;
import com.piesat.busiclogic.busic.productMgr.entity.ProductHeadUnifed;
import com.piesat.busiclogic.busic.productMgr.service.ProductHeadUService;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;

@Service
public class ProductHeadUServiceImpl implements ProductHeadUService {

    @Autowired
    private ProductHeadUDao productHeadUDao;

    @Override
    public void addUnified(ProductHeadUnifed productHeadUnifed) {
        int i =  productHeadUDao.addUnified(productHeadUnifed);
        if(i<=0){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void editUnified(ProductHeadUnifed productHeadUnifed) {
        int i = productHeadUDao.editUnified(productHeadUnifed);
        if (i <= 0) {
            throw new BusinessException("编辑失败");
        }
    }

    @Override
    public void deleteUnified(String ids) {
         productHeadUDao.deleteUnified(ids);
    }

    @Override
    public ProductHeadUnifed getUnifiedById(String id) {
        return productHeadUDao.getUnifiedById(id);
    }

    @Override
    public List<Map<String,Object>> getSelects(ProductHeadUnifed productHeadUnifed) {
        return productHeadUDao.getSelects(productHeadUnifed.getProduct_id());
    }

    @Override
    public List<Map<String,Object>> getOptions(ProductHeadUnifed productHeadUnifed) {
        return productHeadUDao.getOptions(productHeadUnifed.getProduct_id(),productHeadUnifed.getSelect_id());
    }

    @Override
    @Transactional
    public void addoreditUnified(ProductHeadUnifed productHeadUnifed) throws IOException {
        if(!PublicUtil.isEmpty(productHeadUnifed.getListOpions())){
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<Option> optionList = mapper.readValue(productHeadUnifed.getListOpions(),new TypeReference<List<Option>>() { });

            List<Option> addOptionList = new ArrayList<>();
            List<Option> editOptionList = new ArrayList<>();
            List<String> keyList = new ArrayList<>();
            List<String> editIDlist = new ArrayList<>();

            for(Option option:optionList){
                keyList.add(option.getKey());
                if(PublicUtil.isEmpty(option.getId())){
                    addOptionList.add(option);

                }else{
                    editOptionList.add(option);
                    editIDlist.add(option.getId());
                }
            }
            // 校验是否有重复的key value数据
            List<Map<String,Object>> repeatData = productHeadUDao.getRepeatData(productHeadUnifed.getId(),keyList,editIDlist);

            if(repeatData.size()>0){
                throw new BusinessException("数据重复:"+ repeatData);
            }else{
                // 批量新增
                if(!PublicUtil.isEmpty(addOptionList)){
                    productHeadUDao.batchAdd(addOptionList,productHeadUnifed);
                }
                // 批量编辑
                if(!PublicUtil.isEmpty(editOptionList)){
                    productHeadUDao.batchEdit(editOptionList,productHeadUnifed);
                }
            }
        }
    }
}
