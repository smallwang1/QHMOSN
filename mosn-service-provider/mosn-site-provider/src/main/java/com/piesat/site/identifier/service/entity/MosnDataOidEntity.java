package com.piesat.site.identifier.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnDataOidEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /* 业务系统主键 */
    private String busiId;

    /* 分类标识 */
    private String oidDataClass;

    /* 子类型 */
    private String oidChildClass;

    /* 生产方式 */
    private String oidProduceClass;

    /* 时间分辨率 */
    private String oidTimeResolution;

    /* 空间分辨率 */
    private String oidSpatialResolution;

    /* 要素 */
    private String oidElement;

    /* 区域 */
    private String oidRegion;

    /* 数据形式 */
    private String oidDataForm;

    /* 数据归属 */
    private String oidProduceUnit;

    /* 开始数据时间范围 */
    private String dateRangeStart;

    /* 结束数据时间范围 */
    private String dateRangeEnd;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("busiId", getBusiId())
                .append("oidDataClass", getOidDataClass())
                .append("oidChildClass", getOidChildClass())
                .append("oidProduceClass", getOidProduceClass())
                .append("oidTimeResolution", getOidTimeResolution())
                .append("oidSpatialResolution", getOidSpatialResolution())
                .append("oidElement", getOidElement())
                .append("oidRegion", getOidRegion())
                .append("oidDataForm", getOidDataForm())
                .append("oidProduceUnit", getOidProduceUnit())
                .append("dateRangeStart", getDateRangeStart())
                .append("dateRangeEnd", getDateRangeEnd())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
