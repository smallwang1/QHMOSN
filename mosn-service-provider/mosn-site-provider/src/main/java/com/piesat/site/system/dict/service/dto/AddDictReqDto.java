package com.piesat.site.system.dict.service.dto;

import com.piesat.common.anno.Description;
import lombok.Data;

import java.util.Date;

@Data
public class AddDictReqDto {
    @Description("数据项编码")
    private String dictCode;

    @Description("数据项名称")
    private String dictValue;

    @Description("描述信息")
    private String description;

    //默认00  01不可用
    @Description("数据状态")
    private String dataStatus;

    @Description("创建时间")
    /*@DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")*/
    private Date createDate ;

    @Description("更新时间")
    private Date updateDate ;
}
