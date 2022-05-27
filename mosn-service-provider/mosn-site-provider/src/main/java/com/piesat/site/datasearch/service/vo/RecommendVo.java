package com.piesat.site.datasearch.service.vo;

import lombok.Data;

@Data
public class RecommendVo {

    private Long id;
    private String recommendName;
    private String recommendIcon;
    private String detailId;
    private String detailName;
    private String pid;
    private String productId;
    private String url;
    private String isLink;
    private String value;
}
