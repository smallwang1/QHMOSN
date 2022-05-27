package com.piesat.busiclogic.busic.menuset.controller;


import com.piesat.busiclogic.busic.menuset.service.MenuSetService;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/MenuSet")
public class MenuSetProController extends BaseController {

    @Autowired
    private MenuSetService menuSetService;

    @RequestMapping(value = "/getMenuData",produces = {"application/json;charset=UTF-8"})
    public Wrapper getMenuSetData(HttpServletRequest request, @RequestParam String code) {
        try {
            List<Map<String, Object>> list = menuSetService.getMenuSetProData(code,getLoginAuthDto());
            return WrapMapper.ok(list);
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getMenuDataNoLogin",produces = {"application/json;charset=UTF-8"})
    public Wrapper getMenuDataNoLogin(HttpServletRequest request) {
        try {
            List<Map<String, Object>> list = menuSetService.getMenuSetDataByTree("",getLoginAuthDto());
            return WrapMapper.ok(list);
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getMenuDataByTree",produces = {"application/json;charset=UTF-8"})
    public Wrapper getMenuSetDataByTree(HttpServletRequest request) {
        try {
            List<Map<String, Object>> list = menuSetService.getMenuSetDataByTree("",getLoginAuthDto());
            return WrapMapper.ok(list);
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getMenuDataNoRule",produces = {"application/json;charset=UTF-8"})
    public Wrapper getMenuDataNoRule(HttpServletRequest request) {
        List<Map<String, Object>> list = menuSetService.getMenuDataNoRule();
        return WrapMapper.ok(list);
    }
}
