package com.piesat.site.system.dict.service.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "SYS_DICT")
public class SysDict {
    //'数据项编码'
    @Column(name = "DICT_CODE")
    private String dictCode;

    //'数据名称'
    @Column(name = "DICT_VALUE")
    private String dictValue;

    //'映射编码，映射第三方字典值'
    @Column(name = "MP_CODE")
    private String mpCode ;

    //'描述信息'
    @Column(name = "DESCRIPTION")
    private String description ;

    //'数据状态 00正常，01禁用' 默认为00
    @Column(name = "DATA_STATUS")
    private String dataStatus ;

    //'创建时间'
    @Column(name = "CREATE_DATE")
    private Date createDate ;

    //'创建人ID'
    @Column(name = "CREATE_BY_USERID")
    private Integer createByUserid;

    //'更新时间'
    @Column(name = "UPDATE_DATE")
    private Date updateDate ;

    //'更新人id'
    @Column(name = "UPDATE_BY_USERID")
    private Integer updateByUserid;
}
