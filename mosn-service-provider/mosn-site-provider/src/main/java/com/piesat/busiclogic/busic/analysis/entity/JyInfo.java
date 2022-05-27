/**
 * 项目名称:analysis
 * 类名称:JyInfo.java
 * 包名称:com.piesat.busiclogic.busic.analysis.entity
 *
 * 修改履历:
 *       日期                   修正者      主要内容
 *  Thu Apr 02 16:30:26 CST 2020       admin         初版做成
 *
 * Copyright (c) 2007-2018
 */

package com.piesat.busiclogic.busic.analysis.entity;

import java.io.Serializable;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
/**
 * JyInfo  实体类 <br>
 * 表 JY_INFO 的实体类. <br>
 * @author admin
 * @version 1.0
 */
@Data
public class JyInfo implements Serializable {
    
    /**
     * 使用一个默认的UID
     */
    private static final long serialVersionUID = -4540746011916711939L;

    /**
     * $column.comments
     */
    @ApiModelProperty(value="$column.comments",name="Date")
    private String date;



    /**
     * $column.comments
     */
    @ApiModelProperty(value="$column.comments",name="Description")
    private String description;



    /**
     * $column.comments
     */
    @ApiModelProperty(value="$column.comments",name="Filename")
    private String filename;



    /**
     * $column.comments
     */
    @ApiModelProperty(value="$column.comments",name="Filetype")
    private String filetype;



    /**
     * $column.comments
     */
    @ApiModelProperty(value="$column.comments",name="Id")
    private String id;



    /**
     * $column.comments
     */
    @ApiModelProperty(value="$column.comments",name="Time")
    private String time;



}
