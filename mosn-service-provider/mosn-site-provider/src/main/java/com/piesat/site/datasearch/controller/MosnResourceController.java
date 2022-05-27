package com.piesat.site.datasearch.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.IMosnResourceService;
import com.piesat.site.datasearch.service.entity.MosnResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resource")
public class MosnResourceController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnResourceController.class);

    @Autowired
    private IMosnResourceService mosnResourceService;

    /**
     * 健康监测
     * @return
     */
    @GetMapping("/health")
    public String health() {
        return "up";
    }

    /**
     * 查询资料动态检索表单
     * @param resourceId
     * @return
     */
    @GetMapping("/form/{resourceId}")
    public Wrapper form(@PathVariable("resourceId") Long resourceId) {
        return WrapMapper.ok(mosnResourceService.getDynamicForm(resourceId));
    }

    /**
     * 产品资料分页查询
     * @param pageNum
     * @param pageSize
     * @param resource
     * @return
     */
    @GetMapping("/list/{pageNum}/{pageSize}")
    public Wrapper list(@PathVariable("pageNum") int pageNum, @PathVariable("pageSize") int pageSize, MosnResource resource) {
        Page page = PageHelper.startPage(pageNum, pageSize, true);
        List<MosnResource> resourceList = mosnResourceService.selectResourceList(resource);
        PageInfo<MosnResource> resourcePageInfo = new PageInfo<>(resourceList);
        resourcePageInfo.setTotal(page.getTotal());
        return WrapMapper.ok(resourcePageInfo);
    }

    /**
     * 根据Id查询产品资料
     * @param resourceId
     * @return
     */
    @GetMapping("/{resourceId}")
    public Wrapper selectResourceById(@PathVariable("resourceId") Long resourceId) {
        return WrapMapper.ok(mosnResourceService.selectResourceById(resourceId));
    }

    /**
     *  根据Id查询产品集合
     * @param menuId
     * @return
     */
    @GetMapping("/menu/{menuId}")
    public Wrapper selectResourceByMenuId(@PathVariable("menuId") Long menuId) {
        return WrapMapper.ok(mosnResourceService.selectResourceByMenuId(menuId));
    }

    /**
     * 新增产品资料
     * @param resource
     * @return
     */
    @PostMapping
    public Wrapper add(@Validated @RequestBody MosnResource resource) {
//        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
//        resource.setCreateBy(loginAuthDto.getUsername());
        return WrapMapper.judge(mosnResourceService.insertResource(resource));
    }

    /**
     * 修改保存产品资料
     * @param resource
     * @return
     */
    @PutMapping
    public Wrapper edit(@Validated @RequestBody MosnResource resource) {
//        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
//        resource.setCreateBy(loginAuthDto.getUsername());
        return WrapMapper.judge(mosnResourceService.updateResource(resource));
    }

    /**
     * 删除产品资料
     * @param resourceIds
     * @return
     */
    @DeleteMapping("/{resourceIds}")
    public Wrapper remove(@PathVariable("resourceIds") Long[] resourceIds) {
        return WrapMapper.judge(mosnResourceService.deleteResource(resourceIds));
    }
}
