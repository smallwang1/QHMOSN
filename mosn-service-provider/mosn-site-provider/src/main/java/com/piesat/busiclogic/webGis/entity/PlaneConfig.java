package com.piesat.busiclogic.webGis.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class PlaneConfig {

    @MapperColumn("PLANE_ID")
    private String plane_id;

    @MapperColumn("ID")
    private String id;

    @MapperColumn("TITLE")
    private String title;

    @MapperColumn("TIME_AXIS")
    private String time_axis;

    @MapperColumn("SHOW_TYPE")
    private String show_type;

    @MapperColumn("LEGEND_SHOW")
    private String legend_show;

    @MapperColumn("LEGEND_TYPE")
    private String legend_type;

    @MapperColumn("LEGEND_UNIT")
    private String legend_unit;

    @MapperColumn("LEGEND_COLORS")
    private String legend_colors;

    @MapperColumn("LEGEND_VALUE")
    private String legend_value;

    @MapperColumn("LEGEND_BASE64")
    private String legend_base64;

    @MapperColumn("IS_BUBBLE")
    private String is_bubble;

    @MapperColumn("FIELD_BUBBLE")
    private String field_bubble;

}
