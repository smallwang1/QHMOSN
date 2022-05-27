package com.piesat.site.datalist.service.entity;

import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

@Data
public class MenuInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /* 选项id */
    private String id;

    /* 选项名称 */
    private String name;

    /* 父级ID */
    private String pid;

    /* 选项编码 */
    private String code;

    /* 应用编码 */
    private String appCode;

    /* 状态 */
    private String status;

    /* 是否含有下级目录 */
    private String hasNext;

    /* 连接&路由地址 */
    private String url;

    /* 文件头信息 */
    private String head;

    /* 初始参数 */
    private String initParam;

    /* 排序 */
    private String sort;

    /* 资料文件格式 */
    private String format;

    /* HEAD 类型名称 */
    private String typeName;

    /* HEAD类型值 */
    private String typeValue;

    /* HEAD,code */
    private String typeCode;

    /* LEVEL,名称 */
    private String levelName;

    /* LEVEL值 */
    private String levelValue;

    /* HEAD,CODE */
    private String levelCode;

    /* 挂载产品id */
    private String  productId;

    /* 挂载页面地址 */
    private String  pagUrl;

    /* 是否是外链"0"否，"1"是 */
    private String isLink;

    /* 创建时间 */
    private String createTime;

    /* 是否是产品菜单 */
    private String isProduct;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("name", getName())
                .append("pid", getStatus())
                .append("code", getSort())
                .append("appCode", getPid())
                .append("status", getStatus())
                .append("hasNext", getHasNext())
                .append("url", getUrl())
                .append("head", getHead())
                .append("initParam", getInitParam())
                .append("sort", getSort())
                .append("format", getFormat())
                .append("typeName", getTypeName())
                .append("typeValue", getTypeValue())
                .append("typeCode", getTypeCode())
                .append("levelValue", getLevelValue())
                .append("levelCode", getLevelCode())
                .append("productId", getProductId())
                .append("productName", getPagUrl())
                .append("isLink", getIsLink())
                .append("createTime", getCreateTime())
                .append("isProduct", getIsProduct())
                .toString();
    }
}
