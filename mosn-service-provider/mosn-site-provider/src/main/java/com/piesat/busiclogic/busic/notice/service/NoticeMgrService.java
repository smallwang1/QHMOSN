package com.piesat.busiclogic.busic.notice.service;

import com.piesat.busiclogic.busic.notice.entity.NoticeEntity;
import com.piesat.jdbc.Page;

import javax.servlet.http.HttpServletRequest;

public interface NoticeMgrService {
    Page<NoticeEntity> getNoticeData(int currentPage, int pageSize, NoticeEntity noticeEntity);

    void addNotice(NoticeEntity noticeEntity)throws Exception;

    void deleteNotice(String ids);

    void editNotice(NoticeEntity noticeEntity)throws Exception;

    void editNoticeSingle(NoticeEntity noticeEntity);

    NoticeEntity getNoticeById(String id, HttpServletRequest request) throws Exception;
}
