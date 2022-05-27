package com.piesat.site.datalist.service.impl;

import com.piesat.site.datalist.service.IPbTqDataChildService;
import com.piesat.site.datalist.service.entity.PbTqDataChild;
import com.piesat.site.datalist.service.mapper.PbTqDataChildMapper;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("pbTqDataChildService")
public class PbTqDataChildServiceImpl implements IPbTqDataChildService {

    private static final Logger logger = LoggerFactory.getLogger(PbTqDataChildServiceImpl.class);

    @Autowired
    private PbTqDataChildMapper pbTqDataChildMapper;

    @Override
    public List<PbTqDataChild> selectDataChildById(String dataClassId) {
        StringUtils.notEmpty(dataClassId, "资料大类编码不能为空");
        return pbTqDataChildMapper.selectDataChildById(dataClassId);
    }
}
