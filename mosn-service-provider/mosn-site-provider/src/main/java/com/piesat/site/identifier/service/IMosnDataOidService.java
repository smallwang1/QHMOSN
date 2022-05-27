package com.piesat.site.identifier.service;

import com.piesat.site.identifier.service.entity.MosnDataOidEntity;

import java.util.List;

public interface IMosnDataOidService {

    void batchInsertDataOid(List<MosnDataOidEntity> dataOidEntityList);

    List<MosnDataOidEntity> selectDataOidList(MosnDataOidEntity dataOidEntity);
}
