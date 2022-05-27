package com.piesat.quartz.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.quartz.entity.MosnJobLog;
import com.piesat.quartz.service.IMosnJobLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 定时任务调度日志信息 控制层
 */
@RestController
@RequestMapping("/api/jobLog")
public class MosnJobLogController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnJobLogController.class);

    @Autowired
    private IMosnJobLogService jobLogService;

    /**
     * 查询定时任务调度日志列表
     * @param pageNum
     * @param pageSize
     * @param mosnJobLog
     * @return
     */
    @GetMapping("/list")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, MosnJobLog mosnJobLog) {
        PageHelper.startPage(pageNum, pageSize, true);
        List<MosnJobLog> sysJobList = jobLogService.selectJobLogList(mosnJobLog);
        PageInfo<MosnJobLog> sysJobLogPageInfo = new PageInfo<>(sysJobList);
        return WrapMapper.ok(sysJobLogPageInfo);
    }

    /**
     * 根据调度编号获取详细信息
     * @param jobLogId
     * @return
     */
    @GetMapping(value = "/{configId}")
    public Wrapper getInfo(@PathVariable Long jobLogId) {
        return WrapMapper.ok(jobLogService.selectJobLogById(jobLogId));
    }

    /**
     * 删除定时任务调度日志
     * @param jobLogIds
     * @return
     */
    @DeleteMapping("/{jobLogIds}")
    public Wrapper remove(@PathVariable Long[] jobLogIds) {
        return WrapMapper.judge(jobLogService.deleteJobLogByIds(jobLogIds));
    }

    /**
     * 清空定时任务调度日志
     * @return
     */
    @DeleteMapping("/clean")
    public Wrapper clean() {
        jobLogService.cleanJobLog();
        return WrapMapper.ok();
    }
}
