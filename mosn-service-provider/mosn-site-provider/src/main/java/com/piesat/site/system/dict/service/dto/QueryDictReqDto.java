package com.piesat.site.system.dict.service.dto;

import com.piesat.common.anno.Description;
import lombok.Data;

@Data
public class QueryDictReqDto {

        @Description("当前页")
        private Integer currentPage;

        @Description("每页条数")
        private Integer pageSize;

        @Description("数据项编码")
        private String dictCode;

        @Description("数据项名称")
        private String dictValue;

        @Description("描述")
        private String description;

        @Description("状态")
        private String dataStatus;

}
