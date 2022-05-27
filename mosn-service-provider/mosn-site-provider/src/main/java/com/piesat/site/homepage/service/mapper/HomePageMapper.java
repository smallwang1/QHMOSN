package com.piesat.site.homepage.service.mapper;

import com.piesat.jdbc.MyMapper;
import com.piesat.site.homepage.service.dto.AddHomeColumnReqDto;
import com.piesat.site.homepage.service.entity.HomeColumnData;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface HomePageMapper extends MyMapper<Object> {

        boolean addColumn(AddHomeColumnReqDto addColumnReqDto);

        boolean deleteColumn(AddHomeColumnReqDto deleteColumnReqDto);

        List<HomeColumnData> queryColumnList(String userId);

        List<HomeColumnData> queryDefaultColumnList();

        List<Map<String, Object>> queryMenuId(String roleId);

        List<Map<String, Object>> queryProductId(String roleId);

        List<Map<String, Object>> queryMenuInfo();

        List<Map<String, Object>> queryProductInfo();
}
