package com.piesat.site.datasearch.service.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

@Data
public class MosnOrder extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 订单号 */
    private String orderNo;

    /** 订单名称 */
    private String orderName;

    /** 查询参数 */
    private String orderParam;

    /** 文件类型 */
    private String fileType;

    /** 文件大小 */
    private String fileSize;

    /** 文件路径 */
    private String filePath;

    /** 文件名称 */
    private String fileName;

    /** 文件状态 */
    private String fileStatus;

    /** 订单状态 */
    private String orderStatus;

    /** 审核者 */
    private String auditBy;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date auditTime;

    /* 审核意见 */
    private String auditReason;

    /* 裁剪参数 */
    private String cutParam;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("orderNo", getOrderNo())
                .append("orderName", getOrderName())
                .append("orderParam", getOrderParam())
                .append("fileType", getFileType())
                .append("fileSize", getFileSize())
                .append("filePath", getFilePath())
                .append("fileStatus", getFileStatus())
                .append("fileName", getFileName())
                .append("orderStatus", getOrderStatus())
                .append("auditBy", getAuditBy())
                .append("auditTime", getAuditTime())
                .append("auditReason", getAuditReason())
                .append("cutParam", getCutParam())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
