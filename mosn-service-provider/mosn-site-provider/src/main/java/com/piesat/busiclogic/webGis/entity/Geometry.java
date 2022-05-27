package com.piesat.busiclogic.webGis.entity;

import lombok.Data;

@Data
public class Geometry {

    double[] coordinates = new double[2];

    private String type = "Point";

}
