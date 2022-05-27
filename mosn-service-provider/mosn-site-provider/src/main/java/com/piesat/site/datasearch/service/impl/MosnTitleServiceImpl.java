package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnItemDetailService;
import com.piesat.site.datasearch.service.IMosnItemService;
import com.piesat.site.datasearch.service.IMosnTitleService;
import com.piesat.site.datasearch.service.dto.ElementDto;
import com.piesat.site.datasearch.service.dto.ItemDetailDto;
import com.piesat.site.datasearch.service.dto.ItemDto;
import com.piesat.site.datasearch.service.dto.TitleDto;
import com.piesat.site.datasearch.service.entity.MosnTitle;
import com.piesat.site.datasearch.service.mapper.MosnTitleMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("mosnTitleService")
public class MosnTitleServiceImpl implements IMosnTitleService {

    private static final Logger logger = LoggerFactory.getLogger(MosnTitleServiceImpl.class);

    @Autowired
    private MosnTitleMapper mosnTitleMapper;
    @Autowired
    private IMosnItemService mosnItemService;
    @Autowired
    private IMosnItemDetailService mosnItemDetailService;

    @Override
    public List<TitleDto> generatorTitle(Long[] titleIds, List<ElementDto> elementList) {
        if (StringUtils.isEmpty(titleIds) || StringUtils.isEmpty(elementList))
            return null;

        List<MosnTitle> titleList = mosnTitleMapper.selectTitleByIds(titleIds);
        if (StringUtils.isEmpty(titleList))
            return null;

        Map<Long, List<ItemDto>> map = new HashMap<>();
        Map<Long, List<ElementDto>> elementMap = elementList.stream().collect(Collectors.groupingBy(ElementDto::getItemId));
        Map<Long, List<ItemDetailDto>> itemDetailMap = null;
        for (MosnTitle title : titleList) {
            if (StringUtils.isEmpty(title.getItems())) continue;
            Long[] itemIds = StringUtils.ConvertStr2Arr(title.getItems());
            List<ItemDto> itemDtos = mosnItemService.selectItemByIds(itemIds);
            List<ItemDetailDto> itemDetailDtos = mosnItemDetailService.selectDetailByItemIds(itemIds);
            if (StringUtils.isNotEmpty(itemDetailDtos))
                itemDetailMap = itemDetailDtos.stream().collect(Collectors.groupingBy(ItemDetailDto::getItemId));
            for (ItemDto itemDto : itemDtos) {
                if (StringUtils.equals("elements", itemDto.getItemCode())){
                    itemDto.setIsElement("1");
                    itemDto.setDetailList(elementMap.get(itemDto.getItemId()));
                } else {
                    itemDto.setIsElement("0");
                    itemDto.setDetailList(itemDetailMap.get(itemDto.getItemId()));
                }
            }
            map.put(title.getTitleId(), itemDtos);
        }

        List<TitleDto> titleDtos = BeanUtils.convertList2List(titleList, TitleDto.class);
        if (StringUtils.isNotEmpty(map)) {
            for (TitleDto titleDto : titleDtos) {
                List<ItemDto> itemDtos = map.get(titleDto.getTitleId());
                titleDto.setItemList(itemDtos);
            }
        }

        return titleDtos;
    }
}
