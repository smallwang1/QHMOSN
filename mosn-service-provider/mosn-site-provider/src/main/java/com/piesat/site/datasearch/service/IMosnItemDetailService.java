package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.ItemDetailDto;

import java.util.List;

public interface IMosnItemDetailService {

    List<ItemDetailDto> selectDetailByItemIds(Long[] itemIds);
}
