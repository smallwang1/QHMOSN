package com.piesat.busiclogic.busic.warninfo.entity;

import lombok.Data;

/**
 * @Descripton 预警信息
 * @Author sjc
 * @Date 2020/3/9
 **/
@Data
public class Warning {

    // 标题
    private String alertid;

    // 父标题
    private String alertpid;

    // 省
    private String province;

    // 市
    private String city;

    // 站名/单位
    private String stationname;

    // 经度
    private String stationlon;

    // 纬度
    private String stationlat;

    // 预警信号
    private String standard;

    // 预警信号类型
    private String signaltype;

    // 预警信号等级
    private String signallevel;

    // 站点编号
    private String stationid;

    // 发布时间
    private String issuetime;

    // 预警信号状态
    private String change;

    // 时效
    private String signalvalidhour;

    // 预警内容
    private String issuecontent;

}
