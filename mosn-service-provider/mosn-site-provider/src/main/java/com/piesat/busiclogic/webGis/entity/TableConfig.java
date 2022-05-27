package com.piesat.busiclogic.webGis.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class TableConfig {

    @MapperColumn("T_ID")
    private String t_id;

    @MapperColumn("ID")
    private String id;

    @MapperColumn("SHOW_STATION_TYPE")
    private String show_station_type;

    @MapperColumn("ELEMENTS")
    private String elements;

    @MapperColumn("ELE_NAME")
    private String ele_name;

    @MapperColumn("DESCRIPTION")
    private String description;

    @MapperColumn("STATISTICS_CELL")
    private String statistics_cell;

    @MapperColumn("TABLE_ELES")
    private String table_eles;

    @MapperColumn("TABLE_ELENAME")
    private String table_elename;

    @MapperColumn("REQ_TYPE")
    private String req_type;

    @MapperColumn("REQ_URL")
    private String req_url;

    @MapperColumn("REQ_PARAM")
    private String req_param;
}
