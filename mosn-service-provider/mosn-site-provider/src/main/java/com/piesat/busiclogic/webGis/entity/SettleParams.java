package com.piesat.busiclogic.webGis.entity;

import lombok.Data;

@Data
public class SettleParams {

    private String  dataCode;

    private String interfaceId;

    private String elements = "";

    private String timeRange;

    private String orderBy;

    private String eleValueRanges;

    private String url;
}
