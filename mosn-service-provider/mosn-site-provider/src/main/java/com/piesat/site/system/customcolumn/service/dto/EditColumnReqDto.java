package com.piesat.site.system.customcolumn.service.dto;

import com.piesat.common.anno.Description;
import lombok.Data;

/**
 * @Author Thomas 2020/9/9 16:56
 * The world of programs is a wonderful world
 */
@Data
public class EditColumnReqDto {

    @Description("栏目ID")
    private Integer columnId;

    @Description("栏目名称")
    private String columnName;

    @Description("栏目跳转路径")
    private String columnUrl;

    @Description("栏目图标")
    private String columnPng;

    @Description("栏目状态")
    private String columnStatus;

    @Description("栏目跳转链接去向")
    private String columnLink;

    @Description("栏目排序")
    private String columnSort;

    @Description("创建时间")
    private String createTime;

    @Description("创建人")
    private String createUser;

    @Description("更新时间")
    private String updateTime;

    @Description("更新人")
    private String updateUser;

}
