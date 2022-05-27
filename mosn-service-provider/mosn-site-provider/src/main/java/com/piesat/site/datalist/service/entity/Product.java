package com.piesat.site.datalist.service.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 主键ID */
    private String id;

    /* 产品名称 */
    private String productName;

    /* 启停状态 */
    private String status;

    /* 排序 */
    private String sort;

    /* 父级ID */
    private String pid;

    /* 是否是产品"0",否，"1"是 */
    private String isPro;

    /* 产品图标数据 */
    private String iconData;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("productName", getProductName())
                .append("status", getStatus())
                .append("sort", getSort())
                .append("pid", getPid())
                .append("isPro", getIsPro())
                .append("iconData", getIconData())
                .toString();
    }
}
