package com.piesat.busiclogic.app.util;

import com.piesat.common.util.PublicUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AppUtil {


    public  List<Map<String,Object>> eleList = new ArrayList<>();

    public  List<Map<String,Object>> getNodeData(List<Map<String,Object>> handleData) {
        List<Map<String,Object>> rlist = new ArrayList<>();
         for (int i = 0; i < handleData.size(); i++) {
            Map<String, Object> map = handleData.get(i);
            if (!PublicUtil.isEmpty(map.get("childrenList"))) {
                rlist.addAll((List<Map<String,Object>>)map.get("childrenList"));
            }
        }
        return rlist;
    }

    public  List<Map<String,Object>> getHandleData(Map<String,Object> handleData){
        if(PublicUtil.isEmpty(handleData.get("childrenList"))){
                eleList.add(handleData);
        }else{
            List<Map<String,Object>> eList = (List<Map<String,Object>>) handleData.get("childrenList");
            for (int i = 0; i < eList.size(); i++) {
                if(PublicUtil.isEmpty(eList.get(i).get("childrenList"))){
                    eleList.add(eList.get(i));
                }else{
                    this.getHandleData(eList.get(i));
                }
            }
        }
        return eleList;
    }
}
