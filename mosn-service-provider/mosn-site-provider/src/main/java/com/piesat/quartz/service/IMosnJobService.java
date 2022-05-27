package com.piesat.quartz.service;

import com.piesat.common.core.exception.TaskException;
import com.piesat.quartz.entity.MosnJob;
import org.quartz.SchedulerException;

import java.util.List;

public interface IMosnJobService {

    /**
     * 查询定时任务列表
     * @param mosnJob
     * @return
     */
    public List<MosnJob> selectJobList(MosnJob mosnJob);

    /**
     * 通过调度任务ID查询调度信息
     * @param jobId 调度任务ID
     * @return
     */
    public MosnJob selectJobById(Long jobId);

    /**
     * 新增任务
     * @param mosnJob
     * @return
     */
    public int insertJob(MosnJob mosnJob) throws SchedulerException, TaskException;

    /**
     * 更新任务
     * @param mosnJob
     * @return
     */
    public int updateJob(MosnJob mosnJob) throws SchedulerException, TaskException;

    /**
     * 任务调度状态修改
     * @param newJob
     * @return
     */
    public int changeStatus(MosnJob newJob) throws SchedulerException;

    /**
     * 恢复任务
     * @param mosnJob
     * @return
     * @throws SchedulerException
     */
    public int resumeJob(MosnJob mosnJob) throws SchedulerException;

    /**
     * 暂停任务
     * @param mosnJob
     * @return
     * @throws SchedulerException
     */
    public int pauseJob(MosnJob mosnJob) throws SchedulerException;

    /**
     * 立即运行任务
     * @param mosnJob
     */
    public void run(MosnJob mosnJob) throws SchedulerException;

    /**
     * 批量删除调度信息
     * @param jobIds
     */
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException;

    /**
     * 删除任务后，所对应的trigger也将被删除
     * @param mosnJob
     * @return
     * @throws SchedulerException
     */
    public int deleteJob(MosnJob mosnJob) throws SchedulerException;
}
