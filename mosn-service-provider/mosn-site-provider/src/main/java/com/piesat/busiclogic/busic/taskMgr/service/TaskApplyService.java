package com.piesat.busiclogic.busic.taskMgr.service;

import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskApplyData;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskFile;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.jdbc.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface TaskApplyService {
    Page<TaskApplyData> getTodoList(long currentPage, int pageSize, TaskApplyData taskApplyData, LoginAuthDto loginAuthDto);

    Page<TaskApplyData> getDoneList(long currentPage, int pageSize, TaskApplyData taskApplyData,LoginAuthDto loginAuthDto);

    void addApplyData(TaskApplyData taskApplyData,LoginAuthDto loginAuthDto);

    void editApplyData(TaskApplyData taskApplyData, LoginAuthDto loginAuthDto);

    void submitTask(TaskFile taskFile,LoginAuthDto loginAuthDto);

    Map<String,String> upload (MultipartFile file);
}
