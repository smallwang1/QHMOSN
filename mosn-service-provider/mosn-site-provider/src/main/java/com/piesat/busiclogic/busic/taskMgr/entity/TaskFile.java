package com.piesat.busiclogic.busic.taskMgr.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class TaskFile {

    @MapperColumn("FILE_ID")
    private  String file_id;

    @MapperColumn("PATH")
    private String path ;

    @MapperColumn("REAL_NAME")
    private String real_name;

    @MapperColumn("FILE_NAME")
    private String file_name;

    @MapperColumn("TASK_ID")
    private String task_id;

    @MapperColumn("TASK_TYPE")
    private String task_type;

    @MapperColumn("UPTIME")
    private String uptime;

    @MapperColumn("DEADLINE")
    private String deadline;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("TASK_STATUS")
    private String task_status;

    @MapperColumn("COMMENTS")
    private String comments;

}
