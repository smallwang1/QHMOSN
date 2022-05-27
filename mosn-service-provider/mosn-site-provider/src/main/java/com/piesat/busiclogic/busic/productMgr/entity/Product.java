package com.piesat.busiclogic.busic.productMgr.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class Product {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("PRODUCT_NAME")
    private String product_name;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("SORT")
    private String sort;

    @MapperColumn("PID")
    private String pid;

    @MapperColumn("IS_PRO")
    private String is_pro;

    @MapperColumn("ICON_DATA")
    private String icon_data;
}


