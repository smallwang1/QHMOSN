package com.piesat.busiclogic.webGis.entity;

import com.piesat.common.anno.Description;
import lombok.Data;

@Data
public class WebGisConfig {

    @Description("webgis表格显示配置")
    private TableConfig tableConfig;

    @Description("webgis页面显示配置")
    private PlaneConfig planeConfig;
}
