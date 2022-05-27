package com.piesat.quartz.util;

import com.piesat.common.core.constant.ScheduleConstants;
import com.piesat.quartz.entity.MosnJob;
import com.piesat.quartz.entity.MosnJobLog;
import com.piesat.quartz.service.IMosnJobLogService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public abstract class AbstractQuartzJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    /**
     * 线程本地变量
     */
    private static ThreadLocal<Date> threadLocal = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        MosnJob mosnJob = new MosnJob();
        BeanUtils.copyBeanProp(mosnJob, context.getMergedJobDataMap().get(ScheduleConstants.TASK_PROPERTIES));
        try {
            before(context, mosnJob);
            if (mosnJob != null) {
                doExecute(context, mosnJob);
            }
            after(context, mosnJob, null);
        } catch (Exception e) {
            log.error("任务执行异常  - ：", e);
            after(context, mosnJob, e);
        }
    }

    /**
     * 执行前
     *
     * @param context 工作执行上下文对象
     * @param mosnJob 系统计划任务
     */
    protected void before(JobExecutionContext context, MosnJob mosnJob)
    {
        threadLocal.set(new Date());
    }

    /**
     * 执行后
     *
     * @param context 工作执行上下文对象
     * @param mosnJob 系统计划任务
     */
    protected void after(JobExecutionContext context, MosnJob mosnJob, Exception e) {
        Date startTime = threadLocal.get();
        threadLocal.remove();

        final MosnJobLog mosnJobLog = new MosnJobLog();
        mosnJobLog.setJobName(mosnJob.getJobName());
        mosnJobLog.setJobGroup(mosnJob.getJobGroup());
        mosnJobLog.setInvokeTarget(mosnJob.getInvokeTarget());
        mosnJobLog.setStartTime(startTime);
        mosnJobLog.setStopTime(new Date());
        long runMs = mosnJobLog.getStopTime().getTime() - mosnJobLog.getStartTime().getTime();
        mosnJobLog.setJobMessage(mosnJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            mosnJobLog.setStatus("1");
            String errorMsg = StringUtils.substring(ExceptionUtil.getExceptionMessage(e), 0, 2000);
            mosnJobLog.setExceptionInfo(errorMsg);
        } else {
            mosnJobLog.setStatus("0");
        }

        // 写入数据库当中
        SpringUtils.getBean(IMosnJobLogService.class).addJobLog(mosnJobLog);
    }

    /**
     * 执行方法，由子类重载
     *
     * @param context 工作执行上下文对象
     * @param mosnJob 系统计划任务
     * @throws Exception 执行过程中的异常
     */
    protected abstract void doExecute(JobExecutionContext context, MosnJob mosnJob) throws Exception;
}
