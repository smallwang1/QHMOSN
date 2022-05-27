package com.piesat.quartz.mapper;

import com.piesat.quartz.entity.MosnJobLog;

import java.util.List;

/**
 * 调度任务日志信息 数据层
 */
public interface MosnJobLogMapper {

    /**
     * 新增任务日志
     * @param mosnJobLog 调度日志信息
     * @return 结果
     */
    public int insertJobLog(MosnJobLog mosnJobLog);

    /**
     * 获取quartz调度器日志的计划任务
     * @param mosnJobLog 调度日志信息
     * @return 调度任务日志集合
     */
    public List<MosnJobLog> selectJobLogList(MosnJobLog mosnJobLog);

    /**
     * 查询所有调度任务日志
     * @return 调度任务日志列表
     */
    public List<MosnJobLog> selectJobLogAll();

    /**
     * 过调度任务日志ID查询调度信息
     * @param jobLogId 调度任务日志ID
     * @return 调度任务日志对象信息
     */
    public MosnJobLog selectJobLogById(Long jobLogId);

    /**
     * 删除任务日志
     * @param jobLogId 调度日志ID
     * @return 结果
     */
    public int deleteJobLogById(Long jobLogId);

    /**
     * 批量删除调度日志信息
     * @param jobLogIds 需要删除的数据IDs
     * @return 结果
     */
    public int deleteJobLogByIds(Long[] jobLogIds);

    /**
     * 清空任务日志
     */
    public void cleanJobLog();
}
