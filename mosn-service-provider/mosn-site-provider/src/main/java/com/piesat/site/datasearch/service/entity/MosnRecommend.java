package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnRecommend extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /* 主键id */
    private Long id;

    /* 推荐名称 */
    private String recommendName;

    /* 推荐图标 */
    private String recommendIcon;

    /* 显示顺序 */
    private double sort;

    /* 具体产品推荐 */
    private String detailId;

    /* 状态（0正常 1停用） */
    private String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("recommendName", getRecommendName())
                .append("recommendIcon", getRecommendIcon())
                .append("sort", getSort())
                .append("detailId", getDetailId())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
