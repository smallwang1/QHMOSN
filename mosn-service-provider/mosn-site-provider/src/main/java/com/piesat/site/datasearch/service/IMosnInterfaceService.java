package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.InterfaceDto;
import com.piesat.site.datasearch.service.entity.MosnInterface;

import java.util.List;
import java.util.Map;

public interface IMosnInterfaceService {

    List<InterfaceDto> generatorInterface(String resourceCode);

    List<MosnInterface> selectInterfaceList(MosnInterface mosnInterface);

    Map<String, Object> selectInfoById(Long interfaceId);
}
