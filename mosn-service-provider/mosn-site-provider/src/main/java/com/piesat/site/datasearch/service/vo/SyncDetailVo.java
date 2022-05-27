package com.piesat.site.datasearch.service.vo;

import com.piesat.site.datasearch.service.dto.FtpServerDto;
import lombok.Data;

import java.util.List;

@Data
public class SyncDetailVo {

    private String isSync;
    private String syncType;
    private Object syncParam;
    private List<FtpServerDto> syncServer;
}
