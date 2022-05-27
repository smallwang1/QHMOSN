package com.piesat.quartz.util;

import com.piesat.quartz.entity.MosnJob;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, MosnJob mosnJob) throws Exception {
        JobInvokeUtil.invokeMethod(mosnJob);
    }
}
