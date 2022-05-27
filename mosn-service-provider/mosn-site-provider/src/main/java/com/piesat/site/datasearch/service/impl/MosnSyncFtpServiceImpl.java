package com.piesat.site.datasearch.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.piesat.site.datasearch.service.IMosnSyncFtpService;
import com.piesat.site.datasearch.service.dto.FtpServerDto;
import com.piesat.site.datasearch.service.dto.SyncFtpDto;
import com.piesat.site.datasearch.service.entity.MosnSyncFtp;
import com.piesat.site.datasearch.service.mapper.MosnSyncFtpMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import com.piesat.site.datasearch.service.vo.SyncDetailVo;
import com.piesat.site.datasearch.service.vo.SyncVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("mosnSyncFtpService")
public class MosnSyncFtpServiceImpl implements IMosnSyncFtpService {

    private static final Logger logger = LoggerFactory.getLogger(MosnResourceServiceImpl.class);

    @Autowired
    private MosnSyncFtpMapper mosnSyncFtpMapper;

    @Override
    public void generatorSyncFtp(SyncFtpDto syncFtpDto) {
        MosnSyncFtp syncFtp = BeanUtils.convertObj2Obj(syncFtpDto, MosnSyncFtp.class);
        if ("1".equals(syncFtpDto.getIsSync())) {
            syncFtp.setStatus("1");
            mosnSyncFtpMapper.insertSyncFtp(syncFtp);
        } else {
            List<FtpServerDto> ftpServerDtos = syncFtpDto.getSyncServer();
            if (StringUtils.isNotEmpty(ftpServerDtos)){
                ftpServerDtos.forEach(ftpServerDto -> {
                    syncFtp.setSyncServerId(Long.valueOf(ftpServerDto.getServerId()));
                    syncFtp.setSyncServerPath(ftpServerDto.getFilePath());
                    mosnSyncFtpMapper.insertSyncFtp(syncFtp);
                });
            }
        }
    }

    @Override
    public List<SyncVo> generatorSync() {
        return mosnSyncFtpMapper.generatorSync();
    }

    @Override
    public Integer updateSyncFtp(MosnSyncFtp mosnSyncFtp) {
        return mosnSyncFtpMapper.updateSyncFtp(mosnSyncFtp);
    }

    @Override
    public List<MosnSyncFtp> selectSyncFtpByorderNo(String orderNo) {
        return mosnSyncFtpMapper.selectSyncFtpByorderNo(orderNo);
    }

    @Override
    public SyncDetailVo selectSyncDetailByorderNo(String orderNo) {
        List<MosnSyncFtp> mosnSyncFtpList = mosnSyncFtpMapper.selectSyncFtpByorderNo(orderNo);
        if (StringUtils.isEmpty(mosnSyncFtpList)) return null;

        SyncDetailVo syncDetailVo = new SyncDetailVo();
        MosnSyncFtp syncFtp = mosnSyncFtpList.get(0);
        syncDetailVo.setIsSync(syncFtp.getStatus());
        syncDetailVo.setSyncType(syncFtp.getSyncType());
        syncDetailVo.setSyncParam(JSONObject.parseObject(syncFtp.getSyncParam()));
        List<FtpServerDto> ftpServerDtoList = new ArrayList<>();
        for (MosnSyncFtp mosnSyncFtp : mosnSyncFtpList) {
            FtpServerDto ftpServerDto = new FtpServerDto();
            ftpServerDto.setServerId(mosnSyncFtp.getSyncServerId());
            ftpServerDto.setFilePath(mosnSyncFtp.getSyncServerPath());
            ftpServerDtoList.add(ftpServerDto);
        }
        syncDetailVo.setSyncServer(ftpServerDtoList);

        return syncDetailVo;
    }
}
