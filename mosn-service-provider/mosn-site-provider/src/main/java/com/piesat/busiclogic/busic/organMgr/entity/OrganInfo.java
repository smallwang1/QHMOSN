package com.piesat.busiclogic.busic.organMgr.entity;

import com.piesat.jdbc.anno.MapperColumn;
import lombok.Data;

@Data
public class OrganInfo {

    @MapperColumn("ID")
    private String id;

    @MapperColumn("ORGAN_NAME")
    private String organ_name;

    @MapperColumn("GW_NAME")
    private String gw_name;

    @MapperColumn("ORGAN_STATE")
    private String organ_state;

    @MapperColumn("ORGAN_STATE_NAME")
    private String organ_state_name;

    @MapperColumn("ORDERING")
    private String ordering;

    @MapperColumn("PARENT_ORGAN")
    private String parent_organ;

    @MapperColumn("OBSERVATORY")
    private String observatory;
}
