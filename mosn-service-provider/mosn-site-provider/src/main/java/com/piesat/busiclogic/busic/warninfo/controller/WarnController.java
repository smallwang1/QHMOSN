package com.piesat.busiclogic.busic.warninfo.controller;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.PublicUtil;
import com.piesat.common.util.RedisUtils;
import com.xiaoleilu.hutool.json.JSONArray;
import com.xiaoleilu.hutool.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

/**
 * @Descripton 预警信息
 * @Author sjc
 * @Date 2020/3/9
 **/
@RestController
@CrossOrigin
@RequestMapping("/api/warn")
public class WarnController {

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 历史预警接口名称
     */
    private static String hisWarnInterfaceName = "SWP.GetHistorySignalByStationCodes";

    @Value("${warn_url}")
    private String warn_url;

    /**
     *  获取历史预警数据
     * @param SignalType
     * @param StationCodes
     * @param startime
     * @param endtime
     * @param signallevel
     * @return
     */
    @RequestMapping(value = "/history",produces = {"application/json;charset=UTF-8"})
    public Wrapper getWarnHistory(@RequestParam String SignalType,
                                  @RequestParam String StationCodes,
                                  @RequestParam String startime,
                                  @RequestParam String endtime,
                                  @RequestParam String signallevel){
        String iquery = hisWarnInterfaceName+"|2|String;"
                +StationCodes+"|String;"+SignalType+"|String;"+signallevel+"|DateTime;"+startime+"|DateTime;"+endtime;
        String resultData = "";
        try {
            URL url = new URI(warn_url+"iquery="+URLEncoder.encode(iquery,"utf-8")).toURL();
            resultData   = PublicUtil.getResultData(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WrapMapper.ok(Misc.xml2JsonString(resultData));
    }


    /**
     *  获取历史预警数据-- 按时间点查询
     * @param SignalType
     * @param StationCodes
     * @param signallevel
     * @return
     */
    @RequestMapping(value = "/historyByPoint",produces = {"application/json;charset=UTF-8"})
    public Wrapper historyByPoint(@RequestParam String SignalType,
                                  @RequestParam String StationCodes,
                                  @RequestParam String date,
                                  @RequestParam String signallevel){
        String startime = date + "000000";
        String endtime =  date+ "235959";

        String iquery = hisWarnInterfaceName+"|2|String;"
                +StationCodes+"|String;"+SignalType+"|String;"+signallevel+"|DateTime;"+startime+"|DateTime;"+endtime;
        String resultData = "";
        List<Object> list = new ArrayList<>();
        try {

            URL url = new URI(warn_url+"iquery="+URLEncoder.encode(iquery,"utf-8")).toURL();
            resultData   = PublicUtil.getResultData(url);

            JSONObject json = Misc.xml2JsonString(resultData);
            JSONArray array = json.getJSONObject("DATAPACKET").getJSONObject("ROWDATA").getJSONArray("ROW");
            // 查找指定站点的预警数据
            for (int i = 0; i < array.size(); i++) {
                JSONObject eleObj = array.getJSONObject(i);
                if("安徽省".equals(eleObj.get("PROVINCE"))){
                    eleObj.put("ALERTID",String.valueOf(eleObj.get("ALERTID")).substring(7));
                    eleObj.put("ALERTPID",String.valueOf(eleObj.get("ALERTPID")).substring(7));
                    list.add(eleObj);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return WrapMapper.ok(list);
    }

    /**
     * 获取当前有效预警数据
     */
    @RequestMapping(value = "/effect")
    public Wrapper getNowWarnData(){
        return WrapMapper.ok(Misc.xml2JsonString(String.valueOf(redisUtils.get("warndata"))));
    }


    /**
     * 获取当前有效预警数据
     */
    @RequestMapping(value = "/effect/queryList")
    public Wrapper getNowWarnDataByList(){
        List<Object> list = new ArrayList<>();
        try{
            JSONObject json = Misc.xml2JsonString(String.valueOf(redisUtils.get("warndata")));
            JSONArray array = json.getJSONObject("DATAPACKET").getJSONObject("ROWDATA").getJSONArray("ROW");
            // 查找指定站点的预警数据
            for (int i = 0; i < array.size(); i++) {
                JSONObject eleObj = array.getJSONObject(i);
                if("安徽省".equals(eleObj.get("PROVINCE"))){
                    eleObj.put("ALERTID",String.valueOf(eleObj.get("ALERTID")).substring(7));
                    eleObj.put("ALERTPID",String.valueOf(eleObj.get("ALERTPID")).substring(7));
                    list.add(eleObj);
                }
            }
            return WrapMapper.ok(list);
        }catch (Exception e){
            return WrapMapper.ok(list);
        }
    }

    /**
     * 获取当前有效预警数据
     */
    @RequestMapping(value = "/effect/queryListBySort")
    public Wrapper getNowWarnDataByListBySort(@RequestParam String stationId){
        List<Object> list1 = new ArrayList<>();
        List<Object> list2 = new ArrayList<>();
        String stationIds = stationId.substring(0,4);
        try{
            JSONObject json = Misc.xml2JsonString(String.valueOf(redisUtils.get("warndata")));
            JSONArray array = json.getJSONObject("DATAPACKET").getJSONObject("ROWDATA").getJSONArray("ROW");
            List<Map<String,Object>> queryData = (List<Map<String,Object>>) com.alibaba.fastjson.JSONArray.parse(array.toString());
            queryData = this.sortListMap(queryData);
            // 查找指定站点的预警数据
            for (int i = 0; i < queryData.size(); i++) {
                Map<String,Object> eleObj = queryData.get(i);
                if("安徽省".equals(eleObj.get("PROVINCE"))){
                eleObj.put("ALERTID",String.valueOf(eleObj.get("ALERTID")).substring(7));
                eleObj.put("ALERTPID",String.valueOf(eleObj.get("ALERTPID")).substring(7));
                if(stationIds.equals(String.valueOf(eleObj.get("STATIONID")).substring(0,4))){
                    list1.add(eleObj);
                }  else {
                    list2.add(eleObj);
                }}
            }
            list1.addAll(list2);
            return WrapMapper.ok(list1);
        }catch (Exception e){
            return WrapMapper.ok(list1);
        }
    }

    /**
     * 获取当前有效预警数据
     */
    @RequestMapping(value = "/effect/queryByEle")
    public Wrapper queryByEle(@RequestParam String stationId){
       List<Object> list = new ArrayList<>();
       try{
           JSONObject json = Misc.xml2JsonString(String.valueOf(redisUtils.get("warndata")));
           JSONArray array = json.getJSONObject("DATAPACKET").getJSONObject("ROWDATA").getJSONArray("ROW");

           // 查找指定站点的预警数据
           for (int i = 0; i < array.size(); i++) {
               JSONObject eleObj = array.getJSONObject(i);
               if(stationId.equals(String.valueOf(eleObj.get("STATIONID")))){
                   eleObj.put("ALERTID",String.valueOf(eleObj.get("ALERTID")).substring(7));
                   eleObj.put("ALERTPID",String.valueOf(eleObj.get("ALERTPID")).substring(7));
                   list.add(eleObj);
               }
           }
           return WrapMapper.ok(list);
       }catch (Exception e){
            return WrapMapper.ok(list);
       }
    }


    public List<Map<String,Object>>  sortListMap(List<Map<String,Object>> list){
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String date1 = String.valueOf(o1.get("STATIONID"));
                String date2 =  String.valueOf(o2.get("STATIONID"));
                return date1.compareTo(date2);
            }
        });
        return list;
    }
}
