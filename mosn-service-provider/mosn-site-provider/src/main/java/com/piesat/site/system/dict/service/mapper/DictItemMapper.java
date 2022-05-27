package com.piesat.site.system.dict.service.mapper;

import com.piesat.jdbc.MyMapper;
import com.piesat.site.system.dict.service.dto.*;
import com.piesat.site.system.dict.service.entity.SysDict;
import com.piesat.site.system.dict.service.entity.SysDictItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Mapper
@Component
public interface DictItemMapper extends MyMapper<Object> {

    List<SysDictItem> queryDictItemList(QueryDictItemReqDto queryDictItemReqDto);

    List<SysDictItem> judgeDictItemList(String newDictItemValue);

    boolean addDictItem(AddDictItemReqDto addDictItemReqDto);

    boolean updateDictItem(EditDictItemReqDto editDictItemReqDto);

    boolean deleteDictItem(Integer id);
}
