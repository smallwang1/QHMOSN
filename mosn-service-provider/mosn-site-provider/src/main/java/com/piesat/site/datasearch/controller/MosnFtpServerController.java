package com.piesat.site.datasearch.controller;

import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.IMosnFtpServerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ftpServer")
public class MosnFtpServerController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnFtpServerController.class);

    @Autowired
    private IMosnFtpServerService mosnFtpServerService;

    @GetMapping("/list/{userId}")
    public Wrapper list(@PathVariable("userId") Long userId) {
//        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        return WrapMapper.ok(mosnFtpServerService.selectListByUserId(userId));
    }
}
