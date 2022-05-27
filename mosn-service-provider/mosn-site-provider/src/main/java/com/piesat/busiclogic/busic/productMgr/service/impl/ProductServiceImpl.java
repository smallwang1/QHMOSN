package com.piesat.busiclogic.busic.productMgr.service.impl;

import com.piesat.busiclogic.app.util.AppUtil;
import com.piesat.busiclogic.busic.productMgr.dao.ProductDao;
import com.piesat.busiclogic.busic.productMgr.dao.ProductHeadDao;
import com.piesat.busiclogic.busic.productMgr.entity.Product;
import com.piesat.busiclogic.busic.productMgr.entity.ProductSet;
import com.piesat.busiclogic.busic.productMgr.service.ProductServie;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductServie {
    @Autowired
    private ProductDao productDao;

    @Autowired
    private ProductHeadDao productHeadDao;

    @Override
    public List<Map<String,Object>> getProductData (Product product, LoginAuthDto loginAuthDto) throws Exception {
        List<String> roleIdList = new ArrayList<>();
        if(PublicUtil.isEmpty(loginAuthDto.getUserId())){// 如果用户是处于非登录状态，赋予游客角色
            roleIdList.add(Misc.getPropValue("transparent.properties","visitor"));
        }else{  // 查询用户所具有得所有角色
            roleIdList = productDao.getRoleList(String.valueOf(loginAuthDto.getUserId()));
            if(PublicUtil.isEmpty(roleIdList)){ //默认赋予用户普通用户角色
                roleIdList.add(Misc.getPropValue("transparent.properties","nonal"));
            }
        }
        List<Map<String,Object>> plist = productDao.getProductData(product,String.join("','",roleIdList));
        // 默认查询所有产品树数据
        String flag = "R";
        if(!PublicUtil.isEmpty(product.getId())){
            flag = product.getId();
        }
        plist = Misc.recursion(flag,plist);
        return plist;
    }

    @Override
    public List<Map<String, Object>> getProductDataByCode(String code, LoginAuthDto loginAuthDto) throws Exception{
        List<String> roleIdList = new ArrayList<>();
        roleIdList.add("101");
//        if(PublicUtil.isEmpty(loginAuthDto.getUserId())){// 如果用户是处于非登录状态，赋予游客角色
//            roleIdList.add(Misc.getPropValue("transparent.properties","nonal"));
//        }else{  // 查询用户所具有得所有角色
//            roleIdList = productDao.getRoleList(String.valueOf(loginAuthDto.getUserId()));
//            if(PublicUtil.isEmpty(roleIdList)){ //默认赋予用户普通用户角色
//                roleIdList.add(Misc.getPropValue("transparent.properties","nonal"));
//            }
//        }
        return productDao.getAppProduct(code,String.join("','",roleIdList));
    }


    @Override
    public List<Map<String, Object>> getProductTreeAll(Product product) {
        List<Map<String,Object>> plist = productDao.getProductTreeAll(product);
        // 默认查询所有产品树数据
        String flag = "R";
        if(!PublicUtil.isEmpty(product.getId())){
            flag = product.getId();
        }
        try {
            plist = Misc.recursion(flag,plist);
            return plist;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getProductNode() {
        return productDao.getProductNode();
    }

    @Override
    @Transactional
    public void addproductInfo(Product product) {
        List<Map<String,Object>> list = productDao.getNameByPid(product.getPid(),product.getProduct_name());
        if(!PublicUtil.isEmpty(list)){
            throw new BusinessException("该节点下已经包含同名产品!");
        }
        int i =  productDao.addProductInfo(product);
        if(i<=0){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void deleteProductInfo(Product product) {
        int i =   productDao.deleteProductInfo(product);
        if(i<=0){
            throw new BusinessException("删除失败");
        }
    }

    @Override
    @Transactional
    public void editProduct(Product product) {
        if(PublicUtil.isEmpty(product.getId())){
            throw new BusinessException("缺少参数ID");
        }
        List<Map<String,Object>> list = productDao.getNameByPid(product.getPid(),product.getProduct_name());
        if(!PublicUtil.isEmpty(list) && !list.get(0).get("ID").equals(product.getId())){
            throw new BusinessException("该节点下已经包含同名产品!");
        }
        int i = productDao.editProduct(product);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public Product getProductById(Product product) {
        return productDao.getProductById(product);
    }

    @Override
    public List<Map<String, Object>> getProducts(Product product) {
        List<Map<String,Object>> plist = productDao.getProducts(product);
        List<Map<String,Object>> dataList = new ArrayList<>();
        // 默认查询所有产品树数据
        String flag = "R";
        if(!PublicUtil.isEmpty(product.getId())){
            flag = product.getId();
        }
        try {
            plist = Misc.recursion(flag,plist);
            // 遍历给产品赋菜单id
            for (int i = 0; i < plist.size(); i++) {
                String menu_id = Misc.getPropValue("transparent.properties",String.valueOf(plist.get(i).get("ID")));
                AppUtil appUtil = new AppUtil();
                List<Map<String,Object>> eleList = appUtil.getHandleData(plist.get(i));
                for (int i1 = 0; i1 < eleList.size(); i1++) {
                    eleList.get(i1).put("menu_id",menu_id);
                }
                dataList.addAll(eleList);
            }
            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public ProductSet getProductSet(Product product) {
        ProductSet productSet = new ProductSet();
        Product product1 =  productDao.getProductById(product);
        productSet.setProduct(product1);
        List<Map<String,Object>> Resultlist = productHeadDao.getProductHeadTree(product.getId());
        try {
            Resultlist = Misc.recursion("0",Resultlist);
            productSet.setTreeData(Resultlist);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return  productSet;
    }

    @Override
    @Transactional
    public void moveProduct(String id,String targetid,String flag) {
        Product product = new Product();
        product.setId(targetid);
        Product oraproduct = new Product();
        oraproduct.setId(id);
        Product productTarget = productDao.getProductById(product);
        Product oraignal = productDao.getProductById(oraproduct);
        List<Map<String,Object>> list = productDao.getProductList(productTarget.getPid(),productTarget.getSort(),flag);
        // 移动到文件夹里面
        if("in".equals(flag)){
            oraignal.setPid(targetid);
//            if(list.size()>0){
//                oraignal.setSort(String.valueOf(Integer.valueOf(list.get(0).get("SORT"))+1));
//            }
            productDao.editProduct(oraignal);
        }else{
            // 更新目标序号
            for(Map<String,Object> map:list){
                productDao.updateSort(String.valueOf(Integer.valueOf(String.valueOf(map.get("SORT")))+1),String.valueOf(map.get("ID")));
            }
            if(PublicUtil.isEmpty(product)){
                oraignal.setPid(targetid);
                oraignal.setSort("0");
            }else{
                oraignal.setPid(productTarget.getPid());
                oraignal.setSort(String.valueOf(Integer.valueOf(productTarget.getSort())+1));
            }
            // 更新移动产品
            productDao.editProduct(oraignal);
        }
    }
}
