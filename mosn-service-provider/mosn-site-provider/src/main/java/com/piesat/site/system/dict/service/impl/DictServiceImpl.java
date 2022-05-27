package com.piesat.site.system.dict.service.impl;


import com.piesat.site.system.dict.service.DictService;
import com.piesat.site.system.dict.service.dto.AddDictReqDto;
import com.piesat.site.system.dict.service.dto.EditDictReqDto;
import com.piesat.site.system.dict.service.dto.QueryDictReqDto;
import com.piesat.site.system.dict.service.entity.SysDict;
import com.piesat.site.system.dict.service.mapper.DictMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author Thomas 2022/1/5 22:51
 * The world of programs is a wonderful world
 */
@Service
@Primary
@Slf4j
public class DictServiceImpl implements DictService {
    @Resource
    private ModelMapper modelMapper;

    @Autowired
    private DictMapper dictMapper;

    public void test() {
        //单个转换
        modelMapper.map(null, null);
        //list转换
        List<Object> productInfoDtos = modelMapper.map(new Object(), new TypeToken<List<Object>>() {
        }.getType());
    }

    public List<SysDict> queryDictList(QueryDictReqDto queryDictReqDto) throws IOException {
        List<SysDict> dataList = dictMapper.queryDictList(queryDictReqDto);
        return dataList;
    }

    @Override
    public boolean addDict(AddDictReqDto addDictReqDto) {
        String newDictCode = addDictReqDto.getDictCode();
        List<SysDict> dataList = dictMapper.judgeDictList(newDictCode);
        if (dataList==null){
            return false;
        }

        addDictReqDto.setCreateDate(new Date());
        addDictReqDto.setUpdateDate(new Date());
        return  dictMapper.addDict(addDictReqDto);
    }

    @Override
    public boolean editDict(EditDictReqDto editDictReqDto) {
        editDictReqDto.setUpdateDate(new Date());
        return dictMapper.updateDict(editDictReqDto);
    }

    @Override
    public boolean banDict(String dictCode) {
        Date updateDate = new Date();
        return dictMapper.banDict(dictCode,updateDate);
    }

    @Override
    public boolean startDict(String dictCode) {
        Date updateDate = new Date();
        return dictMapper.startDict(dictCode,updateDate);
    }
}
