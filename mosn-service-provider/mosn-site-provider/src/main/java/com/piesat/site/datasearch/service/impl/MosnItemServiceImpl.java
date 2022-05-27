package com.piesat.site.datasearch.service.impl;

import com.piesat.site.datasearch.service.IMosnItemDetailService;
import com.piesat.site.datasearch.service.IMosnItemService;
import com.piesat.site.datasearch.service.dto.ItemDetailDto;
import com.piesat.site.datasearch.service.dto.ItemDto;
import com.piesat.site.datasearch.service.entity.MosnItem;
import com.piesat.site.datasearch.service.mapper.MosnItemMapper;
import com.piesat.site.datasearch.service.util.BeanUtils;
import com.piesat.site.datasearch.service.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("mosnItemService")
public class MosnItemServiceImpl implements IMosnItemService {

    private static final Logger logger = LoggerFactory.getLogger(MosnItemServiceImpl.class);

    @Autowired
    private MosnItemMapper mosnItemMapper;
    @Autowired
    private IMosnItemDetailService mosnItemDetailService;

    @Override
    public List<ItemDto> generatorItem(Long[] itemIds, Boolean isReueired) {
        if (StringUtils.isEmpty(itemIds)) return null;
        List<MosnItem> itemList = mosnItemMapper.selectItemByIds(itemIds);
        if (StringUtils.isEmpty(itemList)) return null;
        List<ItemDto> itemDtos = BeanUtils.convertList2List(itemList, ItemDto.class);

        List<ItemDetailDto> itemDetailDtos = mosnItemDetailService.selectDetailByItemIds(itemIds);
        Map<Long, List<ItemDetailDto>> itemDetailMap = itemDetailDtos.stream().collect(Collectors.groupingBy(ItemDetailDto::getItemId));
        for (ItemDto itemDto : itemDtos) {
            if ("elements".equals(itemDto.getItemCode())){
                itemDto.setIsElement("0");
            } else {
                if (isReueired)
                    itemDto.setRequired("0");
                else
                    itemDto.setRequired("1");
                itemDto.setIsElement("1");
                itemDto.setDetailList(itemDetailMap.get(itemDto.getItemId()));
            }
        }
        return itemDtos;
    }

    @Override
    public List<ItemDto> selectItemByIds(Long[] itemIds) {
        List<MosnItem> itemList = mosnItemMapper.selectItemByIds(itemIds);
        if (StringUtils.isEmpty(itemList)) return null;
        return BeanUtils.convertList2List(itemList, ItemDto.class);
    }
}
