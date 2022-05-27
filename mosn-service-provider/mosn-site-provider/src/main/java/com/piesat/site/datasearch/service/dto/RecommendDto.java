package com.piesat.site.datasearch.service.dto;

import lombok.Data;

@Data
public class RecommendDto {

    private Long id;
    private String recommendName;
    private String recommendIcon;
    private String detailName;
    private RecommendDetailDto recommendDetail;
}
