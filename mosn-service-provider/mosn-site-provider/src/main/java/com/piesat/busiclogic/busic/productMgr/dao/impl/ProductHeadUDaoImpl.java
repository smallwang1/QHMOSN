package com.piesat.busiclogic.busic.productMgr.dao.impl;

import com.piesat.busiclogic.busic.productMgr.dao.ProductHeadUDao;
import com.piesat.busiclogic.busic.productMgr.entity.Option;
import com.piesat.busiclogic.busic.productMgr.entity.ProductHeadUnifed;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;
@Repository
public class ProductHeadUDaoImpl implements ProductHeadUDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public int addUnified(ProductHeadUnifed productHeadUnifed) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  MGR_PRODUCT_HEAD_UNIFIED");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("PRODUCT_ID,\n");
        sb.append("SELECT_NAME,\n");
        sb.append("IS_SELECT,\n");
        sb.append("MULTIPLE_SELECT,\n");
        sb.append("SELECT_VALUE \n");
        sb.append(" ) values (");

        sb.append(":ID,\n");
        sb.append(":PRODUCT_ID,\n");
        sb.append(":SELECT_NAME,\n");
        sb.append(":IS_SELECT,\n");
        sb.append(":MULTIPLE_SELECT,\n");
        sb.append(":SELECT_VALUE)\n");

        Map<String,Object> map = new HashMap<>();
        map.put("ID", UUID.randomUUID().toString());
        map.put("PRODUCT_ID", productHeadUnifed.getProduct_id());
        map.put("SELECT_NAME", productHeadUnifed.getSelect_name());
        map.put("MULTIPLE_SELECT", productHeadUnifed.getMultiple_select());
        map.put("IS_SELECT", productHeadUnifed.getIs_select());
        map.put("SELECT_VALUE", productHeadUnifed.getSelect_value());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int editUnified(ProductHeadUnifed productHeadUnifed) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update MGR_PRODUCT_HEAD_UNIFIED set");
        if(!PublicUtil.isEmpty(productHeadUnifed.getProduct_id())){
            sb.append(" PRODUCT_ID=:PRODUCT_ID,");
        }
            sb.append(" SELECT_NAME=:SELECT_NAME, ");
        if(!PublicUtil.isEmpty(productHeadUnifed.getSelect_value())){
            sb.append(" SELECT_VALUE=:SELECT_VALUE,");
        }
        if(!PublicUtil.isEmpty(productHeadUnifed.getIs_select())){
            sb.append(" IS_SELECT=:IS_SELECT,");
        }
        sb.append(" MULTIPLE_SELECT=:MULTIPLE_SELECT, ");
        sb.append(" EXPAND1=:EXPAND1 ");
        sb.append(" WHERE ID =:ID ");

        Map<String,Object> map = new HashMap<>();
        map.put("PRODUCT_ID", productHeadUnifed.getProduct_id());
        map.put("SELECT_NAME", productHeadUnifed.getSelect_name());
        map.put("SELECT_VALUE", productHeadUnifed.getSelect_value());
        map.put("MULTIPLE_SELECT", productHeadUnifed.getMultiple_select());
        map.put("IS_SELECT", productHeadUnifed.getIs_select());
        map.put("EXPAND1", productHeadUnifed.getExpand1());
        map.put("ID", productHeadUnifed.getId());

        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public void deleteUnified(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        String sql = "delete from MGR_PRODUCT_HEAD_UNIFIED where id = ? ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public ProductHeadUnifed getUnifiedById(String id) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from ");
        sb.append(" MGR_PRODUCT_HEAD_UNIFIED where ID=:ID ");
        Map<String,Object> map = new HashMap<>();
        map.put("ID", id);
        return  pageHelper.findSingleData(sb.toString(), map, new CommonMapper(ProductHeadUnifed.class));
    }

    @Override
    public void batchAdd(List<Option> addOptionList,ProductHeadUnifed productHeadUnifed) {
        String sql = "insert into MGR_PRODUCT_HEAD_UNIFIED (ID,PRODUCT_ID,SELECT_ID,KEY,VALUE,IS_SHOW) values (?,?,?,?,?,?)";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < addOptionList.size(); i++){
            Option option = addOptionList.get(i);
            batchArgs.add(new Object[]{UUID.randomUUID().toString(),productHeadUnifed.getProduct_id(),productHeadUnifed.getId(),option.getKey(),option.getValue(),option.getIs_show()});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public void batchEdit(List<Option> addOptionList,ProductHeadUnifed productHeadUnifed) {
        String sql = "update  MGR_PRODUCT_HEAD_UNIFIED set KEY = ?,VALUE = ?,IS_SHOW = ? where ID = ?";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < addOptionList.size(); i++){
            Option option = addOptionList.get(i);
            batchArgs.add(new Object[]{option.getKey(),option.getValue(),option.getIs_show(),option.getId()});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public List<Map<String, Object>> getSelects(String product_id) {
        String qrySql = "SELECT *\n" +
                " FROM mgr_product_head_unified\n" +
                " WHERE (is_select = '1'\n" +
                "\t\tOR is_select = '2')\n" +
                "\tAND product_id = :product_id"+
                "  ORDER BY sort ";
        Map<String,Object> map = new HashMap<>();
        map.put("product_id",product_id);
        return pageHelper.getJdbcTempalte().queryForList(qrySql,map);
    }

    @Override
    public List<Map<String, Object>> getOptions(String product_id,String select_id) {
        String sql = "SELECT ID,KEY,VALUE,IS_SHOW \n" +
                "  FROM MGR_PRODUCT_HEAD_UNIFIED\n" +
                "  WHERE PRODUCT_ID = :product_id and select_id = :select_id \n" +
                "        AND is_select = '0' \n" +
                "  ORDER BY  sort";
        Map<String,Object> map = new HashMap<>();
        map.put("product_id",product_id);
        map.put("select_id",select_id);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }

    @Override
    public List<Map<String, Object>> getRepeatData(String select_id, List<String> keyList, List<String> editIdList) {
        String sql =" SELECT key,value \n" +
                "  FROM mgr_product_head_unified\n" +
                " WHERE select_id = :select_id \n" +
                "  AND key IN (:keyList) \n" +
                "  AND id not in (:editIdList)";
        Map<String,Object> map = new HashMap<>();
        map.put("select_id",select_id);
        map.put("keyList",String.join("','",keyList));
        map.put("editIdList",String.join("','",editIdList));
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }
}
