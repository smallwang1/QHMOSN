package com.piesat.quartz.service.impl;

import com.piesat.quartz.entity.MosnJobLog;
import com.piesat.quartz.mapper.MosnJobLogMapper;
import com.piesat.quartz.service.IMosnJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 定时任务调度日志信息 服务层
 */
@Service
public class MosnJobLogServiceImpl implements IMosnJobLogService {

    @Autowired
    private MosnJobLogMapper jobLogMapper;

    @Override
    public void addJobLog(MosnJobLog mosnJobLog) {
        jobLogMapper.insertJobLog(mosnJobLog);
    }

    @Override
    public List<MosnJobLog> selectJobLogList(MosnJobLog mosnJobLog) {
        return jobLogMapper.selectJobLogList(mosnJobLog);
    }

    @Override
    public MosnJobLog selectJobLogById(Long jobLogId) {
        return jobLogMapper.selectJobLogById(jobLogId);
    }

    @Override
    public int deleteJobLogById(Long jobLogId) {
        return jobLogMapper.deleteJobLogById(jobLogId);
    }

    @Override
    public int deleteJobLogByIds(Long[] jobLogIds) {
        return jobLogMapper.deleteJobLogByIds(jobLogIds);
    }

    @Override
    public void cleanJobLog() {
        jobLogMapper.cleanJobLog();
    }
}
