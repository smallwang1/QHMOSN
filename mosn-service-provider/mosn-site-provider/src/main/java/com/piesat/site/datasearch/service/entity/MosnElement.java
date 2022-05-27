package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnElement extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long elementId;
    private Long itemId;
    private String elementName;
    private String elementValue;
    private String elementType;
    private String status;
    private Double orderNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("elementId", getElementId())
                .append("itemId", getItemId())
                .append("elementName", getElementName())
                .append("elementValue", getElementValue())
                .append("elementType", getElementType())
                .append("status", getStatus())
                .append("orderNum", getOrderNum())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
