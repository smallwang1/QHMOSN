package com.piesat.site.system.dict.service.dto;

import com.piesat.common.anno.Description;
import lombok.Data;
import java.util.Date;

@Data
public class EditDictReqDto {
    @Description("数据项编码")
    private String dictCode;

    @Description("数据项名称")
    private String dictValue;

    @Description("描述信息")
    private String description;

    @Description("更新时间")
    private Date updateDate ;
}
