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

/**
 * @author sjc
 * @date 2020/7/30
 */
@RestController
@CrossOrigin
@RequestMapping("/noAuth/MenuSet")
public class MenuSetController extends BaseController {

    @Autowired
    private MenuSetService menuSetService;

    @RequestMapping(value = "/getMenuData",produces = {"application/json;charset=UTF-8"})
    public Wrapper getMenuSetData(HttpServletRequest request, @RequestParam String code) {
        try{
            List<Map<String, Object>> list = menuSetService.getMenuSetData(code,null);
            return WrapMapper.ok(list);
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @RequestMapping(value = "/getCodeData",produces = {"application/json;charset=UTF-8"})
    public Wrapper getCodeData(HttpServletRequest request, @RequestParam String code) {
        List<Map<String, Object>> list = menuSetService.getCodeData(code);
        return WrapMapper.ok(list);
    }
}
