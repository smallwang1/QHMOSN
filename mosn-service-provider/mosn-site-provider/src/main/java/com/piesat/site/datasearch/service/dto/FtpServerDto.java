package com.piesat.site.datasearch.service.dto;

import lombok.Data;

@Data
public class FtpServerDto {

    private Long serverId;
    private String host;
    private String userName;
    private String filePath;
    private String status;
}
