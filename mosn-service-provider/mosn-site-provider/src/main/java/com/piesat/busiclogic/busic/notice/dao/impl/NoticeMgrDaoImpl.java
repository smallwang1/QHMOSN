package com.piesat.busiclogic.busic.notice.dao.impl;

import com.piesat.busiclogic.busic.notice.dao.NoticeMgrDao;
import com.piesat.busiclogic.busic.notice.entity.AttachEntity;
import com.piesat.busiclogic.busic.notice.entity.NoticeEntity;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class NoticeMgrDaoImpl implements NoticeMgrDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Override
    public Page<NoticeEntity> getNoticeData(int currentPage, int pageSize, NoticeEntity noticeEntity) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" select * from ");
        sb.append("    NOTICEINFO");
        sb.append("    where 1 = 1 ");

        Map map = new HashMap();
        if(!PublicUtil.isEmpty(noticeEntity.getTitle())){
            sb.append(" and title like '%'+:title+'%' ");
            map.put("title",noticeEntity.getTitle());
        }
        if(!PublicUtil.isEmpty(noticeEntity.getIsfirstshow())){
            sb.append(" and ISFIRSTSHOW=:ISFIRSTSHOW");
            map.put("ISFIRSTSHOW",noticeEntity.getIsfirstshow());
        }
        if(!PublicUtil.isEmpty(noticeEntity.getType())){
            sb.append(" and TYPE=:TYPE");
            map.put("TYPE",noticeEntity.getType());
        }
        if(!PublicUtil.isEmpty(noticeEntity.getIspublish())){
            sb.append(" and ISPUBLISH=:ISPUBLISH");
            map.put("ISPUBLISH",noticeEntity.getIspublish());
        }
        sb.append(" ORDER BY ISTOP DESC,UPDATED DESC,CREATED DESC");
        return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(NoticeEntity.class),currentPage,pageSize);
    }


    @Override
    public String addNotice(NoticeEntity noticeEntity) {

        StringBuffer sb = new StringBuffer();
        sb.append(" insert into  NOTICEINFO");
        sb.append(" (");
        sb.append("ID,\n");
        sb.append("TITLE,\n");
        sb.append("TYPE,\n");
        sb.append("LINKURL,\n");
        sb.append("ISPUBLISH,\n");
        sb.append("ISFIRSTSHOW,\n");
        sb.append("PUBLISHDATE,\n");
        sb.append("PUBLISHER,\n");
        sb.append("CREATED,\n");
        sb.append("ISTOP,\n");
        sb.append("CONTENT\n");

        sb.append(" ) values (");

        sb.append(":ID,\n");
        sb.append(":TITLE,\n");
        sb.append(":TYPE,\n");
        sb.append(":LINKURL,\n");
        sb.append(":ISPUBLISH,\n");
        sb.append(":ISFIRSTSHOW,\n");
        sb.append(":PUBLISHDATE,\n");
        sb.append(":PUBLISHER,\n");
        sb.append(":CREATED,\n");
        sb.append(":ISTOP,\n");
        sb.append(":CONTENT)\n");


        Map map = new HashMap<>();
        map.put("ID",noticeEntity.getId() );
        map.put("CONTENT", noticeEntity.getContent());
        map.put("TITLE", noticeEntity.getTitle());
        map.put("TYPE",noticeEntity.getType());
        map.put("LINKURL",noticeEntity.getLinkurl());
        map.put("ISPUBLISH", noticeEntity.getIspublish());
        map.put("ISFIRSTSHOW",noticeEntity.getIsfirstshow());
        if("1".equals(noticeEntity.getIspublish())){
            map.put("PUBLISHDATE",System.currentTimeMillis());
            map.put("PUBLISHER","");
        }else{
            map.put("PUBLISHDATE","");
            map.put("PUBLISHER","");
        }
        map.put("CREATED", System.currentTimeMillis());
        map.put("ISTOP",noticeEntity.getIstop());


        long ids = pageHelper.getJdbcTempalte().update(sb.toString(),map);
        if(!PublicUtil.isEmpty(ids)){
            return  String.valueOf(ids);
        }else{
            return "";
        }
    }

    @Override
    public void deleteNotice(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        String sql = "delete from NOTICEINFO where id = ? ";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < idList.size(); i++){
            batchArgs.add(new Object[]{idList.get(i)});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public int editNotice(NoticeEntity noticeEntity) {
        StringBuffer sb = new StringBuffer(100);
        Map map = new HashMap();
        sb.append("update NOTICEINFO set");
        sb.append(" TITLE=:TITLE ,");
        sb.append(" TYPE=:TYPE,");
        sb.append(" LINKURL=:LINKURL,");
        sb.append(" CONTENT=:CONTENT ,");
        sb.append(" ISPUBLISH=:ISPUBLISH ,");
        sb.append(" ISFIRSTSHOW=:ISFIRSTSHOW,");
        sb.append(" INVALID=:INVALID,");
        sb.append(" PUBLISHDATE =:PUBLISHDATE,");
        sb.append(" PUBLISHER =:PUBLISHER,");
        sb.append(" ISTOP =:ISTOP");
        sb.append(" WHERE ID =:ID ");

        map.put("TITLE", noticeEntity.getTitle());
        map.put("TYPE", noticeEntity.getType());
        map.put("LINKURL", noticeEntity.getLinkurl());
        map.put("CONTENT", noticeEntity.getContent());
        map.put("ISPUBLISH", noticeEntity.getIspublish());
        map.put("ISFIRSTSHOW", noticeEntity.getIsfirstshow());
        map.put("INVALID",noticeEntity.getInvalid());
        map.put("PUBLISHDATE",System.currentTimeMillis());
        map.put("PUBLISHER",noticeEntity.getPublisher());
        map.put("ISTOP",noticeEntity.getIstop());
        map.put("ID",noticeEntity.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int editNoticeSingle(NoticeEntity noticeEntity) {
        StringBuffer sb = new StringBuffer(100);
        Map map = new HashMap();
        sb.append("update NOTICEINFO set");
        if(!PublicUtil.isEmpty(noticeEntity.getIspublish())){
            sb.append(" ISPUBLISH=:ISPUBLISH ,");
            sb.append(" PUBLISHDATE =:PUBLISHDATE,");
        }
        if(!PublicUtil.isEmpty(noticeEntity.getIstop())){
            sb.append(" ISTOP =:ISTOP,");
        }
        sb.append(" UPDATED =:UPDATED");
        sb.append(" WHERE ID =:ID ");

        map.put("ISPUBLISH", noticeEntity.getIspublish());
        map.put("ISTOP",noticeEntity.getIstop());
        map.put("ID",noticeEntity.getId());
        map.put("PUBLISHDATE",System.currentTimeMillis());
        map.put("UPDATED",System.currentTimeMillis());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public NoticeEntity getNoticeById(String id) {
        String sql =" SELECT ID, TITLE, TYPE, LINKURL, CONTENT\n" +
                "\t, ISPUBLISH, ISFIRSTSHOW, INVALID, PUBLISHDATE, CREATED\n" +
                "\t, ISTOP, PUBLISHER\n" +
                "  FROM NOTICEINFO\n" +
                "  WHERE id =:id";
        Map map = new HashMap();
        map.put("id", id);
        return  pageHelper.findSingleData(sql, map, new CommonMapper(NoticeEntity.class));
    }

    @Override
    public void addAttach(List<AttachEntity> attachEntityList,String id) {
        String sql = "insert into NOTICE_ATTACHINFO (ID,FILENAME,ADDRESS,NOTICEID) values (?,?,?,?)";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < attachEntityList.size(); i++){
            AttachEntity AttachEntity = attachEntityList.get(i);
            batchArgs.add(new Object[]{UUID.randomUUID(),AttachEntity.getFileName(),AttachEntity.getAddress(),id});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public void batchAddAttach(List<AttachEntity> attachList,String id) {
        String sql = "insert into notice_attachinfo (ID,FILENAME,ADDRESS,CREATE_TIME,NOTICEID) values (?,?,?,?,?)";
        List<Object[]> batchArgs=new ArrayList<Object[]>();
        for(int i = 0; i < attachList.size(); i++){
            AttachEntity attachEntity = attachList.get(i);
            batchArgs.add(new Object[]{UUID.randomUUID().toString(),attachEntity.getFileName(),attachEntity.getAddress(), System.currentTimeMillis(),id});
        }
        pageHelper.getJdbcTempalte().getJdbcTemplate().batchUpdate(sql,batchArgs);
    }

    @Override
    public void deleteAttach(String id) {
        String sql = "delete from NOTICE_ATTACHINFO where noticeid =:id";
        Map map  = new HashMap();
        map.put("id",id);
        pageHelper.getJdbcTempalte().update(sql,map);
    }

    @Override
    public List<Map<String,Object>> getAttachList(String id) {
        String sql = "select * from NOTICE_ATTACHINFO where NOTICEID=:NOTICEID";
        Map map  = new HashMap();
        map.put("NOTICEID",id);
        return pageHelper.getJdbcTempalte().queryForList(sql,map);
    }
}
