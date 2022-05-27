package com.piesat.site.system.dict.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

@Data
@Table(name = "SYS_DICT_ITEM")
public class SysDictItem {
    @Column(name = "ID")
    private Integer Id;

    //'数据项编码'
    @Column(name = "DICT_CODE")
    private String dictCode;

    //'字典项编码'
    @Column(name = "ITEM_CODE")
    private String itemCode;

    //'字典项值'
    @Column(name = "ITEM_VALUE")
    private String itemValue;

    //'映射编码，映射第三方字典值'
    @Column(name = "MP_CODE")
    private String mpCode ;

    //'数据项编码'
    @Column(name = "MP_ITEM_CODE")
    private String mpItemCode ;

    //'字典项编码'
    @Column(name = "MP_ITEM_VALUE")
    private String mpItemValue ;

    //'描述信息'
    @Column(name = "DESCRIPTION")
    private String Description ;

    //'排序'
    @Column(name = "ORDER")
    private Integer order ;

    //'创建时间'
    @DateTimeFormat(pattern = "yyyy-MM-dd")
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
