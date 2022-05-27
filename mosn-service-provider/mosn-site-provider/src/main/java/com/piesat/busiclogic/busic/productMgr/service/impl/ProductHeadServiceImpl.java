package com.piesat.busiclogic.busic.productMgr.service.impl;

import com.piesat.busiclogic.busic.productMgr.dao.ProductHeadDao;
import com.piesat.busiclogic.busic.productMgr.entity.ProductHead;
import com.piesat.busiclogic.busic.productMgr.service.ProductHeadService;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service
public class ProductHeadServiceImpl implements ProductHeadService {

    @Autowired
    private ProductHeadDao productHeadDao;

    @Override
    public void addproductHeadInfo(ProductHead productHead) {
        List<Map<String,Object>> list = productHeadDao.getNameByPid(productHead.getPid(),productHead.getName(),productHead.getProduct_id());
        if(!PublicUtil.isEmpty(list)){
            throw new BusinessException("该节点下已经包含同名维度!");
        }
        int i =  productHeadDao.addProductInfo(productHead);
        if(i<=0){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void deleteProductHead(ProductHead productHead) {
        int i =   productHeadDao.deleteProductHead(productHead);
        if(i<=0){
            throw new BusinessException("删除失败");
        }
    }

    @Override
    public void editProductHead(ProductHead productHead) {
        if(PublicUtil.isEmpty(productHead.getId())){
            throw new BusinessException("缺少参数ID");
        }
        List<Map<String,Object>> list = productHeadDao.getNameByPid(productHead.getPid(),productHead.getName(),productHead.getProduct_id());
        if(!PublicUtil.isEmpty(list) && !list.get(0).get("ID").equals(productHead.getId())){
            throw new BusinessException("该节点下已经包含同名维度!");
        }
        int i = productHeadDao.editProductHead(productHead);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public ProductHead getProductHeadById(ProductHead productHead) {
        return productHeadDao.getProductHeadById(productHead);
    }

    @Override
    public List<Map<String, Object>> getProductHeadData(String productid) {
        List<Map<String,Object>> Resultlist = productHeadDao.getProductHeadTree(productid);
        try {
            Resultlist = Misc.recursion("R",Resultlist);
            return Resultlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void moveProductHead(String id, String targetid, String flag) {
        ProductHead productHead = new ProductHead();
        productHead.setId(targetid);
        ProductHead oraproduct = new ProductHead();
        oraproduct.setId(id);
        ProductHead productTarget = productHeadDao.getProductHeadById(productHead);
        ProductHead oraignal = productHeadDao.getProductHeadById(oraproduct);

        List<Map<String,Object>> list = productHeadDao.getProductHeadList(productTarget.getPid(),productTarget.getSort(),flag);
        // 移动到文件夹里面
        if("in".equals(flag)){
            oraignal.setPid(targetid);
            productHeadDao.editProductHead(oraignal);
        }else{
            // 更新目标序号
            for(Map<String,Object> map:list){
                productHeadDao.updateSort(String.valueOf(Integer.valueOf(String.valueOf(map.get("SORT")))+1),String.valueOf(map.get("ID")));
            }
            if(PublicUtil.isEmpty(productHead)){
                oraignal.setPid(targetid);
                oraignal.setSort("0");
            }else{
                oraignal.setPid(productTarget.getPid());
                oraignal.setSort(productTarget.getSort());
            }
            // 更新移动产品
            productHeadDao.editProductHead(oraignal);
        }
    }

    @Override
    public List<Map<String, Object>> getProDuctHeadAllData(String productid) {
        List<Map<String,Object>> Resultlist = productHeadDao.getProDuctHeadAllData(productid);
        try {
            Resultlist = Misc.recursion("R",Resultlist);
            return Resultlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
