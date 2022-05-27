package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnSyncHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String syncNo;
    private String syncType;
    private String syncParam;
    private String syncHost;
    private String syncPath;
    private String syncStatus;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("syncNo", getSyncNo())
                .append("syncType", getSyncType())
                .append("syncParam", getSyncParam())
                .append("syncHost", getSyncHost())
                .append("syncStatus", getSyncStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
