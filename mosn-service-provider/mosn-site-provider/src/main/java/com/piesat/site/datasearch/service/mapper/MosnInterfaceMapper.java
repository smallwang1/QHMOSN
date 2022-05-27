package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnInterface;

import java.util.List;
import java.util.Map;

public interface MosnInterfaceMapper {

    List<MosnInterface> selectInterfaceList(MosnInterface mosnInterface);

    Map<String, Object> selectInfoById(Long interfaceId);
}
