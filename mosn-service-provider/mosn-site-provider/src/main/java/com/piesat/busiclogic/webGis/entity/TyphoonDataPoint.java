package com.piesat.busiclogic.webGis.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class TyphoonDataPoint {

    private Double latitude;

    private Double longitude;

    private Double power;

    private String time;

    private Double speed;

    private Double move_speed;

    private String move_dir;

    private String remark;

    private Double pressure;

    private String strong;

    private List<Map<String,List<TyphoonDataPoint>>> forecast = new ArrayList<>();

    private List<TyphoonDataPoint> forecast_middle = new ArrayList<>();

    private Double radius7 = 0d;

    private Double radius10 = 0d;

    private Double radius12 = 0d;

    private Map<String,Double> radius7_quad = new HashMap(){{
        put("se", 0);
        put("sw", 0);
        put("ne", 0);
        put("nw", 0);
    }};
    private Map<String,Double> radius10_quad = new HashMap(){{
        put("se", 0);
        put("sw", 0);
        put("ne", 0);
        put("nw", 0);
    }};

    private Map<String,Double> radius12_quad = new HashMap(){{
        put("se", 0);
        put("sw", 0);
        put("ne", 0);
        put("nw", 0);
    }};
}
