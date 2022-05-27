package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnInterfaceService;
import com.piesat.site.datasearch.service.IMosnItemService;
import com.piesat.site.datasearch.service.dto.InterfaceDto;
import com.piesat.site.datasearch.service.dto.ItemDto;
import com.piesat.site.datasearch.service.entity.MosnInterface;
import com.piesat.site.datasearch.service.mapper.MosnInterfaceMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("mosnInterfaceService")
public class MosnInterfaceServiceImpl implements IMosnInterfaceService {

    private static final Logger logger = LoggerFactory.getLogger(MosnInterfaceServiceImpl.class);

    @Autowired
    private MosnInterfaceMapper mosnInterfaceMapper;
    @Autowired
    private IMosnItemService mosnItemService;

    @Override
    public List<InterfaceDto> generatorInterface(String resourceCode) {
        MosnInterface mosnInterface = new MosnInterface();
        mosnInterface.setResourceCode(resourceCode);
        List<MosnInterface> interfaceList = this.selectInterfaceList(mosnInterface);
        if (StringUtils.isEmpty(interfaceList)) return null;

        List<InterfaceDto> interfaceDtos = new ArrayList<>();
        for (MosnInterface mosnInter : interfaceList) {
            List<ItemDto> itemList = new ArrayList<>();
            Long[] requiredIds = StringUtils.ConvertStr2Arr(mosnInter.getRequiredParam());
            List<ItemDto> requiredDtos = mosnItemService.generatorItem(requiredIds, true);
            if (StringUtils.isNotEmpty(requiredDtos))
                itemList.addAll(requiredDtos);

            Long[] optionalIds = StringUtils.ConvertStr2Arr(mosnInter.getOptionalParam());
            List<ItemDto> optionalDtos = mosnItemService.generatorItem(optionalIds, false);
            if (StringUtils.isNotEmpty(optionalDtos))
                itemList.addAll(optionalDtos);

            InterfaceDto interfaceDto = BeanUtils.convertObj2Obj(mosnInter, InterfaceDto.class);
            interfaceDto.setItemList(itemList);
            interfaceDtos.add(interfaceDto);
        }

        return interfaceDtos;
    }

    @Override
    public List<MosnInterface> selectInterfaceList(MosnInterface mosnInterface) {
        return mosnInterfaceMapper.selectInterfaceList(mosnInterface);
    }

    @Override
    public Map<String, Object> selectInfoById(Long interfaceId) {
        return mosnInterfaceMapper.selectInfoById(interfaceId);
    }
}
