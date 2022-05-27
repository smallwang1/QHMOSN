package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.SyncFtpDto;
import com.piesat.site.datasearch.service.entity.MosnSyncFtp;
import com.piesat.site.datasearch.service.vo.SyncDetailVo;
import com.piesat.site.datasearch.service.vo.SyncVo;

import java.util.List;

public interface IMosnSyncFtpService {

    void generatorSyncFtp(SyncFtpDto syncFtpDto);

    List<SyncVo> generatorSync();

    Integer updateSyncFtp(MosnSyncFtp mosnSyncFtp);

    List<MosnSyncFtp> selectSyncFtpByorderNo(String orderNo);

    SyncDetailVo selectSyncDetailByorderNo(String orderNo);
}
