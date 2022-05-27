package com.piesat.site.datasearch.controller;

import com.piesat.common.core.dto.LoginAuthDto;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.site.datasearch.service.IMosnNavMenuService;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.dto.FNavMenuDto;
import com.piesat.site.datasearch.service.entity.MosnNavMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/navMenu")
public class MosnNavMenuController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(MosnNavMenuController.class);

    @Autowired
    private IMosnNavMenuService mosnNavMenuService;

    /**
     * 查询导航菜单
     * @param navMenu
     * @return
     */
    @GetMapping("/list")
    public Wrapper list(MosnNavMenu navMenu) {
        List<FNavMenuDto> navMenuList = mosnNavMenuService.selectNavMenuList(navMenu);
        return WrapMapper.ok(navMenuList);
    }

    /**
     * 新增导航菜单
     * @param navMenu
     * @return
     */
    @PostMapping
    public Wrapper add(@Validated @RequestBody MosnNavMenu navMenu) {
        if (Constants.NOT_UNIQUE.equals(mosnNavMenuService.checkMenuNameUnique(navMenu))) {
            return WrapMapper.error("新增导航菜单'" + navMenu.getMenuName() + "'失败，菜单名称已存在");
        }
        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        navMenu.setCreateBy(loginAuthDto.getUsername());
        return WrapMapper.judge(mosnNavMenuService.insertNavMenu(navMenu));
    }

    /**
     * 修改导航菜单
     * @param navMenu
     * @return
     */
    @PutMapping
    public Wrapper edit(@Validated @RequestBody MosnNavMenu navMenu) {
        if (Constants.NOT_UNIQUE.equals(mosnNavMenuService.checkMenuNameUnique(navMenu))) {
            return WrapMapper.error("修改导航菜单'" + navMenu.getMenuName() + "'失败，菜单名称已存在");
        } else if (navMenu.getMenuId().equals(navMenu.getParentId()))
        {
            return WrapMapper.error("修改导航菜单'" + navMenu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        LoginAuthDto loginAuthDto = this.getLoginAuthDto();
        navMenu.setCreateBy(loginAuthDto.getUsername());
        return WrapMapper.judge(mosnNavMenuService.updateNavMenu(navMenu));
    }

    /**
     * 根据Id删除菜单
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public Wrapper remove(@PathVariable("menuId") Long menuId) {
        if (mosnNavMenuService.hasChildByMenuId(menuId)) {
            return WrapMapper.error("存在导航子菜单,不允许删除");
        }
        return WrapMapper.judge(mosnNavMenuService.deleteNavMenuById(menuId));
    }
}
