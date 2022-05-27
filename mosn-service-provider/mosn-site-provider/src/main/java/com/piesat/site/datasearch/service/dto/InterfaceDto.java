package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class InterfaceDto {

    private Long interfaceId;
    private String interfaceCode;
    private String interfaceName;
    private List<ItemDto> itemList;
}
