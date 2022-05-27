package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.ElementDto;
import com.piesat.site.datasearch.service.dto.TitleDto;

import java.util.List;

public interface IMosnTitleService {

    List<TitleDto> generatorTitle(Long[] titleIds, List<ElementDto> elementList);
}
