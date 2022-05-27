package com.piesat.site.system.customcolumn.service.impl;

import com.piesat.site.system.customcolumn.service.ColumnService;
import com.piesat.site.system.customcolumn.service.dto.AddColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.QueryColumnReqDto;
import com.piesat.site.system.customcolumn.service.dto.EditColumnReqDto;
import com.piesat.site.system.customcolumn.service.entity.ColumnData;
import com.piesat.site.system.customcolumn.service.mapper.ColumnMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Decoder;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author Thomas 2022/1/5 22:51
 * The world of programs is a wonderful world
 */
@Service
@Slf4j
public class ColumnServiceImpl implements ColumnService {

    @Resource
    private ModelMapper modelMapper;

    @Autowired
    private ColumnMapper columnMapper;

    public void test() {

        //单个转换
        modelMapper.map(null, null);
        //list转换
        List<Object> productInfoDtos = modelMapper.map(new Object(), new TypeToken<List<Object>>() {
        }.getType());
    }

    @Override
    public List<ColumnData> queryColumnList(QueryColumnReqDto queryColumnReqDto) throws IOException {
//        PageHelper.startPage(queryColumnReqDto.getCurrentPage(), queryColumnReqDto.getPageSize(), true);
        List<ColumnData> dataList = columnMapper.queryColumnList(queryColumnReqDto);
        for (ColumnData column: dataList) {
            byte[] basebt = column.getColumnPng();
            byte[] by = new BASE64Decoder().decodeBuffer(new String(basebt));
            column.setColumnPng(by);
        }
//        PageInfo<ColumnData> resPageInfo = new PageInfo<>(dataList);
        return dataList;
    }

    @Override
    public boolean addColumn(AddColumnReqDto addColumnReqDto) {
        return columnMapper.addColumn(addColumnReqDto);
    }

    @Override
    public boolean editColumn(EditColumnReqDto editColumnReqDto) {
        return columnMapper.updateColumn(editColumnReqDto);
    }


    @Override
    public boolean deleteColumn(Integer columnId) {
        return columnMapper.deleteColumn(columnId);
    }
}
