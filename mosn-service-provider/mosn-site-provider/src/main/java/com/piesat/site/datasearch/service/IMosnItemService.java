package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.ItemDto;

import java.util.List;

public interface IMosnItemService {

    List<ItemDto> generatorItem(Long[] itemIds, Boolean isReueired);

    List<ItemDto> selectItemByIds(Long[] itemIds);
}
