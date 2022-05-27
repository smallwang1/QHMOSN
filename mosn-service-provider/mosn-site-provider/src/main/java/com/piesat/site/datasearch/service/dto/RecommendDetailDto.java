package com.piesat.site.datasearch.service.dto;

import com.piesat.site.datasearch.service.vo.RecommendVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendDetailDto {

    private String id;
    private String name;
    private String pid;
    private String productId;
    private String url;
    private String isLink;
    private String value;

    public RecommendDetailDto(RecommendVo recommendVo){
        this.id = recommendVo.getDetailId();
        this.name = recommendVo.getDetailName();
        this.pid = recommendVo.getPid();
        this.productId = recommendVo.getProductId();
        this.url = recommendVo.getUrl();
        this.isLink = recommendVo.getIsLink();
        this.value = recommendVo.getValue();
    }
}
