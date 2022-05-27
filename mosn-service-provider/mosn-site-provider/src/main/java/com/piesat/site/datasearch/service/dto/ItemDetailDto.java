package com.piesat.site.datasearch.service.dto;

import lombok.Data;

@Data
public class ItemDetailDto {

    private Long detailId;
    private Long itemId;
    private String detailName;
    private String detailValue;
    private String detailType;
    private String visible;
    private String remark;
}
