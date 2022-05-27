package com.piesat.site.identifier.service.impl;

import com.piesat.site.datasearch.service.util.StringUtils;
import com.piesat.site.identifier.service.IMosnDataOidService;
import com.piesat.site.identifier.service.entity.MosnDataOidEntity;
import com.piesat.site.identifier.service.mapper.MosnDataOidMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mosnDataOidService")
public class MosnDataOidServiceImpl implements IMosnDataOidService {

    private static final Logger logger = LoggerFactory.getLogger(MosnDataOidServiceImpl.class);

    @Autowired
    private MosnDataOidMapper dataOidMapper;

    @Override
    public void batchInsertDataOid(List<MosnDataOidEntity> dataOidEntityList) {
        if (StringUtils.isNotEmpty(dataOidEntityList)) {
            for (MosnDataOidEntity mosnDataOidEntity : dataOidEntityList) {
                dataOidMapper.insertDataOid(mosnDataOidEntity);
            }
        }
    }

    @Override
    public List<MosnDataOidEntity> selectDataOidList(MosnDataOidEntity dataOidEntity) {
        return dataOidMapper.selectDataOidList(dataOidEntity);
    }
}
