package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnButton;

import java.util.List;

public interface MosnButtonMapper {

    List<MosnButton> selectButtonByIds(Long[] buttonIds);
}
