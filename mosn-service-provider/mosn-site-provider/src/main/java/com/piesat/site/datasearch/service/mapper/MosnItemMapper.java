package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnItem;

import java.util.List;

public interface MosnItemMapper {

    List<MosnItem> selectItemByIds(Long[] itemIds);
}
