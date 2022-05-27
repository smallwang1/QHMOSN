package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnItemDetailService;
import com.piesat.site.datasearch.service.dto.ItemDetailDto;
import com.piesat.site.datasearch.service.entity.MosnItemDetail;
import com.piesat.site.datasearch.service.mapper.MosnItemDetailMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mosnItemDetailService")
public class MosnItemDetailServiceImpl implements IMosnItemDetailService {

    private static final Logger logger = LoggerFactory.getLogger(MosnItemDetailServiceImpl.class);

    @Autowired
    private MosnItemDetailMapper mosnItemDetailMapper;

    @Override
    public List<ItemDetailDto> selectDetailByItemIds(Long[] itemIds) {
        List<MosnItemDetail> detailList = mosnItemDetailMapper.selectDetailByItemIds(itemIds);
        if (StringUtils.isEmpty(detailList)) return null;
        return BeanUtils.convertList2List(detailList, ItemDetailDto.class);
    }
}
