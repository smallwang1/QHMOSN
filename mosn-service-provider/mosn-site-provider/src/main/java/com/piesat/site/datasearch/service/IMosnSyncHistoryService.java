package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.entity.MosnSyncHistory;

import java.util.List;

public interface IMosnSyncHistoryService {

    Integer insertSyncHistory(MosnSyncHistory mosnSyncHistory);

    List<MosnSyncHistory> selectSyncHisList(String orderNo);
}
