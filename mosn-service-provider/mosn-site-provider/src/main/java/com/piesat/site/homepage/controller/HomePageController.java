package com.piesat.site.homepage.controller;

import com.piesat.common.anno.Description;
import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.utils.RequestUtil;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.homepage.service.HomePageService;
import com.piesat.site.homepage.service.entity.HomeColumnData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author Thomas 2022/1/5 22:49
 * The world of programs is a wonderful world
 */
@RestController
@RequestMapping("/homepage")
public class HomePageController extends BaseController {
    @Autowired
    private HomePageService homePageServiceImpl;

    @Description("查询用户栏目接口")
    @RequestMapping(value = "/queryHomeColumnList", method = RequestMethod.GET)
    public Wrapper queryColumnList(HttpServletRequest request) {
        try {
            LoginAuthDto loginAuthDto = this.getLoginAuthDto();
            String userId = String.valueOf(loginAuthDto.getUserId());
            if (userId.equals("-1")){
                userId = RequestUtil.getRemoteAddr(request);
            }
            Map<String,List<HomeColumnData>> queryColumnMap = homePageServiceImpl.queryColumnList(userId);
            return WrapMapper.ok(queryColumnMap);
        } catch (Exception e) {
            return WrapMapper.error("查询失败");
        }
    }


    @Description("修改用户栏目接口")
    @RequestMapping(value = "/editHomeColumn", method = RequestMethod.POST)
    public Wrapper editColumn(String columnId, HttpServletRequest request) {
        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        String userId = String.valueOf(loginAuthDto.getUserId());
        if (userId.equals("-1")){
            userId = RequestUtil.getRemoteAddr(request);
        }
        System.out.println("接收参数"+columnId);
        Map<String,String> reqMap = new HashMap<>();
        reqMap.put("userId",userId);
        reqMap.put("columns",columnId);
        boolean editHomeColumn = homePageServiceImpl.editHomeColumn(reqMap);
        if (editHomeColumn){
            return WrapMapper.ok();
        }else {
            return WrapMapper.error("修改失败");
        }
    }


    @Description("默认配置接口")
    @RequestMapping(value = "/defaultSetting", method = RequestMethod.GET)
    public Wrapper defaultSetting( HttpServletRequest request) {
        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        String userId = String.valueOf(loginAuthDto.getUserId());
        if (userId.equals("-1")){
            userId = RequestUtil.getRemoteAddr(request);
        }
        Map<String,List<HomeColumnData>> queryColumnList = homePageServiceImpl.defaultSetting(userId);
        return WrapMapper.ok(queryColumnList);
    }

    @Description("网站地图")
    @RequestMapping(value = "/getWebsiteMap", method = RequestMethod.GET)
    public Wrapper getWebsiteMap() {
        try {
            LoginAuthDto loginAuthDto = this.getLoginAuthDto();
            Set<Long> roles = loginAuthDto.getRoles();
            Map<String,List<Map<String, Object>>> queryColumnMap = homePageServiceImpl.getWebsiteMap(roles);
            return WrapMapper.ok(queryColumnMap);
        } catch (Exception e) {
            return WrapMapper.error("查询失败");
        }
    }
}
