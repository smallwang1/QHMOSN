package com.piesat.busiclogic.busic.organMgr.controller;

import com.piesat.busiclogic.busic.organMgr.entity.OrganInfo;
import com.piesat.busiclogic.busic.organMgr.service.OrganMgrService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/organMgr")
public class OrganMgrController extends BaseController {

    @Autowired
    private OrganMgrService organMgrService;

    @Description("组织列表信息查询")
    @RequestMapping(value = "/getOrganList", method = RequestMethod.GET)
    public Wrapper getOrganList(@RequestParam long currentPage, @RequestParam int pageSize, OrganInfo organInfo) {
        return this.handleResult(organMgrService.getOrganList(currentPage, pageSize,organInfo));
    }

    @Description("新增组织信息")
    @RequestMapping(value = "/addOrgan", method = RequestMethod.POST)
    public Wrapper addOrgan(OrganInfo organInfo) {
        organMgrService.addOrgan(organInfo);
        return WrapMapper.ok();
    }

    @Description("删除组织信息")
    @RequestMapping(value = "/deleteOrgan", method = RequestMethod.POST)
    public Wrapper deleteOrgan(OrganInfo organInfo) {
        organMgrService.deleteOrgan(organInfo);
        return WrapMapper.ok();
    }

    @Description("编辑组织信息")
    @RequestMapping(value = "/editOrgan", method = RequestMethod.POST)
    public Wrapper editOrgan(OrganInfo organInfo) {
        organMgrService.editOrgan(organInfo);
        return WrapMapper.ok();
    }

    @Description("由ID查组织信息")
    @RequestMapping(value = "/getOrganById", method = RequestMethod.POST)
    public Wrapper getRoleById(OrganInfo organInfo) {
        return this.handleResult( organMgrService.getOrganById(organInfo));
    }

    @Description("获取组织树结构")
    @RequestMapping(value = "/getOrganTree", method = RequestMethod.GET)
    public Wrapper getOrganTreeData(HttpServletRequest request,String pid) {
        List<Map<String, Object>> list = organMgrService.getOrganTreeData(pid);
        return WrapMapper.ok(list);
    }
}
