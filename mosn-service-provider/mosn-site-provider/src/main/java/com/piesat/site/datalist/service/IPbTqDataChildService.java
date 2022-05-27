package com.piesat.site.datalist.service;

import com.piesat.site.datalist.service.entity.PbTqDataChild;

import java.util.List;

public interface IPbTqDataChildService {

    List<PbTqDataChild> selectDataChildById(String dataClassId);
}
