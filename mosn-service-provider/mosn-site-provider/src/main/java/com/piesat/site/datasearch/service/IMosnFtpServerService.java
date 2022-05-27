package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.dto.FtpServerDto;
import com.piesat.site.datasearch.service.entity.MosnFtpServer;

import java.util.List;

public interface IMosnFtpServerService {

    /**
     * 根据用户Id查询服务集合
     * @param userId
     * @return
     */
    List<FtpServerDto> selectListByUserId(Long userId);

    /**
     * 根据ids查询服务集合
     * @param serverIds
     * @return
     */
    List<MosnFtpServer> selectFtpServerByIds(Long[] serverIds);
}
