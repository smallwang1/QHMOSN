package com.piesat.busiclogic.webGis.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GrdBean {

    private String ncols;

    private String nrows;

    private String x0;

    private String y0;

    private String x1;

    private String y1;

    private String cellsize;

    private String nodata_value;

    // 处理后的二维数组
    private float[][] data;

    // 原始数据
    private List<Float> eleList = new ArrayList<>();

    // 带位置信息的数据
    private List<GrdElement> grdEleData = new ArrayList<>();
}
