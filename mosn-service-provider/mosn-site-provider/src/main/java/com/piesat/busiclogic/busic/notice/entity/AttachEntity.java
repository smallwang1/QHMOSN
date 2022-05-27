package com.piesat.busiclogic.busic.notice.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class AttachEntity {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("FILENAME")
    private String fileName;

    @MapperColumn("ADDRESS")
    private String address;

    @MapperColumn("CREATE_TIME")
    private String create_time;

    @MapperColumn("CREATED_USERID")
    private String created_userid;



    @MapperColumn("NOTICEID")
    private String noticeid;

}
