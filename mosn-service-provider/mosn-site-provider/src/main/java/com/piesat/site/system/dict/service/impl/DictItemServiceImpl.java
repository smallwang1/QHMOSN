package com.piesat.site.system.dict.service.impl;

import com.piesat.site.system.dict.service.DictItemService;
import com.piesat.site.system.dict.service.dto.*;
import com.piesat.site.system.dict.service.entity.SysDict;
import com.piesat.site.system.dict.service.entity.SysDictItem;
import com.piesat.site.system.dict.service.mapper.DictItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
@Service
@Primary
@Slf4j
public class DictItemServiceImpl implements DictItemService {
    @Autowired
    private DictItemMapper dictItemMapper;

    @Override
    public List<SysDictItem> queryDictItemList(QueryDictItemReqDto queryDictItemReqDto) throws IOException {
        List<SysDictItem> dataList = dictItemMapper.queryDictItemList(queryDictItemReqDto);
        return dataList;
    }

    @Override
    public boolean addDictItem(AddDictItemReqDto addDictItemReqDto) {
        String newDictItemValue = addDictItemReqDto.getItemValue();
        List<SysDictItem> dataList = dictItemMapper.judgeDictItemList(newDictItemValue);
        if (dataList==null){
            return false;
        }

        addDictItemReqDto.setCreateDate(new Date());
        addDictItemReqDto.setUpdateDate(new Date());
        return  dictItemMapper.addDictItem(addDictItemReqDto);
    }

    @Override
    public boolean editDictItem(EditDictItemReqDto editDictItemReqDto) {
        editDictItemReqDto.setUpdateDate(new Date());
        return dictItemMapper.updateDictItem(editDictItemReqDto);
    }

    @Override
    public boolean deleteDictItem(Integer id) {
        return dictItemMapper.deleteDictItem(id);
    }
}
