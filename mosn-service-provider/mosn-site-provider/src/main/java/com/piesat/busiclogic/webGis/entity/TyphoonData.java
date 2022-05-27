package com.piesat.busiclogic.webGis.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class TyphoonData {

    private String ename ;

    private String name;

    private String begin_time;

    private String end_time;

    private String is_current = "0";

    private String tfbh;

    private List<TyphoonDataPoint> points = new ArrayList<>();
}
