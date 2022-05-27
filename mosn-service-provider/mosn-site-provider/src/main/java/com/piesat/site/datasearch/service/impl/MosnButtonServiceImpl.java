package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnButtonService;
import com.piesat.site.datasearch.service.entity.MosnButton;
import com.piesat.site.datasearch.service.mapper.MosnButtonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mosnButtonService")
public class MosnButtonServiceImpl implements IMosnButtonService {

    @Autowired
    private MosnButtonMapper mosnButtonMapper;

    @Override
    public List<MosnButton> selectButtonByIds(Long[] buttonIds) {
        return mosnButtonMapper.selectButtonByIds(buttonIds);
    }
}
