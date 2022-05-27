package com.piesat.site.identifier.service.mapper;

import com.piesat.site.identifier.service.entity.MosnDataOidEntity;

import java.util.List;

public interface MosnDataOidMapper {

    Integer insertDataOid(MosnDataOidEntity mosnDataOidEntity);

    List<MosnDataOidEntity> selectDataOidList(MosnDataOidEntity dataOidEntity);
}
