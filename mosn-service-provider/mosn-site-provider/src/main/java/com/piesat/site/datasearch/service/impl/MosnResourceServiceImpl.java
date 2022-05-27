package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.*;
import com.piesat.site.datasearch.service.constant.Constants;
import com.piesat.site.datasearch.service.dto.*;
import com.piesat.site.datasearch.service.entity.MosnNavMenu;
import com.piesat.site.datasearch.service.entity.MosnResource;
import com.piesat.site.datasearch.service.mapper.MosnResourceMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("mosnResourceService")
public class MosnResourceServiceImpl implements IMosnResourceService {

    private static final Logger logger = LoggerFactory.getLogger(MosnResourceServiceImpl.class);

    @Value("${tds.path}")
    private String tdsPath;

    @Autowired
    private MosnResourceMapper mosnResourceMapper;
    @Autowired
    private IMosnFormService mosnFormService;
    @Autowired
    private IMosnElementService mosnElementService;
    @Autowired
    private IMosnTitleService mosnTitleService;
    @Autowired
    private IMosnInterfaceService mosnInterfaceService;
    @Autowired
    private IMosnNavMenuService mosnNavMenuService;
    @Autowired
    private IMosnButtonService mosnButtonService;

    @Override
    public ResourceDto getDynamicForm(Long resourceId) {
        StringUtils.notNull(resourceId, "产品资料ID不能为空");

        MosnResource resource = mosnResourceMapper.selectResourceById(resourceId);
        if (null == resource) return null;
        ResourceDto resourceDto = BeanUtils.convertObj2Obj(resource, ResourceDto.class);

        Long[] elementIds = StringUtils.ConvertStr2Arr(resource.getElements());
        List<ElementDto> elementList = mosnElementService.selectElementByIds(elementIds);
        if (StringUtils.isEmpty(elementList)) return resourceDto;

        Long[] titleIds = StringUtils.ConvertStr2Arr(resource.getTitles());
        List<TitleDto> titleList = mosnTitleService.generatorTitle(titleIds, elementList);
        if (StringUtils.isEmpty(titleList)) return resourceDto;

        List<InterfaceDto> interfaceList = mosnInterfaceService.generatorInterface(resource.getResourceCode());
        if (StringUtils.isEmpty(interfaceList)) return resourceDto;

        Long[] formIds = StringUtils.ConvertStr2Arr(resource.getForms());
        List<FormDto> formList = mosnFormService.generatorForm(formIds, interfaceList, titleList);
        if (StringUtils.isEmpty(formList)) return resourceDto;
        resourceDto.setFormList(formList);

        return resourceDto;
    }

    @Override
    public List<MosnResource> selectResourceList(MosnResource resource) {
        return mosnResourceMapper.selectResourceList(resource);
    }

    @Override
    public List<ResourceDto> selectResourceByIds(String resouceArr) {
        if (StringUtils.isEmpty(resouceArr))
            return null;

        Long[] resouceIds = StringUtils.ConvertStr2Arr(resouceArr);

        List<MosnResource> resourceList = mosnResourceMapper.selectResourceByIds(resouceIds);
        if (StringUtils.isEmpty(resourceList)) return null;
        Map<Long, List<ButtonDto>> buttonMap = new HashMap<>();
        for (MosnResource resource : resourceList) {
            String buttons = resource.getButtons();
            if (StringUtils.isEmpty(buttons)) continue;
            Long[] buttonIds = StringUtils.ConvertStr2Arr(buttons);
            List<ButtonDto> buttonDtos = BeanUtils.convertList2List(mosnButtonService.selectButtonByIds(buttonIds), ButtonDto.class);
            buttonDtos.forEach(buttonDto -> {
                if (Constants.BUTTON_TDS.equals(buttonDto.getButtonCode()))
                    buttonDto.setButtonPath(tdsPath.replace("?", resource.getTdsPath()));
            });
            buttonMap.put(resource.getResourceId(), buttonDtos);
        }

        List<ResourceDto> resourceDtos = BeanUtils.convertList2List(mosnResourceMapper.selectResourceByIds(resouceIds), ResourceDto.class);
        resourceDtos.forEach(resourceDto -> {
            resourceDto.setButtons(buttonMap.get(resourceDto.getResourceId()));
        });
        return resourceDtos;
    }

    @Override
    public MosnResource selectResourceById(Long resourceId) {
        return mosnResourceMapper.selectResourceById((resourceId));
    }

    @Override
    public List<ResourceDto> selectResourceByMenuId(Long menuId) {
        MosnNavMenu navMenu = mosnNavMenuService.selectNavMenuById(menuId);
        return this.selectResourceByIds(navMenu.getResourceArr());
    }

    @Override
    public Integer insertResource(MosnResource resource) {
        return mosnResourceMapper.insertResource(resource);
    }

    @Override
    public Integer updateResource(MosnResource resource) {
        return mosnResourceMapper.updateResource(resource);
    }

    @Override
    public Integer deleteResource(Long[] resourceIds) {
        return mosnResourceMapper.deleteResource(resourceIds);
    }
}
