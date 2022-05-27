package com.piesat.site.datalist.service.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
public class PbTqDataChild implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 资料大类编码 */
    private String dataClassId;

    /* 资料名称 */
    private String dataName;

    /* 资料编码 */
    private String dataCode;

    /* 四级编码 */
    private String dDataId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("dataClassId", getDataClassId())
                .append("dataName", getDataName())
                .append("dataCode", getDataCode())
                .append("dDataId", getDDataId())
                .toString();
    }
}
