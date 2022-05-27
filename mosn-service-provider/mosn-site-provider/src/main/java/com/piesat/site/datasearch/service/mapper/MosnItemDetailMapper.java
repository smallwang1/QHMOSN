package com.piesat.site.datasearch.service.mapper;

import com.piesat.site.datasearch.service.entity.MosnItemDetail;

import java.util.List;

public interface MosnItemDetailMapper {

    List<MosnItemDetail> selectDetailByItemIds(Long[] itemIds);
}
