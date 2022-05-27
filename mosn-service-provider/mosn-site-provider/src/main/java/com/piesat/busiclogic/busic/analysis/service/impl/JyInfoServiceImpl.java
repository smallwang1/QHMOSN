/**
 * 项目名称:analysis
 * 类名称:JyInfoServiceImpl.java
 * 包名称:com.piesat.busiclogic.busic.analysis.service.impl
 *
 * 修改履历:
 *       日期                   修正者      主要内容
 *  Thu Apr 02 16:30:26 CST 2020       admin         初版做成
 *
 * Copyright (c) 2007-2018
 */

package com.piesat.busiclogic.busic.analysis.service.impl;

import com.piesat.busiclogic.common.Util.Misc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.piesat.jdbc.Page;

import com.piesat.busiclogic.busic.analysis.entity.JyInfo;
import com.piesat.busiclogic.busic.analysis.dao.IJyInfoDao;
import com.piesat.busiclogic.busic.analysis.service.IJyInfoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * JyInfo管理业务基本实现类<br>
 * 对JyInfo的增 删 改 查逻辑实现  <br>
 * @author admin
 * @version 1.0
 */
@Service
public class JyInfoServiceImpl implements IJyInfoService{

    @Value("${file.tempfile}")
    private String tempfile;

    // 文件内容读取jy和= 之间的内容
    public static final String contRegex = "JY(.*?)=";

    // 文件名中读取时间
    public static final String dateRegex = "-(.*?)/.TXT";

    /**
     * 注入要操作的DAO
     */
    @Autowired
    private IJyInfoDao iJyInfoDao;
    
    /*
     * (non-Javadoc)
     * @see
     * com.piesat.busiclogic.busic.analysis.service.IJyInfoService
     * insertJyInfo(com.piesat.busiclogic.busic.analysis.entity.JyInfo)
     */
    @Override
    public int[] batchInsertJyInfo(List<JyInfo> jyInfoList) {

        return iJyInfoDao.batchInsertJyInfo(jyInfoList);

    }

    /*
     * (non-Javadoc)
     * @see
     * com.piesat.busiclogic.busic.analysis.service.IJyInfoService
     * updateJyInfo(com.piesat.busiclogic.busic.analysis.entity.JyInfo)
     */
    @Override
    public int updateJyInfo(JyInfo jyInfo) {

        int i= iJyInfoDao.updateJyInfo(jyInfo);
        
        if(1!=i){
            throw new RuntimeException("更新JyInfo异常");
        }
        
        return i;
    }

    /*
     * (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysis.service.IJyInfoService
     * deleteJyInfo(java.lang.String)
     */
    @Override
    public int deleteJyInfo() {

        int i= iJyInfoDao.deleteJyInfo();
        
        if(1!=i){
            throw new RuntimeException("更新JyInfo异常");
        }
        
        return i;

    }

    /*
     * (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysis.service.IJyInfoService
     * findJyInfoById(java.lang.String)
     */
    @Override
    public JyInfo findJyInfoById() {

        return iJyInfoDao.findJyInfoById();
    }

    /*
     * (non-Javadoc)
     * @see com.piesat.busiclogic.busic.analysis.service.IJyInfoService
     * findPageJyInfo(long, int)
     */
    @Override
    public Page<JyInfo> findPageJyInfo(long currentPage, int pageSize,JyInfo jyInfo) {

        return iJyInfoDao.findPageJyInfo(currentPage, pageSize,jyInfo);
    }

    @Override
    public String fileAnalysis(String filename) {
        String str = Misc.readFileContent(tempfile+filename);
        String[] cotents = Misc.getSubUtil(str,contRegex).split("\\。");

        // 解析指定文件名称
        List<JyInfo> list = new ArrayList<>();
        for(String s:cotents){
            JyInfo jyInfo = new JyInfo();
            jyInfo.setId(String.valueOf(UUID.randomUUID()));
            jyInfo.setDate(Misc.getSubUtil(filename,dateRegex)+s.substring(3,5));
            jyInfo.setDescription(s.substring(6));
            jyInfo.setFilename(filename);
            jyInfo.setFiletype(s.substring(0,2));
            jyInfo.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            list.add(jyInfo);
        }
        int [] a = iJyInfoDao.batchInsertJyInfo(list);
        return String.valueOf(a.length);
    }
}
