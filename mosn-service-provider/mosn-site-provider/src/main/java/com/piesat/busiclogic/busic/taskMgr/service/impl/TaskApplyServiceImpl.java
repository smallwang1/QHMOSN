package com.piesat.busiclogic.busic.taskMgr.service.impl;

import com.piesat.busiclogic.busic.taskMgr.dao.TaskApplyDao;
import com.piesat.busiclogic.busic.taskMgr.entity.CheckInfo;
import com.piesat.busiclogic.busic.taskMgr.entity.DataSetInfo;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskApplyData;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskFile;
import com.piesat.busiclogic.busic.taskMgr.service.TaskApplyService;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TaskApplyServiceImpl implements TaskApplyService {

    @Autowired
    private TaskApplyDao  taskApplyDao;

    @Override
    public Page<TaskApplyData> getTodoList(long currentPage, int pageSize, TaskApplyData taskApplyData, LoginAuthDto loginAuthDto) {

        Page<TaskApplyData> taskApplyDataPage = taskApplyDao.getTaskApplyDataTodo(currentPage,pageSize,taskApplyData,loginAuthDto);
        // 添加资料信息 和审核信息
        for( int i = 0 ; i < taskApplyDataPage.getResult().size(); i++){
            taskApplyDataPage.getResult().get(i).setDataSetInfoList(taskApplyDao.getDataSetList(taskApplyDataPage.getResult().get(i).getId()));
            taskApplyDataPage.getResult().get(i).setCheckInfokList(taskApplyDao.getCheckInfo(taskApplyDataPage.getResult().get(i).getId()));
            taskApplyDataPage.getResult().get(i).setTaskFileList(taskApplyDao.getTaskFile(taskApplyDataPage.getResult().get(i).getId()));
        }
        return taskApplyDataPage;
    }

    @Override
    public Page<TaskApplyData> getDoneList(long currentPage, int pageSize, TaskApplyData taskApplyData,LoginAuthDto loginAuthDto) {
        Page<TaskApplyData>  taskApplyDataPage = taskApplyDao.getTaskApplyDataDone(currentPage,pageSize,taskApplyData,loginAuthDto);
        // 添加资料信息 和审核信息
        for( int i = 0 ; i < taskApplyDataPage.getResult().size(); i++){
            taskApplyDataPage.getResult().get(i).setDataSetInfoList(taskApplyDao.getDataSetList(taskApplyDataPage.getResult().get(i).getId()));
            taskApplyDataPage.getResult().get(i).setCheckInfokList(taskApplyDao.getCheckInfo(taskApplyDataPage.getResult().get(i).getId()));
            taskApplyDataPage.getResult().get(i).setTaskFileList(taskApplyDao.getTaskFile(taskApplyDataPage.getResult().get(i).getId()));
        }
        return taskApplyDataPage;
    }

    @Override
    @Transactional
    public void addApplyData(TaskApplyData taskApplyData,LoginAuthDto loginAuthDto) {
        taskApplyData.setId(String.valueOf(UUID.randomUUID()));
        taskApplyDao.addTaskApplyInfo(taskApplyData,loginAuthDto);
        for(DataSetInfo dataSetInfo:taskApplyData.getDataSetInfoList()){
            dataSetInfo.setApplyid(taskApplyData.getId());
            taskApplyDao.addDataSetInfo(dataSetInfo);
        }
        for(TaskFile taskFile:taskApplyData.getTaskFileList()){
            taskFile.setTask_id(taskApplyData.getId());
            taskApplyDao.addFileInfo(taskFile);
        }
    }

    @Override
    @Transactional
    public void editApplyData(TaskApplyData taskApplyData, LoginAuthDto loginAuthDto) {
        taskApplyDao.editTaskApplyInfo(taskApplyData,loginAuthDto);
        for(DataSetInfo dataSetInfo:taskApplyData.getDataSetInfoList()){//资料信息更新
            if(PublicUtil.isEmpty(dataSetInfo.getId())){
                dataSetInfo.setApplyid(taskApplyData.getId());
                taskApplyDao.addDataSetInfo(dataSetInfo);
            }else{
                taskApplyDao.editDateSetInfo(dataSetInfo);
            }

        }
        for(TaskFile taskFile:taskApplyData.getTaskFileList()){
            if(PublicUtil.isEmpty(taskFile.getFile_id())){
                taskFile.setTask_id(taskApplyData.getId());
                taskApplyDao.addFileInfo(taskFile);
            }
        }
    }

    @Override
    @Transactional
    public void submitTask(TaskFile taskFile,LoginAuthDto loginAuthDto) {
        if(!PublicUtil.isEmpty(taskFile.getFile_name())){
            taskApplyDao.addFileInfo(taskFile);
        }
        CheckInfo checkInfo = new CheckInfo();
        checkInfo.setComments(taskFile.getComments());
        checkInfo.setStatus(taskFile.getStatus());
        checkInfo.setUserid(String.valueOf("2405"));
        taskApplyDao.addCheckInfo(checkInfo);
        taskApplyDao.updateTaskStatus(taskFile.getTask_id(),taskFile.getTask_status());
    }

    @Override
    public  Map<String,String> upload(MultipartFile file){
        Map<String,String> map = new HashMap<>();
        if(file.isEmpty()){
            map.put("msg","file is lost");
        }
        String filename = file.getOriginalFilename();
        String indexName = filename.substring(filename.lastIndexOf("."));
        String saveName = System.currentTimeMillis()+indexName;

        // 文件默认存储项目同级目录applydata目录下
        String path = System.getProperty("user.dir");
        File filepath = new File(path+"/applydata");

        if(!filepath.exists() && !filepath.isDirectory()){
            filepath.mkdir();
        }

        File desfile = new File(filepath.getAbsolutePath()+'/'+saveName);
        // 存储文件名
        // 新上传文件名称添加当前时间戳
        try {
            file.transferTo(desfile);
            map.put("msg","sucess");
            map.put("real_name",filename);
            map.put("file_name",saveName);
            map.put("path",filepath.getAbsolutePath());
            return map;
        } catch (IOException e) {
            map.put("msg","failure");
            e.printStackTrace();
        }
        return map;
    }
}
