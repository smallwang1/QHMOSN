package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {

    private String orderNo;
    private String orderName;
    private String fileType;
    private String fileSize;
    private String filePath;
    private String fileName;
    private String fileStatus;
    private String orderStatus;
    private Date createTime;
    private Date updateTime;
    private String auditBy;
    private Date auditTime;
    private String remark;
}
