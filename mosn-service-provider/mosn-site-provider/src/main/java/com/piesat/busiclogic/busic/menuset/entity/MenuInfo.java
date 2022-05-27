package com.piesat.busiclogic.busic.menuset.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class MenuInfo {

    @MapperColumn("ID")
    private String id ;

    @MapperColumn("NAME")
    private String name;

    @MapperColumn("PID")
    private String pid;

    @MapperColumn("CODE")
    private String code;

    @MapperColumn("APPCODE")
    private String appcode;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("HASNEXT")
    private String hasnext;

    @MapperColumn("URL")
    private String url;

    @MapperColumn("HEAD")
    private String head;

    @MapperColumn("INITPARAM")
    private String initparam;

    @MapperColumn("SORT")
    private String sort;

    @MapperColumn("FORMAT")
    private String format;

    @MapperColumn("TYPE_NAME")
    private String type_name;

    @MapperColumn("TYPE_VALUE")
    private String type_value;

    @MapperColumn("TYPE_CODE")
    private String type_code;

    @MapperColumn("LEVEL_NAME")
    private String level_name;

    @MapperColumn("LEVEL_VALUE")
    private String level_value;

    @MapperColumn("LEVEL_CODE")
    private String level_code;

    @MapperColumn("PRODUCT_ID")
    private String  product_id;

    @MapperColumn("PRODUCT_NAME")
    private String  product_name;

    @MapperColumn("IS_LINK")
    private String is_link;

    @MapperColumn("CREATE_TIME")
    private String create_time;

}
