package com.piesat.busiclogic.busic.roleMgr.controller;

import com.piesat.busiclogic.busic.roleMgr.entity.RoleMgr;
import com.piesat.busiclogic.busic.roleMgr.service.RoleMgrService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/roleMgr")
public class RoleMgrController extends BaseController {


    @Autowired
    private RoleMgrService roleMgrService;

    @Description("角色列表信息查询")
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    public Wrapper getRoleData(@RequestParam long currentPage, @RequestParam int pageSize, RoleMgr roleMgr) {
        return this.handleResult(roleMgrService.getRoleData(currentPage, pageSize,roleMgr));
    }

    @Description("角色列表信息查询")
    @RequestMapping(value = "/getAllRole", method = RequestMethod.GET)
    public Wrapper getAllRole( RoleMgr roleMgr) {
        return this.handleResult(roleMgrService.getAllRole(roleMgr));
    }

    @Description("增加角色信息")
    @RequestMapping(value = "/addRole", method = RequestMethod.POST)
    public Wrapper addRole(RoleMgr roleMgr) {
        try {
            roleMgrService.addRoleInfo(roleMgr);
            return WrapMapper.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("删除角色信息")
    @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
    public Wrapper deleteRole(RoleMgr roleMgr) {
        roleMgrService.deleteRoleInfo(roleMgr);
        return WrapMapper.ok();
    }

    @Description("编辑角色信息")
    @RequestMapping(value = "/editRole", method = RequestMethod.POST)
    public Wrapper editRole(RoleMgr roleMgr) {
        try {
            roleMgrService.editRole(roleMgr);
            return WrapMapper.ok();
        } catch (IOException e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("由ID查角色单信息")
    @RequestMapping(value = "/getRoleById", method = RequestMethod.POST)
    public Wrapper getRoleById(RoleMgr roleMgr) {
        try {
            return WrapMapper.ok(roleMgrService.getRoleById(roleMgr));
        }catch (Exception e){
            logger.error("getRoleById",e);
            return WrapMapper.error();
        }
    }
}
