package com.piesat.site.system.dict.service.mapper;

import com.piesat.jdbc.MyMapper;
import com.piesat.site.system.dict.service.dto.AddDictReqDto;
import com.piesat.site.system.dict.service.dto.EditDictReqDto;
import com.piesat.site.system.dict.service.dto.QueryDictReqDto;
import com.piesat.site.system.dict.service.entity.SysDict;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;


@Mapper
@Component
public interface DictMapper extends MyMapper<Object> {

    List<SysDict> queryDictList(QueryDictReqDto queryDictReqDto);

    List<SysDict> judgeDictList(String newDictCode);

    boolean addDict(AddDictReqDto addDictReqDto);

    boolean updateDict(EditDictReqDto editDictReqDto);

    boolean banDict(@Param("dictCode") String dictCode,@Param("updateDate") Date date);

    boolean startDict(@Param("dictCode")String dictCode,@Param("updateDate") Date updateDate);
}
