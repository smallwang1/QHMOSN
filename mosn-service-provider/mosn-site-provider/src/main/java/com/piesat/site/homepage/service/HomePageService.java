package com.piesat.site.homepage.service;

import com.piesat.site.homepage.service.entity.HomeColumnData;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author Thomas 2022/1/5 22:50
 * The world of programs is a wonderful world
 */
public interface  HomePageService {

    Map<String,List<HomeColumnData>> queryColumnList(String userId);


    boolean editHomeColumn(Map<String,String> editHomeColumn);


    Map<String,List<HomeColumnData>> defaultSetting(String userId);

    Map<String,List<Map<String, Object>>> getWebsiteMap(Set<Long> roles) throws Exception;
}
