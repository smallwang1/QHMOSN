package com.piesat.busiclogic.busic.productMgr.dao.impl;

import com.piesat.busiclogic.busic.productMgr.dao.ProductHeadDao;
import com.piesat.busiclogic.busic.productMgr.entity.ProductHead;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
public class ProductHeadDaoImpl implements ProductHeadDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public int addProductInfo(ProductHead productHead) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  MGR_PRODUCT_HEAD");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("PRODUCT_ID,\n");
        sb.append("PID,\n");
        sb.append("NAME,\n");
        sb.append("VALUE,\n");
        sb.append("LEVEL,\n");
        sb.append("LEVELNAME, \n");
        sb.append("STATUS \n");
        sb.append(" ) values (");

        sb.append(":ID,\n");
        sb.append(":PRODUCT_ID,\n");
        sb.append(":PID,\n");
        sb.append(":NAME,\n");
        sb.append(":VALUE,\n");
        sb.append(":LEVEL,\n");
        sb.append(":LEVELNAME, \n");
        sb.append(":STATUS) \n");


        Map<String,Object> map = new HashMap<>();
        map.put("ID", UUID.randomUUID().toString());
        map.put("PRODUCT_ID", productHead.getProduct_id());
        map.put("PID", productHead.getPid());
        map.put("NAME",productHead.getName());
        map.put("VALUE", productHead.getValue());
        map.put("LEVEL", productHead.getLevel());
        map.put("LEVELNAME",productHead.getLevelName());
        map.put("STATUS",productHead.getStatus());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int deleteProductHead(ProductHead productHead) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("delete from MGR_PRODUCT_HEAD where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", productHead.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int editProductHead(ProductHead productHead) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update MGR_PRODUCT_HEAD set");
        if(!PublicUtil.isEmpty(productHead.getProduct_id())){
            sb.append(" PRODUCT_ID=:PRODUCT_ID ,");
        }
            sb.append(" NAME=:NAME,");
        if(!PublicUtil.isEmpty(productHead.getValue())){
            sb.append(" VALUE=:VALUE, ");
        }
        if(!PublicUtil.isEmpty(productHead.getLevel())){
            sb.append(" LEVEL=:LEVEL,");
        }
        if(!PublicUtil.isEmpty(productHead.getPid())){
            sb.append(" PID=:PID,");
        }
        if(!PublicUtil.isEmpty(productHead.getLevelName())){
            sb.append(" LEVELNAME=:LEVELNAME,");
        }
        if(!PublicUtil.isEmpty(productHead.getSort())){
            sb.append(" SORT=:SORT,");
        }
        sb.append(" STATUS=:STATUS ");
        sb.append(" WHERE ID =:ID ");

        Map<String,Object> map = new HashMap<>();
        map.put("PRODUCT_ID", productHead.getProduct_id());
        map.put("NAME", productHead.getName());
        map.put("VALUE", productHead.getValue());
        map.put("SORT", productHead.getSort());
        map.put("PID", productHead.getPid());
        map.put("STATUS", productHead.getStatus());
        map.put("LEVEL", productHead.getLevel());
        map.put("LEVELNAME", productHead.getLevelName());
        map.put("ID", productHead.getId());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public ProductHead getProductHeadById(ProductHead productHead) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" MGR_PRODUCT_HEAD where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", productHead.getId());
        return  pageHelper.findSingleData(sb.toString(), map, new CommonMapper(ProductHead.class));
    }

    @Override
    public List<Map<String, Object>> getProductHeadTree(String productid) {
        String qrySql = "select * from MGR_PRODUCT_HEAD where  status = '"+1+"' and PRODUCT_ID = :productid order by sort";
        Map<String,Object> map = new HashMap<>();
        map.put("productid", productid);
        return pageHelper.getJdbcTempalte().queryForList(qrySql,map);
    }

    @Override
    public List<Map<String,Object>> getNameByPid(String pid,String name,String productid) {
        String sql = "select id,name from MGR_PRODUCT_HEAD where pid =:pid and name = :name and product_id = :productid";
        Map<String,Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("name", name);
        map.put("productid", productid);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }

    @Override
    public List<Map<String, Object>> getProductHeadList(String pid, String sort, String flag) {
        String sql ="select * from MGR_PRODUCT_HEAD where pid = :pid and SORT > :sort";
        Map<String,Object> map = new HashMap<>();
        map.put("pid", pid);
        map.put("sort", sort);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }

    @Override
    public int updateSort(String sort, String id) {
        String sql = "update MGR_PRODUCT set sort=:sort where id=:id";
        Map<String,Object> map = new HashMap<>();
        map.put("sort",sort);
        map.put("id",id);
        return pageHelper.getJdbcTempalte().update(sql, map);
    }

    @Override
    public List<Map<String, Object>> getProDuctHeadAllData(String productid) {
        String qrySql = "select * from MGR_PRODUCT_HEAD where  status = '1' and PRODUCT_ID = :productid order by sort";
        Map<String,Object> map = new HashMap<>();
        map.put("productid",productid);
        return pageHelper.getJdbcTempalte().queryForList(qrySql,map);
    }
}
