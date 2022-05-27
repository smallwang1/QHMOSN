package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SyncHisDto {

    private String syncNo;
    private String syncType;
    private String syncParam;
    private String syncHost;
    private String syncPath;
    private String syncStatus;
    private Date createTime;
    private String remark;
}
