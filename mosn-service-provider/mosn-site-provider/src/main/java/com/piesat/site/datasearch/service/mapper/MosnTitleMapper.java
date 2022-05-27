package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnTitle;

import java.util.List;

public interface MosnTitleMapper {

    List<MosnTitle> selectTitleByIds(Long[] titleIds);
}
