package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnSyncHistoryService;
import com.piesat.site.datasearch.service.entity.MosnSyncHistory;
import com.piesat.site.datasearch.service.mapper.MosnSyncHistoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mosnSyncHistoryService")
public class MosnSyncHistoryServiceImpl implements IMosnSyncHistoryService {

    private static final Logger logger = LoggerFactory.getLogger(MosnSyncHistoryServiceImpl.class);

    @Autowired
    private MosnSyncHistoryMapper mosnSyncHistoryMapper;

    @Override
    public Integer insertSyncHistory(MosnSyncHistory mosnSyncHistory) {
        return mosnSyncHistoryMapper.insertSyncHistory(mosnSyncHistory);
    }

    @Override
    public List<MosnSyncHistory> selectSyncHisList(String orderNo) {
        return mosnSyncHistoryMapper.selectSyncHisList(orderNo);
    }
}
