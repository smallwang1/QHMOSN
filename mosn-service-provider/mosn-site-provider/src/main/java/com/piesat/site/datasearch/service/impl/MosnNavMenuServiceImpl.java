package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnNavMenuService;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.dto.FNavMenuDto;
import com.piesat.site.datasearch.service.dto.SNavMenuDto;
import com.piesat.site.datasearch.service.entity.MosnNavMenu;
import com.piesat.site.datasearch.service.mapper.MosnNavMenuMapper;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("mosnNavMenuService")
public class MosnNavMenuServiceImpl implements IMosnNavMenuService {

    private static final Logger logger = LoggerFactory.getLogger(MosnNavMenuServiceImpl.class);

    @Autowired
    private MosnNavMenuMapper mosnNavMenuMapper;

    @Override
    public List<FNavMenuDto> selectNavMenuList(MosnNavMenu navMenu) {
        List<MosnNavMenu> navMenuList = mosnNavMenuMapper.selectNavMenuList(navMenu);
        List<FNavMenuDto> fNavMenuDtoList = new ArrayList<>();
        List<SNavMenuDto> sNavMenuDtoList = new ArrayList<>();
        if (StringUtils.isNotEmpty(navMenuList)) {
            navMenuList.forEach(e -> {
                if ("F".equals(e.getMenuType()))
                    fNavMenuDtoList.add(new FNavMenuDto(e));
                else if ("S".equals(e.getMenuType()))
                    sNavMenuDtoList.add(new SNavMenuDto(e));
            });
        }

        if (StringUtils.isNotEmpty(sNavMenuDtoList) && StringUtils.isNotEmpty(fNavMenuDtoList)) {
            Map<Long, List<SNavMenuDto>> sNavMenuMap = sNavMenuDtoList.stream().collect(Collectors.groupingBy(SNavMenuDto::getParentId));
            fNavMenuDtoList.forEach(e -> {
                List<SNavMenuDto> sNavMenuDtos = sNavMenuMap.get(e.getMenuId());
                if (StringUtils.isNotEmpty(sNavMenuDtos))
                    e.setNavMenuDtoList(sNavMenuDtos);
            });
        }

        return fNavMenuDtoList;
    }

    @Override
    public MosnNavMenu selectNavMenuById(Long menuId) {
        return mosnNavMenuMapper.selectNavMenuById(menuId);
    }

    @Override
    public String checkMenuNameUnique(MosnNavMenu navMenu) {
        Long menuId = StringUtils.isNull(navMenu.getMenuId()) ? -1L : navMenu.getMenuId();
        MosnNavMenu info = mosnNavMenuMapper.checkMenuNameUnique(navMenu.getMenuName(), navMenu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue()) {
            return Constants.NOT_UNIQUE;
        }
        return Constants.UNIQUE;
    }

    @Override
    public Integer insertNavMenu(MosnNavMenu navMenu) {
        return mosnNavMenuMapper.insertNavMenu(navMenu);
    }

    @Override
    public Integer updateNavMenu(MosnNavMenu navMenu) {
        return mosnNavMenuMapper.updateNavMenu(navMenu);
    }

    @Override
    public boolean hasChildByMenuId(Long menuId) {
        Integer count = mosnNavMenuMapper.hasChildByMenuId(menuId);
        return count > 0 ? true : false;
    }

    @Override
    public Integer deleteNavMenuById(Long menuId) {
        return mosnNavMenuMapper.deleteNavMenuById(menuId);
    }
}
