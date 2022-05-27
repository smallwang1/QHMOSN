package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class SyncFtpDto {

    private String orderNo;
    private String isSync;
    private String syncType;
    private String syncParam;
    private List<FtpServerDto> syncServer;
}
