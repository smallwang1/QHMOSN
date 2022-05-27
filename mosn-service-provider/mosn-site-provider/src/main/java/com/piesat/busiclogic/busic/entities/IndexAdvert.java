package com.piesat.busiclogic.busic.entities;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class IndexAdvert {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("NAME")
    private String name;

    @MapperColumn("BASE64DATA")
    private String base64data;

    @MapperColumn("SORT")
    private String sort;

    @MapperColumn("CREATE_TIME")
    private String create_time;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("DESCRIPTION")
    private String description;

    @MapperColumn("TYPE")
    private String type;

    @MapperColumn("LINK")
    private String link;
}
