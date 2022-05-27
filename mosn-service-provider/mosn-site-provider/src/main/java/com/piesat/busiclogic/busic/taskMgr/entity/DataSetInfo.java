package com.piesat.busiclogic.busic.taskMgr.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class DataSetInfo {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("DATASET_NAME")
    private String dataset_name;

    @MapperColumn("AREA_BEAT")
    private String area_beat;

    @MapperColumn("TIME_RANGE")
    private String time_range;

    @MapperColumn("DATASET_LEVEL")
    private String dataset_level;

    @MapperColumn("APPLYID")
    private String applyid;
}
