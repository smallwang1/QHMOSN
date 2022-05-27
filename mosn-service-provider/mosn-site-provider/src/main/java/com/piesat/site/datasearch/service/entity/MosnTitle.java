package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnTitle extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long titleId;
    private Long formId;
    private String titleName;
    private String titleType;
    private String items;
    private String visible;
    private String status;
    private Double orderNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("titleId", getTitleId())
                .append("formId", getFormId())
                .append("titleName", getTitleName())
                .append("items", getItems())
                .append("visible", getStatus())
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
