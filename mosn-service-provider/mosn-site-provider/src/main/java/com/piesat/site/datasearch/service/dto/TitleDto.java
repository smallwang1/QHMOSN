package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class TitleDto {

    private Long titleId;
    private Long formId;
    private String titleName;
    private String titleType;
    private String visible;
    private List<ItemDto> itemList;
}
