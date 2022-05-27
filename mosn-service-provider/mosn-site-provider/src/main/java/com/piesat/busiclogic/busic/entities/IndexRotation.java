package com.piesat.busiclogic.busic.entities;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class IndexRotation {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("ROTATION_NAME")
    private String rotation_name;

    @MapperColumn("UNIT_ID")
    private String unit_id;

    @MapperColumn("SORT")
    private String sort;

    @MapperColumn("CREATE_TIME")
    private String create_time;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("PRODUCT_ID")
    private String product_id;

    @MapperColumn("PRODUCT_NAME")
    private String product_name;

    @MapperColumn("ROUTE")
    private String route;

    @MapperColumn("MENU_ID")
    private String menu_id;
}
