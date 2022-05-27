package com.piesat.busiclogic.busic.statistics.service.impl;

import com.alibaba.fastjson.JSON;
import com.piesat.busiclogic.busic.statistics.service.MosnCensusService;
import com.piesat.busiclogic.busic.statistics.service.dto.CensusDto;
import com.piesat.busiclogic.busic.statistics.service.mapper.MosnCensusMapper;
import com.piesat.common.statistics.entity.MosnCensus;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MosnCensusServiceImpl implements MosnCensusService {

    @Autowired
    private MosnCensusMapper mosnCensusMapper;

    @Override
    public List<CensusDto> topList(String censusType) {
        List<CensusDto> censusDtoList = mosnCensusMapper.topMenuList(censusType);
        if (StringUtils.isEmpty(censusDtoList)) return null;

        for (CensusDto censusDto : censusDtoList) {
            MosnCensus mosnCensus = new MosnCensus();
            mosnCensus.setCensusId(censusDto.getCensusId());
            List<MosnCensus> censusList = mosnCensusMapper.selectCensusList(mosnCensus);
            if (StringUtils.isEmpty(censusList)) continue;
            censusDto.setRequestParam(JSON.parse(String.valueOf(censusList.get(0).getRequestParam())));
        }
        return censusDtoList;
    }
}
