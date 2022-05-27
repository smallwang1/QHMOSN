package com.piesat.site.datasearch.service.vo;

import lombok.Data;

@Data
public class OrderVo {

    private String orderNo;
    private String orderName;
    private String fileType;
    private String fileStatus;
    private String orderStatus;
    private String auditBy;
    private String startTime;
    private String endTime;
}
