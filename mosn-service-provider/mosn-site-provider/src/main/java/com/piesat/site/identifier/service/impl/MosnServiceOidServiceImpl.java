package com.piesat.site.identifier.service.impl;

import com.oid.active.config.QueueProducer;
import com.oid.active.entity.AjaxRes;
import com.oid.active.entity.DataOidEntity;
import com.oid.active.entity.ServiceOidEntity;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import com.piesat.site.identifier.service.IMosnDataOidService;
import com.piesat.site.identifier.service.IMosnServiceOidService;
import com.piesat.site.identifier.service.dto.DataOidDto;
import com.piesat.site.identifier.service.dto.ServiceOidDto;
import com.piesat.site.identifier.service.entity.MosnDataOidEntity;
import com.piesat.site.identifier.service.entity.MosnServiceOidEntity;
import com.piesat.site.identifier.service.mapper.MosnServiceOidMapper;
import com.piesat.site.identifier.service.vo.ServiceOidVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("mosnServiceOidService")
public class MosnServiceOidServiceImpl implements IMosnServiceOidService {

    private static final Logger logger = LoggerFactory.getLogger(MosnServiceOidServiceImpl.class);

    @Autowired
    private MosnServiceOidMapper serviceOidMapper;
    @Autowired
    private IMosnDataOidService mosnDataOidService;

    @Override
    public List<MosnServiceOidEntity> selectServiceOidList(ServiceOidVo serviceOid) {
        return serviceOidMapper.selectServiceOidList(serviceOid);
    }

    @Override
    public Integer insertServiceOid(MosnServiceOidEntity serviceOidEntity) {
        serviceOidEntity.setStatus(Constants.OID_CREATE); // TODO 待申请
        return serviceOidMapper.insertServiceOid(serviceOidEntity);
    }

    @Transactional(rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public void updateServiceOid(ServiceOidDto serviceOid) {
        serviceOidMapper.updateServiceOid(BeanUtils.convertObj2Obj(serviceOid, MosnServiceOidEntity.class));
        List<DataOidDto> source = serviceOid.getSource();
        List<MosnDataOidEntity> dataOidEntityList = BeanUtils.convertList2List(source, MosnDataOidEntity.class);
        mosnDataOidService.batchInsertDataOid(dataOidEntityList);
    }

    @Override
    public void dataApply(ServiceOidDto serviceOid) {
        serviceOid = this.generatorData(serviceOid);
        ServiceOidEntity entity = BeanUtils.convertObj2Obj(serviceOid, ServiceOidEntity.class);
        List<DataOidEntity> source = BeanUtils.convertList2List(serviceOid.getSource(), DataOidEntity.class);
        entity.setSource(source);
        // TODO 发送消息
        QueueProducer producer = new QueueProducer();
        producer.sendMessage(com.oid.active.util.BeanUtils.beanToMqStr(entity));
        serviceOid.setStatus(Constants.OID_CREATEING); // TODO 申请中
        serviceOidMapper.updateServiceOid(BeanUtils.convertObj2Obj(serviceOid, MosnServiceOidEntity.class));
    }

    @Override
    public void parseResult(AjaxRes ajaxRes) {
        String code = ajaxRes.getCode();
        MosnServiceOidEntity serviceOidEntity = serviceOidMapper.selectServiceOidById(ajaxRes.getBusiId());
        if (StringUtils.isNotEmpty(code) && "200".equals(code)) {
            serviceOidEntity.setStatus(Constants.OID_SUCCESS); // TODO 申请通过
            serviceOidEntity.setServiceId(ajaxRes.getResult());
        } else {
            serviceOidEntity.setStatus(Constants.OID_FAIL); // TODO 申请失败
        }
        serviceOidEntity.setRemark(ajaxRes.getMsg());
        serviceOidMapper.updateServiceOid(serviceOidEntity);
    }

    @Override
    public MosnServiceOidEntity selectServiceOidById(String orderNo) {
        return serviceOidMapper.selectServiceOidById(orderNo);
    }

    public ServiceOidDto generatorData(ServiceOidDto serviceOid) {
        MosnServiceOidEntity serviceOidEntity = serviceOidMapper.selectServiceOidById(serviceOid.getBusiId());
        List<DataOidDto> dataOidDtoList = serviceOid.getSource();
        if (null != serviceOidEntity && StringUtils.isNotEmpty(dataOidDtoList)) {
            serviceOid = BeanUtils.convertObj2Obj(serviceOidEntity, ServiceOidDto.class);
            serviceOid.setSource(dataOidDtoList);
        }
        return serviceOid;
    }
}
