package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MosnInterface extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long interfaceId;
    private String interfaceCode;
    private String interfaceName;
    private String requiredParam;
    private String optionalParam;
    private String resourceCode;
    private String status;
    private Double orderNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("interfaceId", getInterfaceId())
                .append("interfaceCode", getInterfaceCode())
                .append("interfaceName", getInterfaceName())
                .append("requiredParam", getRequiredParam())
                .append("optionalParam", getOptionalParam())
                .append("resourceCode", getResourceCode())
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
