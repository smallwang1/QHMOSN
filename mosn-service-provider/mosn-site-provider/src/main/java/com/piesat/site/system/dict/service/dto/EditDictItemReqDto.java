package com.piesat.site.system.dict.service.dto;

import com.piesat.common.anno.Description;
import lombok.Data;

import java.util.Date;

@Data
public class EditDictItemReqDto {
    @Description("ID")
    private Integer id;

    /*@Description("数据项编码")
    private String dictCode;*/

    @Description("字典项编码")
    private String itemCode;

    @Description("字典项值")
    private String itemValue;

    @Description("描述信息")
    private String description;

    @Description("排序")
    private Integer order;

    @Description("更新时间")
    private Date updateDate ;
}
