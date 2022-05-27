package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnElement;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface MosnElementMapper {

    List<MosnElement> selectElementByIds(Long[] elementIds);
}
