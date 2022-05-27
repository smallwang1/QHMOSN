package com.piesat.site.identifier.service.mapper;

import com.piesat.site.identifier.service.dto.ServiceOidDto;
import com.piesat.site.identifier.service.entity.MosnServiceOidEntity;
import com.piesat.site.identifier.service.vo.ServiceOidVo;

import java.util.List;
import java.util.Map;

public interface MosnServiceOidMapper {

    Integer insertServiceOid(MosnServiceOidEntity serviceOidEntity);

    List<MosnServiceOidEntity> selectServiceOidList(ServiceOidVo serviceOid);

    Integer updateServiceOid(MosnServiceOidEntity serviceOidEntity);

    List<Map<String, Object>> selectApplyData(ServiceOidDto serviceOid);

    MosnServiceOidEntity selectServiceOidById(String busiId);
}
