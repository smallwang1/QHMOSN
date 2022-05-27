/**
 * 项目名称:analysis
 * 类名称:IJyInfoService.java
 * 包名称:com.piesat.busiclogic.busic.analysis.service
 *
 * 修改履历:
 *       日期                   修正者      主要内容
 *  Thu Apr 02 16:30:26 CST 2020       admin         初版做成
 *
 * Copyright (c) 2007-2018
 */

package com.piesat.busiclogic.busic.analysis.service;


import com.piesat.jdbc.Page;
import com.piesat.busiclogic.busic.analysis.entity.JyInfo;

import java.util.List;

/**
 * JyInfo管理服务层接口<br>
 * 提供JyInfo的增、删、改、查服务接口   <br>
 * @author admin
 * @version 1.0
 */
public interface IJyInfoService {
    /**
     * 
     * 主要功能: 批量新增          <br>
     * 注意事项:无           <br>
     * 
     * @param jyInfoList jyInfo实体
     * @return   成功返回1，否则抛出异常
     */
    int[]  batchInsertJyInfo(List<JyInfo> jyInfoList);

    /**
     * 
     * 主要功能: 根据更新记录            <br>
     * 注意事项: 不支持更新 字段本身       <br>
     * 
     * @param jyInfo jyInfo实体
     * @return 成功返回1，否则返回0，如果有锁定，会抛出异常
     */
    int updateJyInfo(JyInfo jyInfo);

    /**
     * 
     * 主要功能: 按 删除JyInfo           <br>
     * 注意事项: 记录锁定时会抛出异常         <br>
     * @return   删除成功返回1（根据主键删除，最多删除1条记录），否则返回0
     */
    int deleteJyInfo();

    /**
     * 
     * 主要功能: 根据 查询JyInfo           <br>
     * 注意事项:       <br>
     * @return  JyInfo
     */
    JyInfo findJyInfoById();

    /**
     * 
     * 主要功能: 分页查询系统示例           <br>
     * 注意事项: 未添加查询条件            <br>
     * 
     * @param currentPage  开始页
     * @param pageSize  每页记录数
     * @param jyInfo jyInfo实体
     * @return   Page<JyInfo>
     */
    Page<JyInfo> findPageJyInfo(long currentPage, int pageSize,JyInfo jyInfo);


    /**
     * 更新
     * @param filename
     * @return
     */
    String fileAnalysis(String filename);
}

