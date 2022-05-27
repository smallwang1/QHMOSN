package com.piesat.busiclogic.busic.statistics.service.mapper;

import com.piesat.busiclogic.busic.statistics.service.dto.CensusDto;
import com.piesat.common.statistics.entity.MosnCensus;

import java.util.List;

public interface MosnCensusMapper {

    List<CensusDto> topMenuList(String censusType);

    List<MosnCensus> selectCensusList(MosnCensus mosnCensus);
}
