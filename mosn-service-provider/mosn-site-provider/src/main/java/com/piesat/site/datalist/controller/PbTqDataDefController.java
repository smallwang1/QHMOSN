package com.piesat.site.datalist.controller;

import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datalist.service.IPbTqDataDefService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dataDef")
public class PbTqDataDefController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(PbTqDataDefController.class);

    @Autowired
    private IPbTqDataDefService pbTqDataDefService;

    @GetMapping("/listAll")
    public Wrapper listAll() {
        return WrapMapper.ok(pbTqDataDefService.selectDataDefAll());
    }
}
