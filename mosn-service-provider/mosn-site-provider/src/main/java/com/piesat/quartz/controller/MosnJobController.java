package com.piesat.quartz.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.exception.TaskException;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.CronUtils;
import com.piesat.quartz.entity.MosnJob;
import com.piesat.quartz.service.IMosnJobService;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务调度信息 控制层
 */
@RestController
@RequestMapping("/api/job")
public class MosnJobController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnJobController.class);

    @Autowired
    private IMosnJobService jobService;

    /**
     * 查询定时任务列表
     */
    @GetMapping("/jobs/{pageNum}/{pageSize}")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, MosnJob mosnJob) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<MosnJob> mosnJobList = jobService.selectJobList(mosnJob);
        PageInfo<MosnJob> sysJobPageInfo = new PageInfo<>(mosnJobList);
        return WrapMapper.ok(sysJobPageInfo);
    }

    /**
     * 获取定时任务详细信息
     */
    @GetMapping(value = "/{jobId}")
    public Wrapper getInfo(@PathVariable("jobId") Long jobId) {
        return WrapMapper.ok(jobService.selectJobById(jobId));
    }

    /**
     * 新增定时任务
     * @param job
     * @return
     * @throws SchedulerException
     * @throws TaskException
     */
    @PostMapping
    public Wrapper add(@RequestBody MosnJob job) throws SchedulerException, TaskException {
//        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        if (!CronUtils.isValid(job.getCronExpression())) {
            return WrapMapper.error("新增任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
//        job.setCreateBy(loginAuthDto.getUsername());
        return WrapMapper.ok(jobService.insertJob(job));
    }

    /**
     * 修改定时任务
     */
    @PutMapping
    public Wrapper edit(@RequestBody MosnJob job) throws SchedulerException, TaskException {
//        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        if (!CronUtils.isValid(job.getCronExpression())) {
            return WrapMapper.error("修改任务'" + job.getJobName() + "'失败，Cron表达式不正确");
        }
//        job.setUpdateBy(loginAuthDto.getUsername());
        return WrapMapper.ok(jobService.updateJob(job));
    }

    /**
     * 定时任务状态修改
     */
    @PutMapping("/changeStatus")
    public Wrapper changeStatus(@RequestBody MosnJob job) throws SchedulerException {
        MosnJob newJob = jobService.selectJobById(job.getJobId());
        newJob.setStatus(job.getStatus());
        return WrapMapper.ok(jobService.changeStatus(newJob));
    }

    /**
     * 定时任务立即执行一次
     */
    @PutMapping("/run")
    public Wrapper run(@RequestBody MosnJob job) throws SchedulerException {
        jobService.run(job);
        return WrapMapper.ok();
    }

    /**
     * 删除定时任务
     */
    @DeleteMapping("/{jobIds}")
    public Wrapper remove(@PathVariable Long[] jobIds) throws SchedulerException, TaskException {
        jobService.deleteJobByIds(jobIds);
        return WrapMapper.ok();
    }
}
