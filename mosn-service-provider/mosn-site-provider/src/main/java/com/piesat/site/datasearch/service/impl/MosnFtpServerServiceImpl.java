package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnFtpServerService;
import com.piesat.site.datasearch.service.dto.FtpServerDto;
import com.piesat.site.datasearch.service.entity.MosnFtpServer;
import com.piesat.site.datasearch.service.mapper.MosnFtpServerMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("mosnFtpServerService")
public class MosnFtpServerServiceImpl implements IMosnFtpServerService {

    private static final Logger logger = LoggerFactory.getLogger(MosnFtpServerServiceImpl.class);

    @Autowired
    private MosnFtpServerMapper mosnFtpServerMapper;

    @Override
    public List<FtpServerDto> selectListByUserId(Long userId) {
        List<Long> idList = mosnFtpServerMapper.selectIdsByUserId(userId);
        if (StringUtils.isEmpty(idList)) return null;
        List<MosnFtpServer> ftpServerList = this.selectFtpServerByIds(idList.toArray(new Long[0]));
        return BeanUtils.convertList2List(ftpServerList, FtpServerDto.class);
    }

    @Override
    public List<MosnFtpServer> selectFtpServerByIds(Long[] serverIds) {
        return mosnFtpServerMapper.selectFtpServerByIds(serverIds);
    }
}
