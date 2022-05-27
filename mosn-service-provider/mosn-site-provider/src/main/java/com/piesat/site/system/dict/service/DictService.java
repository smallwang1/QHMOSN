package com.piesat.site.system.dict.service;

import com.piesat.site.system.dict.service.dto.AddDictReqDto;
import com.piesat.site.system.dict.service.dto.EditDictReqDto;
import com.piesat.site.system.dict.service.dto.QueryDictReqDto;
import com.piesat.site.system.dict.service.entity.SysDict;

import java.io.IOException;
import java.util.List;

public interface DictService {
    List<SysDict> queryDictList(QueryDictReqDto queryDictReqDto) throws IOException;

    boolean addDict(AddDictReqDto addDictReqDto);

    boolean editDict(EditDictReqDto editDictReqDto);

    boolean banDict(String dictCode);

    boolean startDict(String dictCode);
}
