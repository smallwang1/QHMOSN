package com.piesat.site.datasearch.controller;

import com.alibaba.fastjson.JSON;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.IRemoteObsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/obs")
public class RemoteObsController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(RemoteObsController.class);

    @Autowired
    private IRemoteObsService remoteObsService;

    @PostMapping("/data/{interfaceId}")
    public Wrapper queryObs(@RequestBody Map<String, Object> paramMap, @PathVariable("interfaceId") Long interfaceId) {
        String result = remoteObsService.queryObsData(paramMap, interfaceId);
        return WrapMapper.ok(JSON.parse(result));
    }

    @PostMapping("/data")
    public Wrapper queryObs(@RequestBody Map<String, Object> paramMap) {
        String result = remoteObsService.queryObsData(paramMap);
        return WrapMapper.ok(JSON.parse(result));
    }

    @PostMapping("/cmadaas")
    public Wrapper queryInventory(@RequestBody Map<String, Object> paramMap) {
        String result = remoteObsService.getInventoryForCmadaas(paramMap);
        return WrapMapper.ok(JSON.parse(result));
    }
}