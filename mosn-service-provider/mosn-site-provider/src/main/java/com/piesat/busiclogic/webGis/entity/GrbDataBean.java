package com.piesat.busiclogic.webGis.entity;


import lombok.Data;

@Data
public class GrbDataBean {

    private int xsize;

    private int ysize;

    private float xstep;

    private float ystep;

    private String[] bbox = new String[4];

    private float[][] data;
}
