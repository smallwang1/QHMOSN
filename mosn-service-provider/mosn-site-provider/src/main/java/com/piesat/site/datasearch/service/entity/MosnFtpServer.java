package com.piesat.site.datasearch.service.entity;

import com.piesat.site.datasearch.service.base.BaseEntity;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Data
public class MosnFtpServer extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键Id */
    private Long serverId;

    /** 服务地址 */
    private String host;

    /** 端口 */
    private Integer port;

    /** 用户名 */
    private String userName;

    /** 密码 */
    private String password;

    /** 文件存放路径 */
    private String filePath;

    /** 使用状态（0正常 1停用） */
    private String status;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("serverId", getServerId())
                .append("host", getHost())
                .append("port", getPort())
                .append("userName", getUserName())
                .append("password", getPassword())
                .append("filePath", getFilePath())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
