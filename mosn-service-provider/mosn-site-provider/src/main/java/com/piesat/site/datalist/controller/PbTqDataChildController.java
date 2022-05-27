package com.piesat.site.datalist.controller;

import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datalist.service.IPbTqDataChildService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dataChild")
public class PbTqDataChildController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PbTqDataChildController.class);

    @Autowired
    private IPbTqDataChildService pbTqDataChildService;

    @GetMapping("/listData")
    public Wrapper listData(@RequestParam("dataClassId") String dataClassId) {
        return WrapMapper.ok(pbTqDataChildService.selectDataChildById(dataClassId));
    }
}
