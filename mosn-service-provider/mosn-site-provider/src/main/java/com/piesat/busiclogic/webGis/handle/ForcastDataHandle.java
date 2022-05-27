package com.piesat.busiclogic.webGis.handle;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.util.PublicUtil;
import net.sf.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

public class ForcastDataHandle {

    public static JSONObject forcastMatrix(String initTime, String queryTime,String ModeCode,String elementCode, String forecastLevel) throws Exception {

        // 获取token
        URL urlToken = new URL(Misc.getPropValue("transparent.properties","forcast_token"));
        JSONObject tokenData = JSONObject.fromObject(PublicUtil.getResultData(urlToken));
        JSONObject result = new JSONObject();
        if(!PublicUtil.isEmpty(tokenData) && !PublicUtil.isEmpty(JSONObject.fromObject(tokenData.get("Datas")).get("Token"))){
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("Token",String.valueOf(JSONObject.fromObject(tokenData.get("Datas")).get("Token")));
            String lonlatArea = Misc.getPropValue("transparent.properties","coordinate");
            String[] lonlat = lonlatArea.split(",");
            paramMap.put("InitialTime",initTime);
            paramMap.put("ModeCode","SPCC");
            paramMap.put("MemberCode","BEHF");
            paramMap.put("ElementCode",elementCode);
            paramMap.put("TargetTime",queryTime);
            paramMap.put("ForecastLevel",forecastLevel);
            paramMap.put("MinLon",lonlat[0]);
            paramMap.put("MinLat",lonlat[1]);
            paramMap.put("MaxLon",lonlat[2]);
            paramMap.put("MaxLat",lonlat[3]);
            URL url3hforest = new URL(Misc.getPropValue("transparent.properties","forcast_matrix") + Misc.createLinkStringByGet(paramMap));
            result = JSONObject.fromObject(PublicUtil.getResultData(url3hforest));
        }
        return  result;
    }


    /**
     * 获取未来3 ~  168 小时预报数据
     */
    public static Map<String,JSONObject> get3T72ForcastData(String initDate) throws Exception{
        Map<String,JSONObject> jsonMap = new HashMap<>();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(sf.parse(initDate));
        for(int i = 0;i<56;i++){
            calendar.add(Calendar.HOUR_OF_DAY,3);
            JSONObject eleJson = ForcastDataHandle.forcastMatrix(initDate,sf.format(calendar.getTime()),"SPCC","ER03","1010");
            jsonMap.put(sf.format(calendar.getTime()),eleJson);
        }
        return jsonMap;
    }
}
