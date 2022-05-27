package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemDto<T> {

    private Long itemId;
    private String itemCode;
    private String itemName;
    private String itemType;
    private String required;
    private String visible;
    private String isElement;
    private List<T> detailList;
}
