package com.piesat.busiclogic.busic.notice.service.impl;

import com.piesat.busiclogic.busic.notice.dao.NoticeMgrDao;
import com.piesat.busiclogic.busic.notice.entity.AttachEntity;
import com.piesat.busiclogic.busic.notice.entity.NoticeEntity;
import com.piesat.busiclogic.busic.notice.service.NoticeMgrService;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.core.constant.GlobalConstant;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.Page;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class NoticeMgrServiceImpl implements NoticeMgrService {

    @Autowired
    private NoticeMgrDao noticeMgrDao;

    @Override
    public Page<NoticeEntity> getNoticeData(int currentPage, int pageSize, NoticeEntity noticeEntity) {
        Page<NoticeEntity>  page = noticeMgrDao.getNoticeData(currentPage,pageSize,noticeEntity);
        if(!PublicUtil.isEmpty(page.getResult())){
            for (int i = 0; i < page.getResult().size(); i++) {
                NoticeEntity noticeEntity1 = page.getResult().get(i);
                if(!PublicUtil.isEmpty(noticeEntity1.getPublishdate())){
                    noticeEntity1.setPublishdate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").
                            format(new Date(Long.parseLong(noticeEntity1.getPublishdate()) + 8*3600)));
                }
            }
        }
        return page;
    }

    @Override
    @Transactional
    public void addNotice(NoticeEntity noticeEntity) throws Exception{
        noticeEntity.setId(UUID.randomUUID().toString());
        String status = noticeMgrDao.addNotice(noticeEntity);
        if(!PublicUtil.isEmpty(noticeEntity.getAttachEntity())){
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<AttachEntity> AttachList = mapper.readValue(noticeEntity.getAttachEntity(),new TypeReference<List<AttachEntity>>() { });
            noticeMgrDao.batchAddAttach(AttachList,noticeEntity.getId());
        }
        if(PublicUtil.isEmpty(status)){
            throw new BusinessException("新增公告失败");
        }
    }

    @Override
    public void deleteNotice(String ids) {
        noticeMgrDao.deleteNotice(ids);
    }

    @Override
    @Transactional
    public void editNotice(NoticeEntity noticeEntity) throws Exception {
        if(PublicUtil.isEmpty(noticeEntity.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = noticeMgrDao.editNotice(noticeEntity);
        if(!PublicUtil.isEmpty(noticeEntity.getAttachEntity())){
            noticeMgrDao.deleteAttach(noticeEntity.getId());
            ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
            List<AttachEntity> AttachList = mapper.readValue(noticeEntity.getAttachEntity(),new TypeReference<List<AttachEntity>>() { });
            noticeMgrDao.batchAddAttach(AttachList,noticeEntity.getId());
        }
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public void editNoticeSingle(NoticeEntity noticeEntity) {
        if(PublicUtil.isEmpty(noticeEntity.getId())){
            throw new BusinessException("缺少参数ID");
        }
        int i = noticeMgrDao.editNoticeSingle(noticeEntity);
        if (i <= 0) {
            throw new BusinessException("操作失败");
        }
    }

    @Override
    public NoticeEntity getNoticeById(String id, HttpServletRequest request) throws Exception{
        NoticeEntity noticeEntity = noticeMgrDao.getNoticeById(id);
        String url = request.getHeader(GlobalConstant.Header.domain);
        if(!PublicUtil.isEmpty(url) && url.indexOf("ahamic.cn")!=-1) { // 外网请求公告url替换成域名
            String targetPath = Misc.getPropValue("transparent.properties", "url_inside");
            String repstr = Misc.getPropValue("transparent.properties", "url_outside");
            noticeEntity.setContent(noticeEntity.getContent().replace(targetPath, repstr));
        }else {
            String targetPath = Misc.getPropValue("transparent.properties", "url_inside");
            String repstr = Misc.getPropValue("transparent.properties", "url_inside_");
            noticeEntity.setContent(noticeEntity.getContent().replace(targetPath, repstr));
        }
        noticeEntity.setAttachEntityList(noticeMgrDao.getAttachList(id));
        return noticeEntity;
    }
}
