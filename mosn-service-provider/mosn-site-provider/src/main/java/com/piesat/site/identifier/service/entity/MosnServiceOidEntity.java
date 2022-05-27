package com.piesat.site.identifier.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnServiceOidEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /* 业务系统主键 */
    private String busiId;

    /* 服务名称 */
    private String name;

    /* 服务单位 */
    private String serviceUnit;

    /* 服务对象名称 */
    private String serviceName;

    /* 服务对象地址 */
    private String serviceAddress;

    /* 服务对象电话 */
    private String servicePhone;

    /* 服务对象邮箱 */
    private String serviceEmail;

    /* 服务用途 */
    private String serviceUse;

    /* 限制 */
    private String limitGrading;

    /* 限制信息描述 */
    private String limitDesc;

    /* 服务对象行业 */
    private String serviceBusiness;

    /* 其他服务信息描述 */
    private String serviceInfoOther;

    /* 服务金额 */
    private String serviceAmount;

    /* 服务授权使用开始时间 */
    private String serviceDateAuthTimeStart;

    /* 服务授权使用结束时间 */
    private String serviceDateAuthTimeEnd;

    /* 数据来源 */
    private String origin;

    /* 服务ID编码 */
    private String serviceId;

    /* 标识符申请状态 (0:待申请 1:申请中 2:申请通过 3:申请失败)*/
    private String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("busiId", getBusiId())
                .append("name", getName())
                .append("serviceUnit", getServiceUnit())
                .append("serviceName", getServiceName())
                .append("serviceAddress", getServiceAddress())
                .append("servicePhone", getServicePhone())
                .append("serviceEmail", getServiceEmail())
                .append("serviceUse", getServiceUse())
                .append("limitGrading", getLimitGrading())
                .append("limitDesc", getLimitDesc())
                .append("serviceBusiness", getServiceBusiness())
                .append("serviceInfoOther", getServiceInfoOther())
                .append("serviceAmount", getServiceAmount())
                .append("serviceDateAuthTimeStart", getServiceDateAuthTimeStart())
                .append("serviceDateAuthTimeEnd", getServiceDateAuthTimeEnd())
                .append("origin", getOrigin())
                .append("serviceId", getServiceId())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
