package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnElementService;
import com.piesat.site.datasearch.service.dto.ElementDto;
import com.piesat.site.datasearch.service.entity.MosnElement;
import com.piesat.site.datasearch.service.mapper.MosnElementMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mosnElementService")
public class MosnElementServiceImpl implements IMosnElementService {

    private static final Logger logger = LoggerFactory.getLogger(MosnElementServiceImpl.class);

    @Autowired
    private MosnElementMapper mosnElementMapper;

    @Override
    public List<ElementDto> selectElementByIds(Long[] elementIds) {
        if (StringUtils.isEmpty(elementIds)) return null;
        List<MosnElement> elementList = mosnElementMapper.selectElementByIds(elementIds);
        return BeanUtils.convertList2List(elementList, ElementDto.class);
    }
}
