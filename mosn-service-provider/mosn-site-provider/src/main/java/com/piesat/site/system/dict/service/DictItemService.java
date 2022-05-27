package com.piesat.site.system.dict.service;

import com.piesat.site.system.dict.service.dto.*;
import com.piesat.site.system.dict.service.entity.SysDict;
import com.piesat.site.system.dict.service.entity.SysDictItem;

import java.io.IOException;
import java.util.List;

public interface DictItemService {
    List<SysDictItem> queryDictItemList(QueryDictItemReqDto queryDictItemReqDto) throws IOException;

    boolean addDictItem(AddDictItemReqDto addDictItemReqDto);

    boolean editDictItem(EditDictItemReqDto editDictItemReqDto);

    boolean deleteDictItem(Integer id);
}
