package com.piesat.site.homepage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.piesat.common.util.PublicUtil;
import com.piesat.site.homepage.service.dto.AddHomeColumnReqDto;
import com.piesat.site.homepage.service.entity.HomeColumnData;
import com.piesat.site.homepage.service.mapper.HomePageMapper;
import com.piesat.site.homepage.service.HomePageService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author Thomas 2022/1/5 22:51
 * The world of programs is a wonderful world
 */
@Service
@Slf4j
public class HomePageServiceImpl implements HomePageService {

    @Resource
    private ModelMapper modelMapper;

    @Autowired
    private HomePageMapper homePageMapper;


    public void test() {

//        //单个转换
//        modelMapper.map();
//        //list转换
//        List<Object> productInfoDtos = modelMapper.map(new Object(), new TypeToken<List<Object>>() {
//        }.getType());
    }

    @Override
    public Map<String, List<HomeColumnData>> queryColumnList(String userId) {
        Map<String, List<HomeColumnData>> map = new HashMap<>();
        //先获取用户相关的栏目
        List<HomeColumnData> homeColumnData = homePageMapper.queryColumnList(userId);
        //查询所有栏目
        List<HomeColumnData> homeColumnDataList = homePageMapper.queryDefaultColumnList();
        //如果用户相关为空，则展示默认栏目
        if (homeColumnData.isEmpty()) {
            if (homeColumnDataList.size() <= 10) {
                map.put("showColumn", homeColumnDataList);
                map.put("noShowColumn", null);
            } else {
                map.put("showColumn", homeColumnDataList.subList(0, 9));
                map.put("noShowColumn", homeColumnDataList.subList(9, homeColumnDataList.size() - 10));
            }
        } else {
            map.put("showColumn", homeColumnData);
            List<HomeColumnData> noHomeColumnData = new ArrayList<>();
            for (HomeColumnData hcd : homeColumnDataList) {
                boolean isHave = false;
                for (HomeColumnData hcda : homeColumnData) {
                    if (Objects.equals(hcd.getColumnId(), hcda.getColumnId())) {
                        isHave = true;
                    }
                }
                if (!isHave) {
                    noHomeColumnData.add(hcd);
                }
            }
            map.put("noShowColumn", noHomeColumnData);
        }
        return map;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public boolean editHomeColumn(Map<String, String> addColumnReqDto) {
        String userId = addColumnReqDto.get("userId");
        String columns = addColumnReqDto.get("columns");
        JSONObject jsonObject = JSON.parseObject(columns);
        JSONArray addColumnId = jsonObject.getJSONObject("data").getJSONArray("addColumnId");
//        JSONArray deleteColumnId = jsonObject.getJSONObject("data").getJSONArray("deleteColumnId");
        List<HomeColumnData> homeColumnData = homePageMapper.queryColumnList(userId);
        for (int i = 0; i < homeColumnData.size(); i++) {
            HomeColumnData deleteColumn = homeColumnData.get(i);
            AddHomeColumnReqDto addHomeColumnReqDto = new AddHomeColumnReqDto();
            addHomeColumnReqDto.setUserId(userId);
            addHomeColumnReqDto.setColumnId(String.valueOf(deleteColumn.getColumnId()));
            homePageMapper.deleteColumn(addHomeColumnReqDto);
        }
        for (int i = 0; i < addColumnId.size(); i++) {
            AddHomeColumnReqDto addHomeColumnReqDto = new AddHomeColumnReqDto();
            addHomeColumnReqDto.setUserId(userId);
            addHomeColumnReqDto.setColumnId(String.valueOf(addColumnId.get(i)));
            addHomeColumnReqDto.setShowSort(i + 1);
//            addHomeColumnReqDto.setColumnStatus(1);
            homePageMapper.addColumn(addHomeColumnReqDto);
        }
        return true;
    }


    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, RuntimeException.class})
    @Override
    public Map<String, List<HomeColumnData>> defaultSetting(String userId) {
        //默认的栏目
        List<HomeColumnData> homeColumnData = homePageMapper.queryDefaultColumnList();
        //用户相关的栏目
        List<HomeColumnData> homeColumnDataList = homePageMapper.queryColumnList(userId);

        for (HomeColumnData hcdl : homeColumnDataList) {
            AddHomeColumnReqDto addHomeColumnReqDto = new AddHomeColumnReqDto();
            addHomeColumnReqDto.setUserId(userId);
            addHomeColumnReqDto.setColumnId(String.valueOf(hcdl.getColumnId()));
            homePageMapper.deleteColumn(addHomeColumnReqDto);
        }
        for (int i = 0; i < homeColumnData.size(); i++) {
            AddHomeColumnReqDto addHomeColumnReqDto = new AddHomeColumnReqDto();
            HomeColumnData hcd = homeColumnData.get(i);
            addHomeColumnReqDto.setUserId(userId);
            addHomeColumnReqDto.setColumnId(String.valueOf(hcd.getColumnId()));
            addHomeColumnReqDto.setShowSort(i + 1);
            homePageMapper.addColumn(addHomeColumnReqDto);
            hcd.setUserId(userId);
        }
        Map<String, List<HomeColumnData>> map = new HashMap<>();
        if (homeColumnData.size() <= 10) {
            map.put("showColumn", homeColumnData);
            map.put("noShowColumn", null);
        } else {
            map.put("showColumn", homeColumnData.subList(0, 9));
            map.put("noShowColumn", homeColumnData.subList(9, homeColumnData.size() - 10));
        }
        return map;
    }

    @SneakyThrows
    @Override
    public Map<String, List<Map<String, Object>>> getWebsiteMap(Set<Long> roles){
        Map<String, List<Map<String, Object>>> map = new HashMap<>();
        List<Long> roleIds = new ArrayList<Long>(roles);

        //根据角色权限查询菜单，取并集并去重
        List<Map<String, Object>> menuList = new ArrayList<>();
        for (Long roleId: roleIds) {
            List<Map<String, Object>> menuIDList = homePageMapper.queryMenuId(String.valueOf(roleId));
            menuList.addAll(menuIDList);
            menuList = menuList.stream().distinct().collect(Collectors.toList());
        }

        //根据角色权限查询产品，取并集并去重
        List<Map<String, Object>> productList = new ArrayList<>();
        for (Long roleId: roleIds) {
            List<Map<String, Object>> productIDList = homePageMapper.queryProductId(String.valueOf(roleId));
            productList.addAll(productIDList);
            productList = productList.stream().distinct().collect(Collectors.toList());
        }

        //查询出所有的菜单，和角色权限进行比对
        List<Map<String, Object>> menuListAll = homePageMapper.queryMenuInfo();
        List<Map<String, Object>> menuMaps = new ArrayList<>();
        for (Map<String, Object> map1 : menuList) {
            for (Map<String, Object> map2 : menuListAll) {
                if (map1.get("MENUID").equals(map2.get("ID"))) {
                    menuMaps.add(map2);
                }
            }
        }
        List<Map<String, Object>> menuMapsAll = recursion("R", menuMaps);

        //查询出所有的产品，和角色权限进行比对
        List<Map<String, Object>> productListAll = homePageMapper.queryProductInfo();
        List<Map<String, Object>> productMaps = new ArrayList<>();
        for (Map<String, Object> map1 : productList) {
            for (Map<String, Object> map2 : productListAll) {
                if (map1.get("PRODUCTID").equals(map2.get("ID"))) {
                    productMaps.add(map2);
                }
            }
        }
        List<Map<String, Object>> productMapsAll = recursion("R", productMaps);

        //将产品信息放入对应菜单下
        for (Map<String, Object> promap : productMapsAll) {
            for (Map<String, Object> menmap : menuMapsAll) {
                if (promap.get("ID").equals(menmap.get("PRODUCTID"))){
                    menmap.put("childrenList",promap.get("childrenList"));
                }
            }
        }

        //菜单中去除首页和综合显示
        for(int i = menuMapsAll.size() - 1; i >= 0; i--){
            if(menuMapsAll.get(i).get("NAME").equals("首页") || menuMapsAll.get(i).get("NAME").equals("综合显示")){
                menuMapsAll.remove(menuMapsAll.get(i));
            }
        }
        map.put("menu", screen(menuMapsAll));
        return map;
    }

    //递归获取树型结构数据
    public static List<Map<String, Object>> recursion(String id, List<Map<String, Object>> listData) throws Exception{
        List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
        Iterator it = listData.iterator();
        while (it.hasNext()){
            Map<String, Object> map = (Map<String, Object>)it.next();
            String pid = String.valueOf(map.get("pid"));
            if(PublicUtil.isEmpty(pid)){
                pid = String.valueOf(map.get("PID"));
            }
            if (id.equals(pid)){
                treeList.add(map);
                //使用Iterator，以便在迭代时把listData中已经添加到treeList的数据删除，迭代次数
                it.remove();
            }
        }
        for(Map<String, Object> map:treeList){
            List<Map<String, Object>> treeList123 =  recursion(map.get("ID").toString(), listData);
            if(!PublicUtil.isEmpty(treeList123)){
                map.put("childrenList", treeList123);
            }
        }
        return treeList;
    }

    public static List<Map<String, Object>> screen(List<Map<String, Object>> listData) {
        Iterator it = listData.iterator();
        while (it.hasNext()) {
            Map<String, Object> map = (Map<String, Object>) it.next();
            if (map.get("childrenList") != null){
                List<Map<String, Object>> child1 = (List<Map<String, Object>>) map.get("childrenList");
                for (Map<String, Object> map1: child1) {
                    if (map1.get("childrenList") != null){
                        List<Map<String, Object>> child2 = (List<Map<String, Object>>) map1.get("childrenList");
                        for (Map<String, Object> map2: child2) {
                            if (map2.get("childrenList") != null){
                                List<Map<String, Object>> child3 = (List<Map<String, Object>>) map2.get("childrenList");
                                for (int i = child3.size() -1; i > 0; i--) {
                                    child3.remove(child3.get(i));
                                }
                            }
                        }
                    }
                }
            }
        }
        return listData;
    }
}
