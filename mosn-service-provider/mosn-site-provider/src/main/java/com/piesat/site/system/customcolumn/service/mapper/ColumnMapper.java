package com.piesat.site.system.customcolumn.service.mapper;

import com.piesat.jdbc.MyMapper;
import com.piesat.site.system.customcolumn.service.dto.AddColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.EditColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.QueryColumnReqDto;
import com.piesat.site.system.customcolumn.service.entity.ColumnData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;


@Mapper
@Component
public interface ColumnMapper extends MyMapper<Object> {

    List<ColumnData> queryColumnList(QueryColumnReqDto queryColumnReqDto);

    boolean addColumn(AddColumnReqDto addColumnReqDto);

    boolean updateColumn(EditColumnReqDto editColumnReqDto);

    boolean deleteColumn(Integer columnId);
}
