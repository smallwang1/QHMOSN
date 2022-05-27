package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnRecommendDetail extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /* 选项ID */
    private String id;

    /* 选项名称 */
    private String name;

    /* 父级ID */
    private String pid;

    /* 挂载产品id */
    private String productId;

    /* 连接&路由地址 */
    private String url;

    /* 是否是外链"0"否，"1"是 */
    private String isLink;

    /* 显示顺序 */
    private String value;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("pid", getPid())
                .append("productId", getProductId())
                .append("url", getUrl())
                .append("isLink", getIsLink())
                .append("value", getValue())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
