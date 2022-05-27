package com.piesat.site.datasearch.service.vo;

import lombok.Data;

@Data
public class OrderDetailVo {

    private String fileType;
    private String orderName;
    private String auditReason;
    private Object params;
}
