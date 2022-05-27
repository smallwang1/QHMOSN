package com.piesat.site.datasearch.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

@Data
public class MosnSyncFtp extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String orderNo;
    private String syncType;
    private String syncParam;
    private Long syncServerId;
    private String syncServerPath;
    private String status;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date nextSyncTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("orderNo", getOrderNo())
                .append("syncType", getSyncType())
                .append("syncParam", getSyncParam())
                .append("syncServerId", getSyncServerId())
                .append("syncServerPath", getSyncServerPath())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
