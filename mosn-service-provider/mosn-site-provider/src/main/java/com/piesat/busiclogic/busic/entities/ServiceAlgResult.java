package com.piesat.busiclogic.busic.entities;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class ServiceAlgResult {

    private String field ;

    private String fileName;

    private List<Map<String,Object>> dataList = new ArrayList<>();

}
