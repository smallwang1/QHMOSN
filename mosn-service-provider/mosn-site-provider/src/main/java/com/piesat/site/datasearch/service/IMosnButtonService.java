package com.piesat.site.datasearch.service;

import com.piesat.site.datasearch.service.entity.MosnButton;

import java.util.List;

public interface IMosnButtonService {

    List<MosnButton> selectButtonByIds(Long[] buttonIds);
}
