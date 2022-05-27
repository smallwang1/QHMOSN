package com.piesat.busiclogic.busic.statistics.service;

import com.piesat.busiclogic.busic.statistics.service.dto.CensusDto;

import java.util.List;

public interface MosnCensusService {

    /**
     * 统计栏目访问排行榜
     * @return
     */
    List<CensusDto> topList(String censusType);
}
