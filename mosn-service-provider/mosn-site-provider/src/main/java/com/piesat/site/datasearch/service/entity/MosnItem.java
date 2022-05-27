package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnItem extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long itemId;
    private String itemCode;
    private String itemName;
    private String itemType;
    private String required;
    private String visible;
    private String status;
    private Double orderNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("itemId", getItemId())
                .append("itemCode", getItemCode())
                .append("itemName", getItemName())
                .append("itemType", getItemType())
                .append("required", getRequired())
                .append("visible", getVisible())
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
