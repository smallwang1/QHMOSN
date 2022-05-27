package com.piesat.site.datalist.service.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
public class PbTqDataDef implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 资料大类编码 */
    private String dataClassId;

    /* 资料大类名称 */
    private String dataClassName;

    /* 排序 */
    private Integer serialNo;

    /* 资料描述信息 */
    private String description;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dataClassId", getDataClassId())
                .append("dataClassName", getDataClassName())
                .append("serialNo", getSerialNo())
                .append("description", getDescription())
                .toString();
    }
}
