package com.piesat.site.datasearch.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class FormDto<T> {

    private Long formId;
    private String formCode;
    private String formName;
    private String isSearch;
    private List<T> titleList;
}
