package com.piesat.busiclogic.common.file;

import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Descripton 文件操作工具
 **/
public class FileManager {

    /**
     * @param file 文件信息
     * @param filepath 文件上传路径
     * @return
     */
    public static Map<String,String> upload( MultipartFile file, String filepath){
        Map<String,String> map = new HashMap<>();
        String filename = file.getOriginalFilename();
        String stuffix = filename.substring(filename.lastIndexOf("."));
        // 新上传文件名称添加当前时间戳
        File desfile = new File(filepath + UUID.randomUUID() +"_"+stuffix);
        try {
            file.transferTo(desfile);
            map.put("msg","sucess");
            map.put("filename",filename);
            map.put("fileurl",desfile.getAbsolutePath());
            return map;
        } catch (IOException e) {
            map.put("msg","failure");
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 文件上传以时间戳形式存储文件名称
     * @param file 文件信息
     * @param filepath 文件上传路径
     * @return
     *
     */
    public static Map<String,String> uploadByTimeStrame( MultipartFile file, String filepath){
        Map<String,String> map = new HashMap<>();
        String originalFilename = file.getOriginalFilename();
        String stuffix = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
        String fileName =  UUID.randomUUID() +"."+stuffix;
        // 新上传文件名称添加当前时间戳
        File desfile = new File(filepath + fileName );
        try {
            file.transferTo(desfile);
            map.put("msg","sucess");
            map.put("originalFilename",originalFilename);
            map.put("fileName",fileName);
            map.put("fileurl",desfile.getAbsolutePath());
            return map;
        } catch (IOException e) {
            map.put("msg","failure");
            e.printStackTrace();
        }
        return map;
    }
}
