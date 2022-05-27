package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnSyncFtp;
import com.piesat.site.datasearch.service.vo.SyncVo;

import java.util.List;

public interface MosnSyncFtpMapper {

    Integer insertSyncFtp(MosnSyncFtp syncFtp);

    List<SyncVo> generatorSync();

    Integer updateSyncFtp(MosnSyncFtp mosnSyncFtp);

    List<MosnSyncFtp> selectSyncFtpByorderNo(String orderNo);
}
