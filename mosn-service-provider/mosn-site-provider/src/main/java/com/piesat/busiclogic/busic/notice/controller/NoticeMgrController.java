package com.piesat.busiclogic.busic.notice.controller;

import com.piesat.busiclogic.busic.notice.entity.NoticeEntity;
import com.piesat.busiclogic.busic.notice.service.NoticeMgrService;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.common.file.FileManager;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;

@RestController
@CrossOrigin
@Validated
@Slf4j
@RequestMapping("/noticeMgr")
public class NoticeMgrController extends BaseController {

    @Autowired
    private NoticeMgrService noticeMgrService;

    @Description("公告列表信息查询")
    @RequestMapping(value = "/getNoticeData", method = RequestMethod.GET)
    public Wrapper getNoticeData(@RequestParam int currentPage, @RequestParam int pageSize,NoticeEntity noticeEntity) {
        return this.handleResult(noticeMgrService.getNoticeData(currentPage,pageSize,noticeEntity));
    }

    @Description("增加公告信息")
    @RequestMapping(value = "/noSign/addNotice", method = RequestMethod.POST)
    public Wrapper addNotice(NoticeEntity noticeEntity) {
        try {
            noticeMgrService.addNotice(noticeEntity);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("删除公告信息")
    @RequestMapping(value = "/deleteNotice", method = RequestMethod.POST)
    public Wrapper deleteNotice(@NotEmpty(message = "ids 不能为空") String ids) {
        noticeMgrService.deleteNotice(ids);
        return WrapMapper.ok();
    }

    @Description("编辑公告信息")
    @RequestMapping(value = "/editNotice", method = RequestMethod.POST)
    public Wrapper editNotice(NoticeEntity noticeEntity) {
        try {
            noticeMgrService.editNotice(noticeEntity);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("单字段编辑公告信息")
    @RequestMapping(value = "/editNoticeSingle", method = RequestMethod.POST)
    public Wrapper editNoticeSingle(NoticeEntity noticeEntity) {
        noticeMgrService.editNoticeSingle(noticeEntity);
        return WrapMapper.ok();
    }

    @Description("由ID查公告信息")
    @RequestMapping(value = "/getNoticeById", method = RequestMethod.GET)
    public Wrapper getNoticeById(@NotEmpty(message = "id 不能为空") String id, HttpServletRequest request) {
        try {
            return this.handleResult( noticeMgrService.getNoticeById(id,request));
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.wrap(Wrapper.ERROR_CODE, "操作失败",null);
        }
    }

    @ResponseBody
    @CrossOrigin
    @Description("文件上传、以时间戳形式存储文件名称")
    @RequestMapping(value = "/noSign/upload",produces = {"application/json;charset=UTF-8"})
    public Map<String,String> uploadByTimeStrame(@RequestParam("file") MultipartFile file){
        try{
          return   FileManager.uploadByTimeStrame(file,Misc.getPropValue("transparent.properties","notice_path"));
        } catch (Exception e){
            return null;
        }
    }

    @Description("下载、公告文件")
    @RequestMapping(value = "/downLoad")
    public void downLoadFile( @RequestParam String address,@RequestParam String fileName,HttpServletResponse response)  {
        String path="";
        try {
            path = URLDecoder.decode(new String(Base64.decodeBase64(address), "UTF-8"),"UTF-8");
            // 获取文件后缀名称、
            String endstr = path.substring(path.lastIndexOf("."));
            response.setContentType(Misc.getContentype(endstr));
            File file = new File(path);
            String name = file.getName();
            response.addHeader("Content-Type", "text/html; charset=utf-8");
            response.setHeader("Content-disposition ", "attachment; filename="+fileName);
            FileCopyUtils.copy(new FileInputStream(path), response.getOutputStream());
        } catch (IOException e) {
            logger.error("error msg",  e);
        }
    }
}
