package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnForm;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface MosnFormMapper {

    List<MosnForm> selectFormByIds(Long[] formIds);
}
