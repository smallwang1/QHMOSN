package com.piesat.busiclogic.busic.menuset.service.impl;

import com.piesat.busiclogic.busic.menuset.dao.MenuMgrDao;
import com.piesat.busiclogic.busic.menuset.entity.MenuInfo;
import com.piesat.busiclogic.busic.menuset.service.MenuMgrService;
import com.piesat.busiclogic.busic.productMgr.entity.Product;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class MenuMgrServiceImpl implements MenuMgrService {

    @Autowired
    private MenuMgrDao menuMgrDao;

    @Override
    public Page<MenuInfo> getMenuData(long currentPage, int pageSize, MenuInfo menuInfo) {
        return menuMgrDao.getMenuData(currentPage,pageSize,menuInfo);
    }

    @Override
    public void addMenuInfo(MenuInfo menuInfo) {
        int i =  menuMgrDao.addMenuInfo(menuInfo);
        if(i<=0){
            throw new BusinessException("新增失败");
        }
    }

    @Override
    public void deleteMenuInfo(MenuInfo menuInfo) {
        if(PublicUtil.isEmpty(menuInfo.getId())){
            throw new BusinessException("缺少参数ID");
        }
         menuMgrDao.deleteMenuInfo(menuInfo) ;
    }

    @Override
    public void editMenuInfo(MenuInfo menuInfo) {
        if(PublicUtil.isEmpty(menuInfo.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = menuMgrDao.editStation(menuInfo);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public MenuInfo getMenuById(MenuInfo menuInfo) {
        return menuMgrDao.getMenuInfoById(menuInfo);
    }

    @Override
    public List<Map<String, Object>> getMenuSetDataByTree(String code) {
        List<Map<String,Object>> Resultlist = menuMgrDao.getMenuSetByCode(code);

        try {
            Resultlist = Misc.recursion("R",Resultlist);
            return Resultlist;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Map<String, Object>> getMenuHead() {
        return menuMgrDao.getMenuHead();
    }

    @Override
    @Transactional
    public void moveMenu(String id, String targetid, String flag) {
        MenuInfo product = new MenuInfo();
        product.setId(targetid);
        MenuInfo oraproduct = new MenuInfo();
        oraproduct.setId(id);
        MenuInfo productTarget = menuMgrDao.getMenuInfoById(product);
        MenuInfo oraignal = menuMgrDao.getMenuInfoById(oraproduct);
        List<Map<String,Object>> list = menuMgrDao.getTheLowSortList(productTarget.getPid(),productTarget.getSort(),flag);
        // 移动到文件夹里面
        if("in".equals(flag)){
            oraignal.setPid(targetid);
            menuMgrDao.editStation(oraignal);
        }else{
            // 更新目标序号
            for(Map<String,Object> map:list){
                menuMgrDao.updateSort(String.valueOf(Integer.valueOf(String.valueOf(map.get("SORT")))+1),String.valueOf(map.get("ID")));
            }
            if(PublicUtil.isEmpty(product)){
                oraignal.setPid(targetid);
                oraignal.setSort("0");
            }else{
                oraignal.setPid(productTarget.getPid());
                oraignal.setSort(String.valueOf(Integer.valueOf(productTarget.getSort())+1));
            }
            // 更新移动产品
            menuMgrDao.editStation(oraignal);
        }
    }
}
