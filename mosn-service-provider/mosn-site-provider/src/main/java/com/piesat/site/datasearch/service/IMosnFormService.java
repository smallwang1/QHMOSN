package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.ElementDto;
import com.piesat.site.datasearch.service.dto.FormDto;
import com.piesat.site.datasearch.service.dto.InterfaceDto;
import com.piesat.site.datasearch.service.dto.TitleDto;

import java.util.List;

public interface IMosnFormService {

    List<FormDto> generatorForm(Long[] formIds, List<InterfaceDto> interfaceList, List<TitleDto> titleList);
}
