package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnButton extends BaseEntity {

    private Long id;
    private String buttonName;
    private String buttonCode;
    private String status;
    private Double orderNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("buttonName", getButtonName())
                .append("buttonCode", getButtonCode())
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
