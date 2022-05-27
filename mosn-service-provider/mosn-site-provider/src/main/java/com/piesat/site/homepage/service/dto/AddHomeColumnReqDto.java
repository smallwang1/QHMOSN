package com.piesat.site.homepage.service.dto;

import com.piesat.common.anno.Description;
import lombok.Data;

/**
 * @Author Thomas 2020/9/9 16:56
 * The world of programs is a wonderful world
 */
@Data
public class AddHomeColumnReqDto {

    @Description("用户ID")
    private String userId;

    @Description("栏目ID")
    private String columnId;

    @Description("栏目状态")
    private Integer columnStatus;

    @Description("栏目展示排序")
    private Integer showSort;
}
