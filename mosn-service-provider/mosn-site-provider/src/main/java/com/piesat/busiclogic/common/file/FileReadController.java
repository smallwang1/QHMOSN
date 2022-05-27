package com.piesat.busiclogic.common.file;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.anno.Description;
import com.piesat.common.config.properties.MdesCloudProperties;
import com.piesat.common.core.constant.GlobalConstant;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.util.CommonUtil;
import com.piesat.common.util.PublicUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

/**
 * 对各类文件流读取到前台综合类
 */
@Controller
@Slf4j
public class FileReadController extends BaseController {


    /**
     * 读取文件以流的形式到前端
     */
    @RequestMapping("/readFile")
    public void getFile(HttpServletRequest request, HttpServletResponse response) {
        String filePath = request.getParameter("location");
        // 获取文件后缀名称、
        String endstr = filePath.substring(filePath.lastIndexOf("."));
        response.setContentType(Misc.getContentype(endstr));
        String name = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        response.setHeader("Content-disposition ", "attachment; filename=" + name);
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
        } catch (IOException e) {
            log.error("error msg", e);
        }
    }

    /**
     * 读取文件以流的形式到前端
     */
    @RequestMapping(value = "/noAuth/source/getfile/{location}")
    public void getFile2(@PathVariable("location") String location, HttpServletResponse response) {
        String path = "";
        try {
            path = URLDecoder.decode(new String(Base64.decodeBase64(location), "UTF-8"), "UTF-8");
            // 获取文件后缀名称、
            String endstr = path.substring(path.lastIndexOf("."));
            response.setContentType(Misc.getContentype(endstr));
            File file = new File(path);
            String filename = file.getName();
            String staff = filename.substring(filename.lastIndexOf("."));
            response.addHeader("Content-Type", "text/html; charset=utf-8");
            response.setHeader("Content-disposition ", "attachment; filename=" + System.currentTimeMillis() + staff);
            FileCopyUtils.copy(new FileInputStream(path), response.getOutputStream());
        } catch (IOException e) {
            log.error("error msg", e);
        }
    }


    @ResponseBody
    @CrossOrigin
    @Description("文件上传")
    @RequestMapping(value = "/noAuth/file/upload", produces = {"application/json;charset=UTF-8"})
    public Map<String, String> upload(@RequestParam("file") MultipartFile file, @RequestParam String filepath) {
        try {
            filepath = new String(Base64.decodeBase64(filepath), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return FileManager.upload(file, filepath);
    }

    @Autowired
    private MdesCloudProperties mdesCloudProperties;

    @CrossOrigin
    @RequestMapping(value = "/provider/api/callBack")
    public void testRed(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String header = request.getHeader(GlobalConstant.Header.DEF_HEADER_ORIGIN);
        String innerside = mdesCloudProperties.getDomains().get(GlobalConstant.Header.innerside);
        if (PublicUtil.isEmpty(header) || header.equals(innerside)) {
            response.sendRedirect(CommonUtil.getCallBackUrl("http://piesat.domains/site/#/index", request));
        } else {
            response.sendRedirect(CommonUtil.getCallBackUrl("http://piesat.domains/#/index", request));
        }
    }
}
