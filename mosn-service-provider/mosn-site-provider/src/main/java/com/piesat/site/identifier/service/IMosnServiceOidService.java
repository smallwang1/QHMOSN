package com.piesat.site.identifier.service;

import com.oid.active.entity.AjaxRes;
import com.piesat.site.identifier.service.dto.ServiceOidDto;
import com.piesat.site.identifier.service.entity.MosnServiceOidEntity;
import com.piesat.site.identifier.service.vo.ServiceOidVo;

import java.util.List;

public interface IMosnServiceOidService {

    Integer insertServiceOid(MosnServiceOidEntity serviceOidEntity);

    List<MosnServiceOidEntity> selectServiceOidList(ServiceOidVo serviceOid);

    void updateServiceOid(ServiceOidDto serviceOid);

    void dataApply(ServiceOidDto serviceOid);

    void parseResult(AjaxRes ajaxRes);

    MosnServiceOidEntity selectServiceOidById(String orderNo);
}
