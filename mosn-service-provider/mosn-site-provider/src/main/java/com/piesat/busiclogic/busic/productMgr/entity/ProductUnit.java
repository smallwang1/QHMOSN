package com.piesat.busiclogic.busic.productMgr.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class ProductUnit {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("SCALE")
    private String scale;

    @MapperColumn("DESCRIBTION")
    private String describtion;

    @MapperColumn("FMS_ID")
    private String fms_id;

    @MapperColumn("PARAM")
    private String param;

    @MapperColumn("RELATEID")
    private String relateid;

    @MapperColumn("STATUS")
    private String status;

    @MapperColumn("NAME")
    private String name;

    @MapperColumn("URL")
    private String url;

    @MapperColumn("REQUEST_TYPE")
    private String request_type;

    @MapperColumn("FORMAT")
    private String format;

    @MapperColumn("ANALYSIS_DATA")
    private String analysis_data;

    @MapperColumn("TIME_FIELD")
    private String time_field;

    @MapperColumn("ROUTER")
    private String router;

    @MapperColumn("URL_TYPE")
    private String url_type;

    @MapperColumn("SHOW_PATTERN")
    private String show_pattern;

    @MapperColumn("TIME_TYPE")
    private String[] time_type;

    @MapperColumn("TIME_ZONE")
    private String time_zone;

    @MapperColumn("PARAM_POINT")
    private String param_point;

    @MapperColumn("TIME_FIELD_POINT")
    private String time_field_point;

    @MapperColumn("DEFAULT_TIME")
    private String default_time;

    @MapperColumn("DEFAULT_TIME_POINT")
    private String default_time_point;

    @MapperColumn("TIME_FORMAT")
    private String time_format;

}
