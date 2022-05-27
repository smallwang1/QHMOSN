package com.piesat.busiclogic.busic.taskMgr.dao;

import com.piesat.busiclogic.busic.taskMgr.entity.CheckInfo;
import com.piesat.busiclogic.busic.taskMgr.entity.DataSetInfo;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskApplyData;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskFile;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.jdbc.Page;

import java.util.List;


public interface TaskApplyDao {

    /**
     * 代办任务列表
     * @param currentPage
     * @param pageSize
     * @param taskApplyData
     * @param loginAuthDto
     * @return
     */
    Page<TaskApplyData> getTaskApplyDataTodo(long currentPage, int pageSize, TaskApplyData taskApplyData, LoginAuthDto loginAuthDto);


    /**
     * 已办列表
     * @param currentPage
     * @param pageSize
     * @param taskApplyData
     * @param loginAuthDto
     * @return
     */
    Page<TaskApplyData> getTaskApplyDataDone(long currentPage, int pageSize, TaskApplyData taskApplyData, LoginAuthDto loginAuthDto);


    /**
     * 数据集信息
     * @param taskid
     * @return
     */
    List<DataSetInfo> getDataSetList(String taskid);

    /**
     * 审核信息
     * @param taskid
     * @return
     */
    List<CheckInfo> getCheckInfo(String taskid);

    /**
     * 附件信息
     * @param taskid
     * @return
     */
    List<TaskFile> getTaskFile(String taskid);


    int addDataSetInfo(DataSetInfo dataSetInfo);

    int addTaskApplyInfo(TaskApplyData taskApplyData,LoginAuthDto loginAuthDto);


    int editDateSetInfo(DataSetInfo dataSetInfo);

    int editTaskApplyInfo(TaskApplyData taskApplyData,LoginAuthDto loginAuthDto);

    int addFileInfo(TaskFile taskFile);

    int updateTaskStatus(String taskid,String status);

    int addCheckInfo(CheckInfo checkInfo);


}
