package com.piesat.busiclogic.busic.notice.dao;

import com.piesat.busiclogic.busic.notice.entity.AttachEntity;
import com.piesat.busiclogic.busic.notice.entity.NoticeEntity;
import com.piesat.jdbc.Page;

import java.util.List;
import java.util.Map;

public interface NoticeMgrDao {
    Page<NoticeEntity> getNoticeData(int currentPage, int pageSize, NoticeEntity noticeEntity);

    String addNotice(NoticeEntity noticeEntity);

    void deleteNotice(String ids);

    int editNotice(NoticeEntity noticeEntity);

    int editNoticeSingle(NoticeEntity noticeEntity);

    NoticeEntity getNoticeById(String id);

    void addAttach(List<AttachEntity> attachEntityList,String id);

    void batchAddAttach(List<AttachEntity> attachList,String id);

    void deleteAttach(String id);

    List<Map<String,Object>> getAttachList(String id);
}
