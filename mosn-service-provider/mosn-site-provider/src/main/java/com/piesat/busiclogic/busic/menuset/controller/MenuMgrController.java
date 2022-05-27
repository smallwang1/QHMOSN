package com.piesat.busiclogic.busic.menuset.controller;

import com.piesat.busiclogic.busic.menuset.entity.MenuInfo;
import com.piesat.busiclogic.busic.menuset.service.MenuMgrService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 菜单管理controller
 */
@RestController
@CrossOrigin
@RequestMapping("/menuMgr")
public class MenuMgrController extends BaseController {

    @Autowired
    private MenuMgrService menuMgrService;

    @Description("菜单信息查询")
    @RequestMapping(value = "/getMenuData", method = RequestMethod.GET)
    public Wrapper getMenuData(@RequestParam long currentPage, @RequestParam int pageSize, MenuInfo menuInfo) {
        return this.handleResult(menuMgrService.getMenuData(currentPage, pageSize,menuInfo));
    }

    /**
     * 查询head部分头菜单
    * @param request
     * @return
     */
    @RequestMapping(value = "/getMenuHead",produces = {"application/json;charset=UTF-8"})
    public Wrapper getMenuRootInfo(HttpServletRequest request) {
        List<Map<String, Object>> list = menuMgrService.getMenuHead();
        return WrapMapper.ok(list);
    }

    /**
     * 以树形结构返回菜单信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/getMenuDataByTree",produces = {"application/json;charset=UTF-8"})
    public Wrapper getMenuSetDataByTree(HttpServletRequest request) {
        List<Map<String, Object>> list = menuMgrService.getMenuSetDataByTree("");
        return WrapMapper.ok(list);
    }

    @Description("增加菜单信息")
    @RequestMapping(value = "/addMenu", method = RequestMethod.POST)
    public Wrapper addMenu(MenuInfo menuInfo) {
        menuMgrService.addMenuInfo(menuInfo);
        return WrapMapper.ok();
    }

    @Description("删除菜单信息")
    @RequestMapping(value = "/deleteMenu", method = RequestMethod.POST)
    public Wrapper deleteMenu(MenuInfo menuInfo) {
        menuMgrService.deleteMenuInfo(menuInfo);
        return WrapMapper.ok();
    }

    @Description("编辑菜单信息")
    @RequestMapping(value = "/editMenu", method = RequestMethod.POST)
    public Wrapper editMenu(MenuInfo menuInfo) {
        menuMgrService.editMenuInfo(menuInfo);
        return WrapMapper.ok();
    }

    @Description("由ID查询菜单信息")
    @RequestMapping(value = "/getMenuById", method = RequestMethod.POST)
    public Wrapper getMenuById(MenuInfo menuInfo) {
        return this.handleResult( menuMgrService.getMenuById(menuInfo));
    }

    @Description("移动产品")
    @RequestMapping(value = "/moveMenu", method = RequestMethod.POST)
    public Wrapper moveProduct(@RequestParam String id,@RequestParam String targetid,@RequestParam String flag) {
        menuMgrService.moveMenu(id,targetid,flag);
        return WrapMapper.ok();
    }

    @RequestMapping(value = "/noSign/doNoThing", method = RequestMethod.GET)
    public Wrapper doNoThing(){
        System.out.println("+++");
        return WrapMapper.ok();
    }
}
