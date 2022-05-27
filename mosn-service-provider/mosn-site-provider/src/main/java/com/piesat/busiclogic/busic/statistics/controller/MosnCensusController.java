package com.piesat.busiclogic.busic.statistics.controller;

import com.piesat.busiclogic.busic.statistics.service.MosnCensusService;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.statistics.annotation.Statistics;
import com.piesat.common.statistics.constant.CensusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/cencus")
public class MosnCensusController extends BaseController {

    @Autowired
    private MosnCensusService mosnCensusService;

    @GetMapping("/noAuth/menuSet")
    @Statistics(censusType = CensusType.MENU, remark = "菜单栏目访问统计")
    public Wrapper censusMenu(@RequestParam(required = false) Map<String, Object> paramMap) {
        return WrapMapper.ok();
    }

    @GetMapping("/noAuth/product")
    @Statistics(censusType = CensusType.SERVER, remark = "产品服务访问统计")
    public Wrapper censusServer(@RequestParam(required = false) Map<String, Object> paramMap) {
        return WrapMapper.ok();
    }

    @GetMapping("/noAuth/top")
    public Wrapper top(@RequestParam("censusType") String censusType) {
        return WrapMapper.ok(mosnCensusService.topList(censusType));
    }
}
