package com.piesat.quartz.service.impl;

import com.piesat.common.core.constant.ScheduleConstants;
import com.piesat.common.core.exception.TaskException;
import com.piesat.quartz.entity.MosnJob;
import com.piesat.quartz.mapper.MosnJobMapper;
import com.piesat.quartz.service.IMosnJobService;
import com.piesat.quartz.util.ScheduleUtils;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 定时任务调度信息 服务层
 */
@Service
public class MosnJobServiceImpl implements IMosnJobService {

    @Autowired
    private Scheduler scheduler;
    @Autowired
    private MosnJobMapper jobMapper;

    /**
     * 项目启动时，初始化定时器 主要是防止手动修改数据库导致未同步到定时任务处理（注：不能手动修改数据库ID和任务组名，否则会导致脏数据）
     * @throws SchedulerException
     * @throws TaskException
     */
    @PostConstruct
    public void init() throws SchedulerException, TaskException {
        scheduler.clear();
        List<MosnJob> jobList = jobMapper.selectJobAll();
        for (MosnJob job : jobList) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
    }

    @Override
    public List<MosnJob> selectJobList(MosnJob mosnJob) {
        return jobMapper.selectJobList(mosnJob);
    }

    @Override
    public MosnJob selectJobById(Long jobId) {
        return jobMapper.selectJobById(jobId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertJob(MosnJob job) throws SchedulerException, TaskException {
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.insertJob(job);
        if (rows > 0) {
            ScheduleUtils.createScheduleJob(scheduler, job);
        }
        return rows;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateJob(MosnJob job) throws SchedulerException, TaskException {
        MosnJob properties = selectJobById(job.getJobId());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            updateSchedulerJob(job, properties.getJobGroup());
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int changeStatus(MosnJob newJob) throws SchedulerException {
        int rows = 0;
        String status = newJob.getStatus();
        if (ScheduleConstants.Status.NORMAL.getValue().equals(status)) {
            rows = resumeJob(newJob);
        } else if (ScheduleConstants.Status.PAUSE.getValue().equals(status)) {
            rows = pauseJob(newJob);
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int resumeJob(MosnJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.NORMAL.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pauseJob(MosnJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        job.setStatus(ScheduleConstants.Status.PAUSE.getValue());
        int rows = jobMapper.updateJob(job);
        if (rows > 0) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void run(MosnJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        MosnJob properties = selectJobById(job.getJobId());
        // 参数
        JobDataMap dataMap = new JobDataMap();
        dataMap.put(ScheduleConstants.TASK_PROPERTIES, properties);
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, jobGroup), dataMap);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobByIds(Long[] jobIds) throws SchedulerException {
        for (Long jobId : jobIds) {
            MosnJob job = jobMapper.selectJobById(jobId);
            deleteJob(job);
        }
    }

    @Override
    public int deleteJob(MosnJob job) throws SchedulerException {
        Long jobId = job.getJobId();
        String jobGroup = job.getJobGroup();
        int rows = jobMapper.deleteJobById(jobId);
        if (rows > 0) {
            scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
        return rows;
    }

    /**
     * 更新任务
     * @param job
     * @param jobGroup
     * @throws SchedulerException
     * @throws TaskException
     */
    public void updateSchedulerJob(MosnJob job, String jobGroup) throws SchedulerException, TaskException {
        Long jobId = job.getJobId();
        // 判断是否存在
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            // 防止创建时存在数据问题 先移除，然后在执行创建操作
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }
}
