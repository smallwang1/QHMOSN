package com.piesat.busiclogic.webGis.entity;

import lombok.Data;

@Data
public class ElementCell {

    private Geometry geometry;

    private String type = "Feature";

    private Properties properties;

}
