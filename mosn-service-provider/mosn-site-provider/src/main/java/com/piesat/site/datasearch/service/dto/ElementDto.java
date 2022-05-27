package com.piesat.site.datasearch.service.dto;

import lombok.Data;

@Data
public class ElementDto {

    private Long elementId;
    private Long itemId;
    private String elementName;
    private String elementValue;
    private String elementType;
}
