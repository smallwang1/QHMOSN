package com.piesat.busiclogic.busic.productMgr.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class ProductHead {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("PRODUCT_ID")
    private String product_id;

    @MapperColumn("PID")
    private String pid;

    @MapperColumn("NAME")
    private String name;

    @MapperColumn("VALUE")
    private String value;

    @MapperColumn("SORT")
    private String sort;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("LEVEL")
    private String level;

    @MapperColumn("LEVELNAME")
    private String levelName;
}
