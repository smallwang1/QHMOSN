package com.piesat.busiclogic.busic.taskMgr.controller;

import com.piesat.busiclogic.busic.taskMgr.entity.TaskApplyData;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskFile;
import com.piesat.busiclogic.busic.taskMgr.service.TaskApplyService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/taskApply")
public class TaskApplyController extends BaseController {

    @Autowired
    private TaskApplyService taskApplyService;

    @Description("查询待办任务列表")
    @RequestMapping(value = "/getTodoList", method = RequestMethod.GET)
    public Wrapper getTodoList(@RequestParam long currentPage, @RequestParam int pageSize, TaskApplyData taskApplyData) {
        return this.handleResult(taskApplyService.getTodoList(currentPage, pageSize,taskApplyData,null));
    }

    @Description("查询已办任务列表")
    @RequestMapping(value = "/getDoneList", method = RequestMethod.GET)
    public Wrapper getDoneList(@RequestParam long currentPage, @RequestParam int pageSize, TaskApplyData taskApplyData) {
        return this.handleResult(taskApplyService.getDoneList(currentPage, pageSize,taskApplyData,null));
    }

    @Description("发起任务")
    @RequestMapping(value = "/addApplyTask", method = RequestMethod.POST)
    public Wrapper addApplyData(TaskApplyData taskApplyData) {
        taskApplyService.addApplyData(taskApplyData,null);
        return WrapMapper.ok();
    }

    @Description("编辑任务")
    @RequestMapping(value = "/editApplyTask", method = RequestMethod.POST)
    public Wrapper editApplyData(TaskApplyData taskApplyData) {
        taskApplyService.editApplyData(taskApplyData,null);
        return WrapMapper.ok();
    }


    @Description("提交任务")
    @RequestMapping(value = "/submitTask", method = RequestMethod.POST)
    public Wrapper submitTask(TaskFile taskFile) {
        taskApplyService.submitTask(taskFile,null);
        return WrapMapper.ok();
    }

    /**
     *@desc 申请资料文件上传
     *@params
     *@return
     */
    @CrossOrigin
    @RequestMapping(value = "/upload",produces = {"application/json;charset=UTF-8"})
    public Map<String,String> upload(@RequestParam("file") MultipartFile file){
        return taskApplyService.upload(file);
    }
}
