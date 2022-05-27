package com.piesat.busiclogic.busic.productMgr.dao.impl;

import com.piesat.busiclogic.busic.productMgr.dao.ProductDao;
import com.piesat.busiclogic.busic.productMgr.entity.Product;
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
public class ProductDaoImpl implements ProductDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public List<Map<String,Object>> getProductData(Product product,String roleIds) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" SELECT *");
        sb.append("    FROM mgr_product ");
        sb.append("    WHERE id IN (");
        sb.append("    SELECT DISTINCT id");
        sb.append(" FROM mgr_product, MGR_PRODUCT_PROTOROLE");
        sb.append(" WHERE mgr_product.id = MGR_PRODUCT_PROTOROLE.PRODUCT_ID");
        sb.append(" AND STATUS = '1' AND MGR_PRODUCT_PROTOROLE.ROLE_ID IN (:roleIds))");
        sb.append("   ORDER BY sort");
        Map<String,Object> map = new HashMap<>();
        map.put("roleIds", roleIds);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map);
    }

    @Override
    public int addProductInfo(Product product) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  MGR_PRODUCT");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("PRODUCT_NAME,\n");
        sb.append("STATUS,\n");
        sb.append("PID,\n");
        sb.append("ICON_DATA,\n");
        sb.append("IS_PRO\n");
        sb.append(" ) values (");

        sb.append(":ID,\n");
        sb.append(":PRODUCT_NAME,\n");
        sb.append(":STATUS,\n");
        sb.append(":PID,\n");
        sb.append(":ICON_DATA,\n");
        sb.append(":IS_PRO)\n");


        Map<String,Object> map = new HashMap<>();
        map.put("ID", UUID.randomUUID().toString());
        map.put("PRODUCT_NAME", product.getProduct_name());
        map.put("STATUS", product.getStatus());
        map.put("PID", product.getPid());
        map.put("ICON_DATA", product.getIcon_data());
        map.put("IS_PRO", product.getIs_pro());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int deleteProductInfo(Product product) {
        StringBuilder sb = new StringBuilder(100);
        sb.append("delete from MGR_PRODUCT where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", product.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int editProduct(Product product) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update MGR_PRODUCT set");
        sb.append(" PRODUCT_NAME=:PRODUCT_NAME,");
        if(!PublicUtil.isEmpty(product.getIs_pro())){
            sb.append(" IS_PRO=:IS_PRO, ");
        }
        if(!PublicUtil.isEmpty(product.getPid())){
            sb.append(" PID=:PID,");
        }
        if(!PublicUtil.isEmpty(product.getSort())){
            sb.append(" SORT=:SORT,");
        }
        if(!PublicUtil.isEmpty(product.getIcon_data())){
            sb.append(" ICON_DATA=:ICON_DATA");
        }
        sb.append(" STATUS=:STATUS ");
        sb.append(" WHERE ID =:ID ");

        Map<String,Object> map = new HashMap<>();
        map.put("PRODUCT_NAME", product.getProduct_name());
        map.put("STATUS", product.getStatus());
        map.put("PID", product.getPid());
        map.put("SORT", product.getSort());
        map.put("IS_PRO", product.getIs_pro());
        map.put("ICON_DATA",product.getIcon_data());
        map.put("ID", product.getId());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public Product getProductById(Product product) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" MGR_PRODUCT where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", product.getId());
        return  pageHelper.findSingleData(sb.toString(), map, new CommonMapper(Product.class));
    }

    @Override
    public List<Map<String, Object>> getProducts(Product product) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from mgr_product where status = '1' order by sort ");
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sb.toString());
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
    public List<Map<String,Object>> getNameByPid(String pid,String product_name) {
        String sql = "select id,product_name from MGR_PRODUCT where pid =:pid and product_name=:product_name";
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        map.put("product_name",product_name);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }

    @Override
    public List<Map<String, Object>> getProductList(String pid, String sort,String flag) {
        String sql ="select * from MGR_PRODUCT where pid =:pid and SORT > :sort";
        Map<String,Object> map = new HashMap<>();
        map.put("pid",pid);
        map.put("sort",sort);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }

    @Override
    public List<String> getRoleList(String userid) {
        String sql = "select role_id from sys_user_role where user_id=:user_id";
        Map<String,Object> map = new HashMap<>();
        map.put("user_id",userid);
        return pageHelper.getJdbcTempalte().queryForList(sql,map,String.class);
    }

    @Override
    public List<Map<String, Object>> getProductTreeAll(Product product) {
        String sql = "select * from MGR_PRODUCT order by sort  ";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getProductNode() {
        String sql = "select id,product_name from mgr_product where pid = 'R'";
        return pageHelper.getJdbcTempalte().getJdbcTemplate().queryForList(sql);
    }

    @Override
    public List<Map<String, Object>> getAppProduct(String code, String roleIds) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" SELECT *");
        sb.append("    FROM mgr_product_app ");
//        sb.append("    WHERE id IN (");
//        sb.append("    SELECT DISTINCT id");
//        sb.append(" FROM mgr_product_app");
//        sb.append(", MGR_PRODUCT_PROTOROLE  WHERE mgr_product.id = MGR_PRODUCT_PROTOROLE.PRODUCT_ID");
        sb.append(" where APP_CODE =:code ");
        sb.append(" AND STATUS = '1'");
//        sb.append(" AND STATUS = '"+1+"' AND MGR_PRODUCT_PROTOROLE.ROLE_ID IN ('"+roleIds+"'))");
        sb.append("   ORDER BY sort");
        Map<String,Object> map = new HashMap<>();
        map.put("code",code);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map);
    }
}
