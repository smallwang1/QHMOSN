package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ResourceDto {

    private Long resourceId;
    private String resourceCode;
    private String resourceName;
    private String resourceType;
    private String serviceNodeId;
    private String summary;
    private List<ButtonDto> buttons;
    private List<FormDto> formList;
}
