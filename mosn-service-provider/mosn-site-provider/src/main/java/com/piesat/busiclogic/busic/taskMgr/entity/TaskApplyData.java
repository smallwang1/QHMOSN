package com.piesat.busiclogic.busic.taskMgr.entity;

import com.piesat.common.anno.Description;
import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TaskApplyData {

    @MapperColumn("ID")
    private String id ;

    @MapperColumn("USERID")
    private String userid;

    @MapperColumn("USERNAME")
    private String username;

    @MapperColumn("TELEPHONE")
    private String telephone;

    @MapperColumn("TITLE")
    private String title;

    @MapperColumn("DESCRIPTION")
    private String description;

    @MapperColumn("USEINFO")
    private String useinfo;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("CREATE_TIME")
    private String create_time;

    @MapperColumn("UPDATE_TIME")
    private String update_time;

    @MapperColumn("UPDATE_USER")
    private String update_user;

    /**
     * 资料清单信息
      */
    @MapperColumn("DATASETINFOLIST")
    private List<DataSetInfo> dataSetInfoList = new ArrayList<>();

    /**
     * 审核信息
     */
    @MapperColumn("CHECKINFOKLIST")
    private List<CheckInfo> checkInfokList = new ArrayList<>();

    /**
     * 附件信息
     */
    @MapperColumn("TASKFILELIST")
    private List<TaskFile> taskFileList = new ArrayList<>();

}
