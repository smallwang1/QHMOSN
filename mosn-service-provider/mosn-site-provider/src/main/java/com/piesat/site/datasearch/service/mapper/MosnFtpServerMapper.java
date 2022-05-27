package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnFtpServer;

import java.util.List;

public interface MosnFtpServerMapper {

    /**
     * 根据用户Id获取服务Id集合
     * @param userId
     * @return
     */
    List<Long> selectIdsByUserId(Long userId);

    /**
     * 根据ids查询服务集合
     * @param serverIds
     * @return
     */
    List<MosnFtpServer> selectFtpServerByIds(Long[] serverIds);
}
