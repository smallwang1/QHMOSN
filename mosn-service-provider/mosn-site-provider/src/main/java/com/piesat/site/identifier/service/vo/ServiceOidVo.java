package com.piesat.site.identifier.service.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ServiceOidVo {

    @NotBlank(message = "业务系统主键不能为空")
    private String busiId;
    @NotBlank(message = "服务名称不能为空")
    private String name;
    @NotBlank(message = "服务用途不能为空")
    private String serviceUse;
    @NotBlank(message = "服务对象名称不能为空")
    private String serviceName;
    @NotBlank(message = "服务对象地址不能为空")
    private String serviceAddress;
    @NotBlank(message = "服务对象电话不能为空")
    private String servicePhone;
    @NotBlank(message = "服务对象邮箱不能为空")
    private String serviceEmail;
    @NotBlank(message = "服务对象行业不能为空")
    private String serviceBusiness;
    private String serviceInfoOther;
    private String serviceDateAuthTimeStart;
    private String serviceDateAuthTimeEnd;
    private String status;
    private String startTime;
    private String endTime;
}
