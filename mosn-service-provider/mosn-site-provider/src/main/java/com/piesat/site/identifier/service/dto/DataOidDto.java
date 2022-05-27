package com.piesat.site.identifier.service.dto;

import lombok.Data;

@Data
public class DataOidDto {

    private String busiId;
    private String oidDataClass;
    private String oidChildClass;
    private String oidProduceClass;
    private String oidTimeResolution;
    private String oidSpatialResolution;
    private String oidElement;
    private String oidRegion;
    private String oidDataForm;
    private String oidProduceUnit;
    private String dateRangeStart;
    private String dateRangeEnd;
}
