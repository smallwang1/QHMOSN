package com.piesat.site.identifier.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ServiceOidDto {

    private String busiId;
    private String name;
    private String serviceUnit;
    private String serviceName;
    private String serviceAddress;
    private String servicePhone;
    private String serviceEmail;
    private String serviceUse;
    private String limitGrading;
    private String limitDesc;
    private String serviceBusiness;
    private String serviceInfoOther;
    private String serviceAmount;
    private String serviceDateAuthTimeStart;
    private String serviceDateAuthTimeEnd;
    private String origin;
    private String status;
    private List<DataOidDto> source;
}
