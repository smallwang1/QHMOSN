package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnNavMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long menuId;

    /** 父导航菜单id */
    private Long parentId;

    /** 导航菜单名称 */
    private String menuName;

    /** 产品资料id集合（以 "," 分割） */
    private String resourceArr;

    /** 导航菜单类型（F一级 S二级） */
    private String menuType;

    /** 导航菜单图标 */
    private String menuIcon;

    /** 显示状态（0显示 1隐藏） */
    private String visible;

    /** 使用状态（0正常 1停用） */
    private String status;

    /** 显示顺序 */
    private Double orderNum;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("menulId", getMenuId())
                .append("parentId", getParentId())
                .append("menuName", getMenuName())
                .append("resourceArr", getResourceArr())
                .append("menuType", getMenuType())
                .append("menuIcon", getMenuIcon())
                .append("visible", getVisible())
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
