package com.piesat.site.datalist.service.impl;

import com.piesat.site.datalist.service.IPbTqDataDefService;
import com.piesat.site.datalist.service.entity.PbTqDataDef;
import com.piesat.site.datalist.service.mapper.PbTqDataDefMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pbTqDataDefService")
public class PbTqDataDefServiceImpl implements IPbTqDataDefService {

    private static final Logger logger = LoggerFactory.getLogger(PbTqDataDefServiceImpl.class);

    @Autowired
    private PbTqDataDefMapper pbTqDataDefMapper;

    @Override
    public List<PbTqDataDef> selectDataDefAll() {
        return pbTqDataDefMapper.selectDataDefAll();
    }
}
