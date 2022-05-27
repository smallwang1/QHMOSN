package com.piesat.site.datalist.service.mapper;

import com.piesat.site.datalist.service.entity.PbTqDataChild;

import java.util.List;

public interface PbTqDataChildMapper {

    List<PbTqDataChild> selectDataChildById(String dataClassId);
}
