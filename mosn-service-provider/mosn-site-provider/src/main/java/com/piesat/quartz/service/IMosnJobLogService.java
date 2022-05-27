package com.piesat.quartz.service;

import com.piesat.quartz.entity.MosnJobLog;

import java.util.List;

public interface IMosnJobLogService {

    /**
     * 新增任务日志
     * @param mosnJobLog 调度日志信息
     */
    public void addJobLog(MosnJobLog mosnJobLog);

    /**
     * 获取quartz调度器日志的计划任务
     * @param mosnJobLog 调度日志信息
     * @return 调度任务日志集合
     */
    public List<MosnJobLog> selectJobLogList(MosnJobLog mosnJobLog);

    /**
     * 通过调度任务日志ID查询调度信息
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    public MosnJobLog selectJobLogById(Long jobLogId);

    /**
     * 删除任务日志
     * @param jobLogId
     * @return
     */
    public int deleteJobLogById(Long jobLogId);

    /**
     * 批量删除调度日志信息
     * @param jobLogIds 需要删除的日志ID
     * @return
     */
    public int deleteJobLogByIds(Long[] jobLogIds);

    /**
     * 清空任务日志
     */
    public void cleanJobLog();
}
