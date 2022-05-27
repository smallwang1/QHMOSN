package com.piesat.site.system.customcolumn.service;

import com.piesat.site.system.customcolumn.service.dto.AddColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.QueryColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.EditColumnReqDto;
import com.piesat.site.system.customcolumn.service.entity.ColumnData;

import java.io.IOException;
import java.util.List;

/**
 * @Author Thomas 2022/1/5 22:50
 * The world of programs is a wonderful world
 */
public interface  ColumnService {

    List<ColumnData> queryColumnList(QueryColumnReqDto queryColumnReqDto) throws IOException;

    boolean addColumn(AddColumnReqDto addColumnReqDto);

    boolean editColumn(EditColumnReqDto editShareReqDto);

    boolean deleteColumn(Integer columnId);
}
