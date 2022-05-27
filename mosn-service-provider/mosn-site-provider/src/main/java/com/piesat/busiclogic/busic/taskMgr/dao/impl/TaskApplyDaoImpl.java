package com.piesat.busiclogic.busic.taskMgr.dao.impl;

import com.piesat.busiclogic.busic.roleMgr.dao.RoleMgrDao;
import com.piesat.busiclogic.busic.taskMgr.dao.TaskApplyDao;
import com.piesat.busiclogic.busic.taskMgr.entity.CheckInfo;
import com.piesat.busiclogic.busic.taskMgr.entity.DataSetInfo;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskApplyData;
import com.piesat.busiclogic.busic.taskMgr.entity.TaskFile;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.util.PublicUtil;
import com.piesat.jdbc.JdbcPageHelper;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.mapper.CommonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.*;

@Repository
public class TaskApplyDaoImpl implements TaskApplyDao {

    @Autowired
    private JdbcPageHelper pageHelper;

    @Autowired
    private RoleMgrDao roleMgrDao;


    @Override
    public Page<TaskApplyData> getTaskApplyDataTodo(long currentPage, int pageSize, TaskApplyData taskApplyData, LoginAuthDto loginAuthDto) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mgr_task_dataapply.* ");
        sb.append(" ,sys_user.USERNAME ");
        sb.append("  from mgr_task_dataapply");
        sb.append(" left join sys_user on mgr_task_dataapply.userid = sys_user.id");
        sb.append("    where 1 = 1 ");
        Map<String,Object> map = new HashMap<>();

        List<String> CodeList =  roleMgrDao.getRoleIdByUserid(String.valueOf("2405"));

        if(PublicUtil.isEmpty(CodeList)){
            return null;
        }
        // 由用户的角色做过滤处理
        if(CodeList.contains("nomal")){// 普通用户
            sb.append(" and (( status = '0' or status = '2' ) and userid =:userid )");
            map.put("userid","2405");
        }
        if(CodeList.contains("inchecker")){ // 信息中心审核员
            sb.append(" or status = '1'");
        }
        if(CodeList.contains("gwcchecker")){
            sb.append(" or status = '3'");
        }
        return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(TaskApplyData.class),currentPage,pageSize);
    }

    @Override
    public Page<TaskApplyData> getTaskApplyDataDone(long currentPage, int pageSize, TaskApplyData taskApplyData, LoginAuthDto loginAuthDto) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select mgr_task_dataapply.* ");
        sb.append(" ,sys_user.USERNAME ");
        sb.append("  from mgr_task_dataapply");
        sb.append(" left join sys_user on mgr_task_dataapply.userid = sys_user.id");
        sb.append("    where 1 = 1 ");
        Map<String,Object> map = new HashMap<>();

        List<String> CodeList =  roleMgrDao.getRoleIdByUserid(java.lang.String.valueOf("2405"));
        if(PublicUtil.isEmpty(CodeList)){
            return null;
        }
        // 由用户的角色进行过滤
        if(CodeList.contains("nomal")){ //普通用户角色
            sb.append(" and  (status != '0' and userid=:userid )");
            map.put("userid","2405");
        }
        if(CodeList.contains("inchecker")){ //信息中心审核员
            sb.append(" or ( status != '0' and status !='1' ) ");
        }

        if(CodeList.contains("gwcchecker")){  //观测中心审核员
             sb.append(" or (status != '0' and status ! = '1' and status !='2' and status! = '3')");
        }

       return pageHelper.pagingFind(sb.toString(),map,new CommonMapper(TaskApplyData.class),currentPage,pageSize);

    }

    @Override
    public List<DataSetInfo> getDataSetList(String taskid) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from MGR_TASK_APPLYINFO where applyid =: applyid");
        Map<String,Object> map = new HashMap<>();
        map.put("applyid",taskid);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map,DataSetInfo.class);
    }

    @Override
    public List<CheckInfo> getCheckInfo(String taskid) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from MGR_TASK_CHECK where taskid =: taskid");
        Map<String,Object> map = new HashMap<>();
        map.put("taskid",taskid);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map,CheckInfo.class);
    }

    @Override
    public List<TaskFile> getTaskFile(String taskid) {
        StringBuffer sb = new StringBuffer();
        sb.append(" select * from MGR_TASK_FILE where taskid =: taskid");
        Map<String,Object> map = new HashMap<>();
        map.put("taskid",taskid);
        return pageHelper.getJdbcTempalte().queryForList(sb.toString(),map,TaskFile.class);
    }


    @Override
    public int addDataSetInfo(DataSetInfo dataSetInfo) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" insert into  mgr_task_applyinfo");
        sb.append(" (");
        sb.append(" id,\n");
        sb.append(" dataset_name,\n");
        sb.append("area_beat,\n");
        sb.append("time_range,\n");
        sb.append("dataset_level,\n");
        sb.append("applyid\n");
        sb.append(" ) values (");

        sb.append(":id,\n");
        sb.append(":dataset_name,\n");
        sb.append(":area_beat,\n");
        sb.append(":time_range,\n");
        sb.append(":dataset_level,\n");
        sb.append(":applyid)\n");

        Map<String,Object> map = new HashMap<>();
        map.put("id",UUID.randomUUID().toString());
        map.put("dataset_name",dataSetInfo.getDataset_name());
        map.put("area_beat", dataSetInfo.getArea_beat());
        map.put("time_range", dataSetInfo.getTime_range());
        map.put("dataset_level",dataSetInfo.getDataset_level());
        map.put("applyid", dataSetInfo.getApplyid());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int addTaskApplyInfo(TaskApplyData taskApplyData,LoginAuthDto loginAuthDto) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" insert into  mgr_task_dataapply");
        sb.append(" (");
        sb.append(" id,\n");
        sb.append(" userid,\n");
        sb.append("telephone,\n");
        sb.append("title,\n");
        sb.append("description,\n");
        sb.append("useinfo,\n");
        sb.append("create_time,\n");
        sb.append("status\n");
        sb.append(" ) values (");

        sb.append(":id,\n");
        sb.append(":userid,\n");
        sb.append(":telephone,\n");
        sb.append(":title,\n");
        sb.append(":description,\n");
        sb.append(":useinfo,\n");
        sb.append(":create_time,\n");
        sb.append(":status)\n");

        Map<String,Object> map = new HashMap<>();
        map.put("id",taskApplyData.getId());
        map.put("userid","2405");
        map.put("telephone", taskApplyData.getTelephone());
        map.put("title", taskApplyData.getTitle());
        map.put("description",taskApplyData.getDescription());
        map.put("useinfo", taskApplyData.getUseinfo());
        map.put("create_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("status", taskApplyData.getStatus());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int editDateSetInfo(DataSetInfo dataSetInfo) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update mgr_task_applyinfo ");
        sb.append(" set dataset_name=:dataset_name ,");
        sb.append(" area_beat=:area_beat,");
        sb.append(" time_range=:time_range, ");
        sb.append(" dataset_level=:dataset_level");
        sb.append(" WHERE id =:id ");

        Map<String,Object> map = new HashMap<>();
        map.put("dataset_name", dataSetInfo.getDataset_name());
        map.put("area_beat", dataSetInfo.getArea_beat());
        map.put("time_range", dataSetInfo.getTime_range());
        map.put("dataset_level", dataSetInfo.getDataset_level());
        map.put("id",dataSetInfo.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int editTaskApplyInfo(TaskApplyData taskApplyData, LoginAuthDto loginAuthDto) {
        StringBuffer sb = new StringBuffer(100);
        sb.append("update mgr_task_dataapply ");
        sb.append(" set telephone=:telephone ,");
        sb.append(" title=:title,");
        sb.append(" description=:description, ");
        sb.append(" useinfo=:useinfo,");
        sb.append(" update_time=:update_time");
        sb.append(" WHERE id =:id ");

        Map<String,Object> map = new HashMap<>();
        map.put("telephone", taskApplyData.getTelephone());
        map.put("title",taskApplyData.getTitle());
        map.put("description", taskApplyData.getDescription());
        map.put("useinfo", taskApplyData.getUseinfo());
        map.put("update_time",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("id",taskApplyData.getId());
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int addFileInfo(TaskFile taskFile) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" insert into  mgr_task_file");
        sb.append(" (");
        sb.append(" FILE_ID,\n");
        sb.append(" REAL_NAME,\n");
        sb.append("FILE_NAME,\n");
        sb.append("TASKID,\n");
        sb.append("TASK_TYPE,\n");
        sb.append("UP_TIME,\n");
        sb.append("DEADLINE \n");
        sb.append(" ) values (");

        sb.append(":FILE_ID,\n");
        sb.append(":REAL_NAME,\n");
        sb.append(":FILE_NAME,\n");
        sb.append(":TASKID,\n");
        sb.append(":TASK_TYPE,\n");
        sb.append(":UP_TIME,\n");
        sb.append(":DEADLINE )\n");

        Map<String,Object> map = new HashMap<>();
        map.put("FILE_ID",UUID.randomUUID().toString());
        map.put("REAL_NAME", taskFile.getReal_name());
        map.put("FILE_NAME", taskFile.getFile_name());
        map.put("TASKID",taskFile.getTask_id());
        map.put("TASK_TYPE", taskFile.getTask_type());
        map.put("UP_TIME",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("DEADLINE", taskFile.getDeadline());

        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }

    @Override
    public int updateTaskStatus(String taskid, String status) {
        StringBuffer sb = new StringBuffer();
        sb.append(" update mgr_task_dataapply set status =:status where id=:id");
        Map<String,Object> map = new HashMap<>();
        map.put("status",status);
        map.put("id",taskid);
        return pageHelper.getJdbcTempalte().update(sb.toString(), map);
    }

    @Override
    public int addCheckInfo(CheckInfo checkInfo) {
        StringBuffer sb  = new StringBuffer();
        sb.append(" insert into  mgr_task_check");
        sb.append(" (");
        sb.append(" id,\n");
        sb.append(" taskid,\n");
        sb.append("check_time,\n");
        sb.append("comments,\n");
        sb.append("status,\n");
        sb.append("userid\n");
        sb.append(" ) values (");

        sb.append(":id,\n");
        sb.append(":taskid,\n");
        sb.append(":check_time,\n");
        sb.append(":comments,\n");
        sb.append(":status,\n");
        sb.append(":userid )\n");

        Map<String,Object> map = new HashMap<>();
        map.put("id",UUID.randomUUID().toString());
        map.put("taskid", checkInfo.getTaskid());
        map.put("check_time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        map.put("comments",checkInfo.getComments());
        map.put("status", checkInfo.getStatus());
        map.put("userid",checkInfo.getUserid());
        return pageHelper.getJdbcTempalte().update(sb.toString(),map);
    }
}
