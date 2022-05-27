package com.piesat.busiclogic.webGis.web;


import com.piesat.busiclogic.webGis.handle.WBConfigService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

@RestController
@CrossOrigin
@RequestMapping("/noAuth/wbConfig")
public class WBConfigController extends BaseController {


    @Autowired
    private WBConfigService wbConfigService;

    @Description("获取webgis图层树信息")
    @RequestMapping(value = "/point", method = RequestMethod.GET)
    public Wrapper getPointTree(){
        return this.handleResult(wbConfigService.getPointTree());
    }


    @Description("获取webgis图层树信息")
    @RequestMapping(value = "/ptdata", method = RequestMethod.GET)
    public Wrapper getConfigData(@NotEmpty(message = "id 不能为空") String id){
        return WrapMapper.ok(wbConfigService.getConfigData(id));
    }

    @Description("获取安徽站点信息")
    @RequestMapping(value = "/getAhStation", method = RequestMethod.GET)
    public Wrapper getAhStation(){
        return WrapMapper.ok(wbConfigService.getAhStation());
    }
}
