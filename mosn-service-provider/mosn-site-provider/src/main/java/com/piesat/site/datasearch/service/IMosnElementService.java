package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.ElementDto;

import java.util.List;

public interface IMosnElementService {

    List<ElementDto> selectElementByIds(Long[] elementIds);
}
