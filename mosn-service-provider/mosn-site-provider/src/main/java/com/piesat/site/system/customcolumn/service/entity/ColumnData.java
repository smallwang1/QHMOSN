package com.piesat.site.system.customcolumn.service.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.File;
import java.util.Date;

@Data
@Table(name = "MGR_CUSTOM_COLUMN")
public class ColumnData {

    //栏目ID
    @Column(name = "COLUMN_ID")
    private Integer columnId;

    //栏目名称
    @Column(name = "COLUMN_NAME")
    private String columnName;

    //跳转路径
    @Column(name = "URL")
    private String columnUrl;

    //展示栏目的图片
    @Column(name = "PNG_SHOW")
    private byte[] columnPng;

    //项目状态 1、启用  2、停用  3、删除
    @Column(name = "STATUS")
    private Integer columnStatus;

    //跳转链接去向 1、本地 2、第三方
    @Column(name = "JUMP_LINK")
    private String columnLink;

    //排序，由小到大进行排序
    @Column(name = "SORT")
    private Integer columnSort;

    //创建时间
    @Column(name = "CREATE_TIME")
    private Date createTime;

    //创建人
    @Column(name = "CREATE_USER")
    private String createUser;

    //更新时间
    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    //更新人
    @Column(name = "UPDATE_USER")
    private String updateUser;
}
