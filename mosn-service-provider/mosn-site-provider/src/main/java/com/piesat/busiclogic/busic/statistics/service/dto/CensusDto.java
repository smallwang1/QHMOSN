package com.piesat.busiclogic.busic.statistics.service.dto;

import lombok.Data;

@Data
public class CensusDto<T> {

    private Long amount;
    private String censusId;
    private String censusType;
    private T requestParam;
}
