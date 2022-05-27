package com.piesat.quartz.util;

import com.piesat.quartz.entity.MosnJob;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, MosnJob mosnJob) throws Exception {
        JobInvokeUtil.invokeMethod(mosnJob);
    }
}
