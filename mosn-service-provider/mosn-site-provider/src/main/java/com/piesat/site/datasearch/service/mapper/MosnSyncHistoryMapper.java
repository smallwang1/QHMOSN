package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnSyncHistory;

import java.util.List;

public interface MosnSyncHistoryMapper {

    Integer insertSyncHistory(MosnSyncHistory mosnSyncHistory);

    List<MosnSyncHistory> selectSyncHisList(String orderNo);
}
