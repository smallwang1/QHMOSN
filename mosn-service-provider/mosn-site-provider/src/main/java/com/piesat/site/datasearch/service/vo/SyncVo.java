package com.piesat.site.datasearch.service.vo;

import lombok.Data;

import java.util.Date;

@Data
public class SyncVo {

    private Long id;
    private String syncNo;
    private String syncType;
    private String syncPeriod;
    private Long syncServerId;
    private String syncServerPath;
    private String syncStatus;
    private String fileType;
    private String syncParam;
    private Date nextSyncTime;
}
