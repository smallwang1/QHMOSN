package com.piesat.busiclogic.webGis.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Transparent {

    private List bbox = new ArrayList();

    private String forcasedata ;

    private String imgBase64;

    private String DataTime;

    private String url;

}
