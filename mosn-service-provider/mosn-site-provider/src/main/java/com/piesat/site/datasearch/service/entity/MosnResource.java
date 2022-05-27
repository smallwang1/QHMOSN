package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnResource extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long resourceId;
    private String resourceCode;
    private String resourceName;
    private String resourceType;
    private String dataFormat;
    private String summary;
    private String serviceNodeId;
    private String forms;
    private String titles;
    private String elements;
    private String buttons;
    private String tdsPath;
    private String cutItems;
    private String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("resourceId", getResourceId())
                .append("resourceCode", getResourceCode())
                .append("resourceName", getResourceName())
                .append("resourceType", getResourceType())
                .append("dataFormat", getDataFormat())
                .append("summary", getSummary())
                .append("serviceNodeId", getServiceNodeId())
                .append("forms", getForms())
                .append("titles", getTitles())
                .append("elements", getElements())
                .append("buttons", getButtons())
                .append("tdsPath", getTdsPath())
                .append("cutItems", getCutItems())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
