package com.piesat.busiclogic.busic.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.piesat.busiclogic.busic.entities.*;
import com.piesat.busiclogic.busic.productMgr.dao.ProductUnitDao;
import com.piesat.busiclogic.busic.productMgr.entity.ProductUnit;
import com.piesat.busiclogic.busic.repositories.IndexRepository;
import com.piesat.busiclogic.busic.supports.responseExtractor.ForWordResponseExtractor;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.common.weather.TimeUtils;
import com.piesat.common.core.exception.BusinessException;
import com.piesat.common.util.PublicUtil;
import com.piesat.common.util.RedisUtils;
import com.piesat.jdbc.Page;
import com.piesat.jdbc.holder.SpringContextHolder;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class IndexService {

    @Autowired
    private ProductUnitDao productUnitDao;

    @Autowired
    private IndexRepository indexRepository;


    private RedisUtils redisUtils=  (RedisUtils) SpringContextHolder.getApplicationContext().getBean("redisUtils");

    public WeatherData getWeatherData(String stationId, String Hour) throws Exception{
        ProductUnit productUnit1 = new ProductUnit();
        productUnit1.setId("9a96c4ea-d621-4b6f-80701");
        ProductUnit productUnit = productUnitDao.getProductUnitById(productUnit1);
        Calendar ca = Calendar.getInstance();
        Date date ;
        // 处理查询时间
        int hour = ca.get(Calendar.HOUR_OF_DAY);
        if(Integer.valueOf(Hour)> hour){ //当前时间小于发布时间,用前一天数据
            ca.add(Calendar.DATE,-1);
            date = ca.getTime();
        }else{ //当前时间大于发布时间,查询当天数据
            date = ca.getTime();
        }
        String queryDate = new SimpleDateFormat("yyyyMMdd").format(date);
        JSONObject json = JSONObject.fromObject(productUnit.getParam());
        json.put("time",queryDate+"000000");
        json.put("staIds",stationId);
        Map<String,String> map =json;
        String param = Misc.createLinkStringByGet(map);
        URL url = new URL(productUnit.getUrl()+"?"+param);
        String result = PublicUtil.getResultData(url);
        JSONObject JsonResult =  JSONObject.fromObject(result);
        net.sf.json.JSONArray jsonArray = new net.sf.json.JSONArray();
        // 时间处理
        SimpleDateFormat sd =  new SimpleDateFormat("yyyy-MM-dd");
        WeatherData weatherData = new WeatherData();
        if(!PublicUtil.isEmpty(JsonResult.get("DS"))){
            JSONArray jsonDs = JSONArray.parseArray(JsonResult.get("DS").toString());
            Map<String, DayWeather> dayMap = new HashMap<>();
            for (int i = 0; i < jsonDs.size(); i++) {
                Weather weather = new Weather();
                String flagTIme = jsonDs.getJSONObject(i).get("Year")+"-"+
                jsonDs.getJSONObject(i).get("Mon")+"-"+
                jsonDs.getJSONObject(i).get("Day");
                Date dataTime = sd.parse(flagTIme);
                Calendar ca2 = Calendar.getInstance();
                ca2.setTime(dataTime);
                ca2.setFirstDayOfWeek(Calendar.MONDAY);
                ca2.add(Calendar.HOUR, Integer.valueOf(String.valueOf(jsonDs.getJSONObject(i).get("Validtime"))));

                String handleTime =  new SimpleDateFormat("yyyy-MM-dd HH:mm").format(ca2.getTime());
                jsonDs.getJSONObject(i).put("showtime",handleTime);

                if(handleTime.indexOf("00:00")!=-1){ //添加天数据
                    DayWeather dayWeather = new DayWeather();
                    dayWeather.setTime(new SimpleDateFormat("MM-dd").format(ca2.getTime()));
                    dayWeather.setDayOfWeek(String.valueOf(ca2.get(Calendar.DAY_OF_WEEK)));
                    dayWeather.setTem_u(String.valueOf(jsonDs.getJSONObject(i).get("TEM_Min_24h")));
                    dayWeather.setTem_d(String.valueOf(jsonDs.getJSONObject(i).get("TEM_Max_24h")));
                    dayWeather.setWep_u(String.valueOf(jsonDs.getJSONObject(i).get("WEP_Past_12h")));
                    dayWeather.setWin_d_u(Misc.getDirection(String.valueOf(jsonDs.getJSONObject(i).get("WIN_PD_12h"))));
                    dayWeather.setWin_s_u(String.valueOf(jsonDs.getJSONObject(i).get("WIN_S_Max_12h")));
                    dayMap.put(dayWeather.getTime(),dayWeather);
                }

                if(handleTime.indexOf("12:00")!=-1 ){//添加天数据
                    if(!PublicUtil.isEmpty(dayMap.get(new SimpleDateFormat("MM-dd").format(ca2.getTime())))){
                        DayWeather dayWeather =  dayMap.get(new SimpleDateFormat("MM-dd").format(ca2.getTime()));
                        dayWeather.setWin_s_d(String.valueOf(jsonDs.getJSONObject(i).get("WIN_S_Max_12h")));
                        dayWeather.setWep_d(String.valueOf(jsonDs.getJSONObject(i).get("WEP_Past_12h")));
                        dayWeather.setWin_d_d(Misc.getDirection(String.valueOf(jsonDs.getJSONObject(i).get("WIN_PD_12h"))));
                    }
                }
                    // 添加小时数据
                    weather.setTime(handleTime);
                    weather.setWep(String.valueOf(jsonDs.getJSONObject(i).get("WEP")));
                    weather.setPre_pre_fore(String.valueOf(jsonDs.getJSONObject(i).get("PRE_PRE_Fore")));
                    weather.setPrs(String.valueOf(jsonDs.getJSONObject(i).get("PRS")));
                    weather.setRhu(String.valueOf(jsonDs.getJSONObject(i).get("RHU")));
                    weather.setWin_d(Misc.getDirection(String.valueOf(jsonDs.getJSONObject(i).get("WIN_PD_12h"))));
                    weather.setWin_s(String.valueOf(jsonDs.getJSONObject(i).get("WIN_S")));
                    weather.setTem(String.valueOf(jsonDs.getJSONObject(i).get("TEM")));
                    weatherData.getHourData().add(weather);
            }
            List lit = new ArrayList();
            Object[] key = dayMap.keySet().toArray();
            Arrays.sort(key);
            for (int i = 0; i < key.length; i++) {
                lit.add(dayMap.get(key[i]));
            }
            weatherData.setHalfDayData(lit);
        }
        return weatherData;
    }

    public WeatherDataGrid getWeatherForcast(String stationId) throws Exception{
        // 获取token
        String token = this.getToken();
        WeatherDataGrid weatherData = new WeatherDataGrid();
        Map<String,String> paramMap = new HashMap<>();
        if(!PublicUtil.isEmpty(token)){
            // 获取24小时预测数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String dateNowStr = sdf.format(date);
            Calendar now = Calendar.getInstance();

            // 获取24小时预报数据
            now.setTime(date);
            now.setFirstDayOfWeek(Calendar.MONDAY);
            now.add(Calendar.DAY_OF_MONTH, -1);

            // 当前时间的前一天
            String date1 = sdf.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 7);
            // 当前时间的后7天
            String date7 = sdf.format(now.getTime());

            // 预报接口参数
            paramMap.put("InitialTime",date1+"200000");
            paramMap.put("QueryStartTime",date1+"235900");
            paramMap.put("QueryEndTime",date7+"235900");
            paramMap.put("StaIDs","["+stationId+"]");
            paramMap.put("Token",token);

            URL url3hforest = new URL(Misc.getPropValue("transparent.properties","forcast_3h") + Misc.createLinkStringByGet(paramMap));
            JSONObject result3 = this.getDataFromInterfaceOrRedis(stationId,paramMap.get("InitialTime"),"3h",url3hforest);
            paramMap.put("QueryStartTime",date1+"220000");
            URL url24hforest = new URL(Misc.getPropValue("transparent.properties","forcast_24h") + Misc.createLinkStringByGet(paramMap));
            JSONObject result24 = this.getDataFromInterfaceOrRedis(stationId,paramMap.get("InitialTime"),"24h",url24hforest);
//            JSONObject result24 = JSONObject.fromObject(PublicUtil.getResultData(url24hforest));
            // 预报天气结构集封装
            net.sf.json.JSONArray JsonArray24h = result24.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleDayValues");
            net.sf.json.JSONArray JsonArray3h = result3.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleHourValues");
            DecimalFormat dfformat = new DecimalFormat("0.0");
            for (int i = 0; i < JsonArray24h.size(); i++) {  // 日数据赋值
                JSONObject jo = JsonArray24h.getJSONObject(i);
                DayWeather dayWeather = new DayWeather();
                dayWeather.setTem_d(String.valueOf(jo.get("MaxTemperature")));
                dayWeather.setTem_u(String.valueOf(jo.get("MinTemperature")));
                dayWeather.setWep_u(String.valueOf(jo.getJSONObject("Detail_12").get("WeatherCode")));
                dayWeather.setWep_d(String.valueOf(jo.getJSONObject("Detail_24").get("WeatherCode")));
                dayWeather.setPre_sum(String.valueOf(jo.get("Precipitation")));

                Calendar now1 = Calendar.getInstance();
                Date d = sdf5.parse(String.valueOf(jo.get("Date")).substring(0,8));
                now1.setTime(d);
                now1.setFirstDayOfWeek(Calendar.MONDAY);
                dayWeather.setDayOfWeek(String.valueOf(now1.get(Calendar.DAY_OF_WEEK)));
                dayWeather.setTime(sdf2.format(sdf4.parse(String.valueOf(jo.get("Date")))));
                dayWeather.setWin_s_u(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_12").get("WindSpeed")))));
                dayWeather.setWin_s_d(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_24").get("WindSpeed")))));
                dayWeather.setWin_d_u(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_12").get("WindDirection"))));
                dayWeather.setWin_d_d(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_24").get("WindDirection"))));
                weatherData.getHalfDayData().add(dayWeather);
            }

            for (int i = 0; i < JsonArray3h.size(); i++) { //3 小时数据赋值
                JSONObject jo = JsonArray3h.getJSONObject(i);
                Weather weather = new Weather();
                weather.setTime(sdf3.format(sdf4.parse(String.valueOf(jo.get("Time")))));
                weather.setTem(String.valueOf(jo.get("Tmp")));
                weather.setWep(String.valueOf(jo.get("WeatherCode")));
                weather.setWin_s(dfformat.format(new BigDecimal(jo.getString("WindSpeed"))));
                weather.setPre_pre_fore("0.0".equals(jo.getString("Er03"))?"0":jo.getString("Er03"));
                weather.setWin_d(Misc.getDirection(String.valueOf(jo.get("WindDirection"))));
                weatherData.getHourData().add(weather);
            }
        }
        return  weatherData;
    }

    public WeatherDataGrid getWeatherForcastFor(String stationId) throws Exception{
        // 获取token
        String token = this.getToken();
        WeatherDataGrid weatherData = new WeatherDataGrid();
        Map<String,String> paramMap = new HashMap<>();
        if(!PublicUtil.isEmpty(token)){
            // 获取24小时预测数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String dateNowStr = sdf.format(date);
            Calendar now = Calendar.getInstance();

            // 获取24小时预报数据
            now.setTime(date);
            now.setFirstDayOfWeek(Calendar.MONDAY);

            // 当前时间的前一天
            now.add(Calendar.DAY_OF_MONTH, -1);
            String date1 = sdf.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
            String date2 = sdf4.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 8);
            // 当前时间的后7天
            String date7 = sdf.format(now.getTime());

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(new Date());
            startCal.add(Calendar.HOUR_OF_DAY,8);

            // 由站点查询 经纬度信息
            Map<String,Object> map =  indexRepository.getLatAndLon(stationId);
            String LAT = String.valueOf(map.get("LAT"));
            String LON = String.valueOf(map.get("LON"));


            // 预报接口参数
            paramMap.put("InitialTime",date1+"200000");
            paramMap.put("QueryStartTime",sdf4.format(startCal.getTime()));
            paramMap.put("QueryEndTime",date7+"235900");
//            paramMap.put("StaIDs","["+stationId+"]");
            paramMap.put("Latlons","[["+LAT+","+LON+"]]");
            paramMap.put("Token",token);

            URL url3hforest = new URL(Misc.getPropValue("transparent.properties","forcast_3h_latLon") + Misc.createLinkStringByGet(paramMap));
            JSONObject result3 = JSONObject.fromObject(PublicUtil.getResultData(url3hforest));
            paramMap.put("QueryStartTime",date1+"235900");
            URL url24hforest = new URL(Misc.getPropValue("transparent.properties","forcast_24h_latLon") + Misc.createLinkStringByGet(paramMap));
            JSONObject result24 = JSONObject.fromObject(PublicUtil.getResultData(url24hforest));
            // 预报天气结构集封装
            net.sf.json.JSONArray JsonArray24h = result24.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleDayValues");
            net.sf.json.JSONArray JsonArray3h = result3.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleHourValues");
            DecimalFormat dfformat = new DecimalFormat("0.0");
            for (int i = 0; i < JsonArray24h.size(); i++) {  // 日数据赋值
                JSONObject jo = JsonArray24h.getJSONObject(i);
                DayWeather dayWeather = new DayWeather();
                dayWeather.setTem_d(String.valueOf(jo.get("MaxTemperature")));
                dayWeather.setTem_u(String.valueOf(jo.get("MinTemperature")));
                dayWeather.setWep_u(String.valueOf(jo.getJSONObject("Detail_12").get("WeatherCode")));
                dayWeather.setWep_d(String.valueOf(jo.getJSONObject("Detail_24").get("WeatherCode")));
                dayWeather.setPre_sum(String.valueOf(jo.get("Precipitation")));

                Calendar now1 = Calendar.getInstance();
                Date d = sdf5.parse(String.valueOf(jo.get("Date")).substring(0,8));
                now1.setTime(d);
                now1.setFirstDayOfWeek(Calendar.MONDAY);
                dayWeather.setDayOfWeek(String.valueOf(now1.get(Calendar.DAY_OF_WEEK)));
                dayWeather.setTime(sdf2.format(sdf4.parse(String.valueOf(jo.get("Date")))));
                dayWeather.setWin_s_u(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_12").get("WindSpeed")))));
                dayWeather.setWin_s_d(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_24").get("WindSpeed")))));
                dayWeather.setWin_d_u(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_12").get("WindDirection"))));
                dayWeather.setWin_d_d(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_24").get("WindDirection"))));
                weatherData.getHalfDayData().add(dayWeather);
            }

            for (int i = 0; i < JsonArray3h.size(); i++) { //3 小时数据赋值
                JSONObject jo = JsonArray3h.getJSONObject(i);
                Weather weather = new Weather();
                weather.setTime(sdf3.format(sdf4.parse(String.valueOf(jo.get("Time")))));
                weather.setTem(String.valueOf(jo.get("Tmp")));
                weather.setWep(String.valueOf(jo.get("WeatherCode")));
                weather.setWin_s(dfformat.format(new BigDecimal(jo.getString("WindSpeed"))));
                weather.setPre_pre_fore("0.0".equals(jo.getString("Er03"))?"0":jo.getString("Er03"));
                weather.setWin_d(Misc.getDirection(String.valueOf(jo.get("WindDirection"))));
                weatherData.getHourData().add(weather);
            }
        }
        return  weatherData;
    }



    public WeatherDataGrid getWeatherForcastForGis(String stationId,String endtime) throws Exception{
        // 获取token
        String token = this.getToken();
        WeatherDataGrid weatherData = new WeatherDataGrid();
        Map<String,String> paramMap = new HashMap<>();
        if(!PublicUtil.isEmpty(token)){
            // 获取24小时预测数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String dateNowStr = sdf.format(date);
            Calendar now = Calendar.getInstance();

            // 获取24小时预报数据
            now.setTime(date);
            now.setFirstDayOfWeek(Calendar.MONDAY);
            now.add(Calendar.DAY_OF_MONTH, -1);
            // 当前时间的前一天
            String date1 = sdf.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 1);
            String date2 = sdf4.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 8);
            // 当前时间的后7天
            String date7 = sdf.format(now.getTime());

            Calendar startCal = Calendar.getInstance();
            startCal.setTime(new Date());
            startCal.add(Calendar.HOUR_OF_DAY,8);

            // 预报接口参数
            paramMap.put("InitialTime",date1+"200000");
            paramMap.put("QueryStartTime",sdf4.format(startCal.getTime()));
            paramMap.put("QueryEndTime",endtime);
//            paramMap.put("StaIDs","["+stationId+"]");
            paramMap.put("Token",token);
            // 由站点查询 经纬度信息
            Map<String,Object> map =  indexRepository.getLatAndLons(stationId);
            String LAT = String.valueOf(map.get("LAT"));
            String LON = String.valueOf(map.get("LON"));
            paramMap.put("Latlons","[["+LAT+","+LON+"]]");

            URL url3hforest = new URL(Misc.getPropValue("transparent.properties","forcast_3h_latLon") + Misc.createLinkStringByGet(paramMap));
            JSONObject result3 = JSONObject.fromObject(PublicUtil.getResultData(url3hforest));
            paramMap.put("QueryStartTime",date1+"235900");
            URL url24hforest = new URL(Misc.getPropValue("transparent.properties","forcast_24h_latLon") + Misc.createLinkStringByGet(paramMap));
            JSONObject result24 = JSONObject.fromObject(PublicUtil.getResultData(url24hforest));
            // 预报天气结构集封装
            net.sf.json.JSONArray JsonArray24h = result24.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleDayValues");
            net.sf.json.JSONArray JsonArray3h = result3.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleHourValues");
            DecimalFormat dfformat = new DecimalFormat("0.0");
            for (int i = 0; i < JsonArray24h.size(); i++) {  // 日数据赋值
                JSONObject jo = JsonArray24h.getJSONObject(i);
                DayWeather dayWeather = new DayWeather();
                dayWeather.setTem_d(String.valueOf(jo.get("MaxTemperature")));
                dayWeather.setTem_u(String.valueOf(jo.get("MinTemperature")));
                dayWeather.setWep_u(String.valueOf(jo.getJSONObject("Detail_12").get("WeatherCode")));
                dayWeather.setWep_d(String.valueOf(jo.getJSONObject("Detail_24").get("WeatherCode")));
                dayWeather.setPre_sum(String.valueOf(jo.get("Precipitation")));
                Calendar now1 = Calendar.getInstance();
                Date d = sdf5.parse(String.valueOf(jo.get("Date")).substring(0,8));
                now1.setTime(d);
                now1.setFirstDayOfWeek(Calendar.MONDAY);
                dayWeather.setDayOfWeek(String.valueOf(now1.get(Calendar.DAY_OF_WEEK)));
                dayWeather.setTime(sdf2.format(sdf4.parse(String.valueOf(jo.get("Date")))));
                dayWeather.setWin_s_u(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_12").get("WindSpeed")))));
                dayWeather.setWin_s_d(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_24").get("WindSpeed")))));
                dayWeather.setWin_d_u(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_12").get("WindDirection"))));
                dayWeather.setWin_d_d(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_24").get("WindDirection"))));
                weatherData.getHalfDayData().add(dayWeather);
            }

            for (int i = 0; i < JsonArray3h.size(); i++) { //3 小时数据赋值
                JSONObject jo = JsonArray3h.getJSONObject(i);
                Weather weather = new Weather();
                weather.setTime(sdf3.format(sdf4.parse(String.valueOf(jo.get("Time")))));
                weather.setTem(String.valueOf(jo.get("Tmp")));
                weather.setWep(String.valueOf(jo.get("WeatherCode")));
                weather.setWin_s(dfformat.format(new BigDecimal(jo.getString("WindSpeed"))));
                weather.setPre_pre_fore("0.0".equals(jo.getString("Er03"))?"0":jo.getString("Er03"));
                weather.setWin_d(Misc.getDirection(String.valueOf(jo.get("WindDirection"))));
                weatherData.getHourData().add(weather);
            }
        }
        return  weatherData;
    }


    public WeatherDataGrid getWeatherDataByLonAndLat(String lat, String lon) throws Exception {
        String token = this.getToken();
        WeatherDataGrid weatherData = new WeatherDataGrid();
        Map<String,String> paramMap = new HashMap<>();
        if(!PublicUtil.isEmpty(token)){
            // 获取24小时预测数据
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            SimpleDateFormat sdf4 = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdf5 = new SimpleDateFormat("yyyyMMdd");
            Date date = new Date();
            String dateNowStr = sdf.format(date);
            Calendar now = Calendar.getInstance();

            // 获取24小时预报数据
            now.setTime(date);
            now.setFirstDayOfWeek(Calendar.MONDAY);
            now.add(Calendar.DAY_OF_MONTH, -1);

            // 当前时间的前一天
            String date1 = sdf.format(now.getTime());
            now.add(Calendar.DAY_OF_MONTH, 7);
            // 当前时间的后7天
            String date7 = sdf.format(now.getTime());

            // 预报接口参数
            paramMap.put("InitialTime",date1+"200000");
            paramMap.put("QueryStartTime",date1+"235900");
            paramMap.put("QueryEndTime",date7+"235900");
            paramMap.put("Latlons","[["+lat+","+lon+"]]");
            paramMap.put("Token",token);

            URL url3hforest = new URL(Misc.getPropValue("transparent.properties","forcast_3h_latLon") + Misc.createLinkStringByGet(paramMap));

            JSONObject result3 = JSONObject.fromObject(PublicUtil.getResultData(url3hforest));
            paramMap.put("QueryStartTime",date1+"220000");
            URL url24hforest = new URL(Misc.getPropValue("transparent.properties","forcast_24h_latLon") + Misc.createLinkStringByGet(paramMap));
            JSONObject result24 = JSONObject.fromObject(PublicUtil.getResultData(url24hforest));
            // 预报天气结构集封装
            net.sf.json.JSONArray JsonArray24h = result24.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleDayValues");
            net.sf.json.JSONArray JsonArray3h = result3.getJSONArray("Datas").getJSONObject(0).getJSONArray("MultipleHourValues");
            DecimalFormat dfformat = new DecimalFormat("0.0");
            for (int i = 0; i < JsonArray24h.size(); i++) {  // 日数据赋值
                JSONObject jo = JsonArray24h.getJSONObject(i);
                DayWeather dayWeather = new DayWeather();
                dayWeather.setTem_d(String.valueOf(jo.get("MaxTemperature")));
                dayWeather.setTem_u(String.valueOf(jo.get("MinTemperature")));
                dayWeather.setWep_u(String.valueOf(jo.getJSONObject("Detail_12").get("WeatherCode")));
                dayWeather.setWep_d(String.valueOf(jo.getJSONObject("Detail_24").get("WeatherCode")));

                Calendar now1 = Calendar.getInstance();
                Date d = sdf5.parse(String.valueOf(jo.get("Date")).substring(0,8));
                now1.setTime(d);
                now1.setFirstDayOfWeek(Calendar.MONDAY);
                dayWeather.setDayOfWeek(String.valueOf(now1.get(Calendar.DAY_OF_WEEK)));
                dayWeather.setTime(sdf2.format(sdf4.parse(String.valueOf(jo.get("Date")))));
                dayWeather.setWin_s_u(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_12").get("WindSpeed")))));
                dayWeather.setWin_s_d(dfformat.format(new BigDecimal(String.valueOf(jo.getJSONObject("Detail_24").get("WindSpeed")))));
                dayWeather.setWin_d_u(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_12").get("WindDirection"))));
                dayWeather.setWin_d_d(Misc.getDirection(String.valueOf(jo.getJSONObject("Detail_24").get("WindDirection"))));
                dayWeather.setPre_sum(String.valueOf(jo.get("Precipitation")));
                weatherData.getHalfDayData().add(dayWeather);
            }

            for (int i = 0; i < JsonArray3h.size(); i++) { //3 小时数据赋值
                JSONObject jo = JsonArray3h.getJSONObject(i);
                Weather weather = new Weather();
                weather.setTime(sdf3.format(sdf4.parse(String.valueOf(jo.get("Time")))));
                weather.setTem(String.valueOf(jo.get("Tmp")));
                weather.setWep(String.valueOf(jo.get("WeatherCode")));
                weather.setWin_s(dfformat.format(new BigDecimal(jo.getString("WindSpeed"))));
                weather.setPre_pre_fore("0.0".equals(jo.getString("Er03"))?"0":jo.getString("Er03"));
                weather.setWin_d(Misc.getDirection(String.valueOf(jo.get("WindDirection"))));
                weatherData.getHourData().add(weather);
            }
        }
        return  weatherData;
    }

    /**
     * 获取token
     * @return
     * @throws Exception
     */
    public String getToken() throws Exception{
        // 获取token
        URL urlToken = new URL( Misc.getPropValue("transparent.properties","forcast_token"));
        JSONObject tokenData = JSONObject.fromObject(PublicUtil.getResultData(urlToken));
        return String.valueOf(JSONObject.fromObject(tokenData.get("Datas")).get("Token"));
    }




    public Map<String,String> getSunData(String stationId) {
        Map<String,Object> map =  indexRepository.getLatAndLon(stationId);
        Calendar now = Calendar.getInstance();
        String LAT = String.valueOf(map.get("LAT"));
        String LON = String.valueOf(map.get("LON"));
        Date[] d =    TimeUtils.calcSunriseAndSunset(now.get(Calendar.YEAR),now.get(Calendar.MONTH),now.get(Calendar.DAY_OF_MONTH),
            Double.valueOf(LAT.substring(0,2)+"."+LAT.substring(2)),Double.valueOf(LON.substring(0,3)+"." + LON.substring(3)));
        Map<String,String> rmap = new HashMap<>();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        rmap.put("sTime",sf.format(d[0]));
        rmap.put("eTime",sf.format(d[1]));
        return  rmap;
    }

    public void addIndexAdvert(IndexAdvert indexAdvert) {
        indexRepository.addIndexAdvert(indexAdvert);
    }

    public void editIndexAdvert(IndexAdvert indexAdvert) {
        if(PublicUtil.isEmpty(indexAdvert.getId())){
            throw new BusinessException("缺少参数id");
        }
        indexRepository.editIndexAdvert(indexAdvert);
    }

    public void deleteIndexAdvert(String ids) {
        indexRepository.deleteIndexAdvert(ids);
    }

    public List<Map<String,Object>> getIndexProduct() {
        return indexRepository.getIndexProduct();
    }

    public List<Map<String,Object>> getHotProduct() {
        return indexRepository.getHotProduct();
    }


    public Page<IndexAdvert> getHotProduct(long currentPage, int pageSize, IndexAdvert indexAdvert) {
        return indexRepository.getIndexAdvertList(currentPage,pageSize,indexAdvert);
    }


    public Page<IndexAdvert> getIndexAdvertList(long currentPage, int pageSize, IndexAdvert indexAdvert) {
        return indexRepository.getIndexAdvertList(currentPage,pageSize,indexAdvert);
    }

    public void addIndexRotation(String irArray) throws Exception{
        ObjectMapper mapper = new ObjectMapper();//定义 org.codehaus.jackson
        List<IndexRotation> indexRotationList = mapper.readValue(irArray,new TypeReference<List<IndexRotation>>() { });
        for (int i = 0; i < indexRotationList.size(); i++) {
            indexRepository.addIndexRotation(indexRotationList.get(i));
        }
    }

    public void editIndexRotation(IndexRotation indexRotation) {
        if(PublicUtil.isEmpty(indexRotation.getId())){
            throw new BusinessException("缺少参数id");
        }
        indexRepository.editIndexRotation(indexRotation);
    }

    public void deleteIndexRotation(String ids) {
        indexRepository.deleteIndexRotation(ids);
    }

    public List<Map<String,Object>>getIndexRotationList(IndexRotation indexRotation, HttpServletRequest request) throws Exception {
        List<Map<String,Object>> rlist = indexRepository.getIndexRotationList(indexRotation);
        for (int i = 0; i < rlist.size(); i++) {
            ProductUnit p = new ProductUnit();
            p.setId(String.valueOf(rlist.get(i).get("PRO_ID")));
            ProductUnit productUnit = productUnitDao.getProductUnitById(p);
            String url = this.getFileUrl(productUnit.getFms_id(),productUnit.getUrl_type(),rlist.get(i),request);
            rlist.get(i).put("url",url);
        }
        return rlist;
    }

    @Transactional
    public void addIndexDataColumn(String idArray) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<IndexColum> indexColumList = mapper.readValue(idArray,new TypeReference<List<IndexColum>>() { });
        indexRepository.deleteIndexCalum();
        indexRepository.insertIndexCalum(indexColumList);
    }

    public List<Map<String,Object>> getIndexDataColumn() throws Exception{
        List<Map<String,Object>> ColumnList = indexRepository.getIndexDataColumn();
        ColumnList = Misc.recursion("0",ColumnList);
        List<Map<String,Object>> resultList = new ArrayList<>();
        for (int i = 0; i < ColumnList.size(); i++) {
            if(ColumnList.get(i).containsKey("childrenList")){
                resultList.addAll((List<Map<String,Object>>)ColumnList.get(i).get("childrenList"));
            }else{
                resultList.add(ColumnList.get(i));
            }
        }
       return resultList ;
    }

    public void addDataBase(String id, String base64)  throws Exception{
        indexRepository.addDataBase(id,base64);
    }

    public List<Map<String,Object>> getIndexDataTree() throws Exception{
        List<Map<String,Object>> indexList  = indexRepository.getIndexData();
        // 默认查询所有产品树数据
        indexList = Misc.recursion("0",indexList);
        return indexList;
    }

    public void setIndexAdvert(String status, String id) {
        indexRepository.setIndexAdvert(status,id);
    }


    // 远程调用文件管理系统接口
    public String getFileUrl(String fileId,String urlType,Map<String,Object> strMap,HttpServletRequest request) throws Exception{
        Map<String,String> map = new HashMap<>();
        String param ="";
            Map body = new HashMap();
            Date d = new Date();
            body = (Map)JSON.parse(String.valueOf(strMap.get("URL")));
            SimpleDateFormat s = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat s1 = new SimpleDateFormat("yyyyMM");
            if("913ccf8d-647e-4e17-8600-b729606d11b5".equals(strMap.get("PRODUCT_ID")) || "732cec8c-f725-4c80-84a1-77d8f9db4e02".equals(strMap.get("PRODUCT_ID"))) {
                body.put("times", s1.format(d));
            }
            else if("46762595-a5c2-42cb-8da8-22a7a9067fac".equals(strMap.get("PRODUCT_ID"))){
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DATE,-1);
                body.put("times",s.format(c.getTime()));
            }
            else{
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                SimpleDateFormat s2 = new SimpleDateFormat("yyyyMMdd");
                body.put("times",s2.format(c.getTime()));
            }
        map.put("body",String.valueOf(JSONObject.fromObject(body)));
        map.put("url",Misc.getPropValue("transparent.properties","file_point_url"));
        map.put("id",String.valueOf(strMap.get("PRO_ID")));
        map.put("httpMethod","GET");
        param = Misc.createLinkStringByGet(map);
        String url = Misc.getPropValue("transparent.properties","file_point_index");

        // 遍历请求头
        MultiValueMap<String,String> multiValueMap = new HttpHeaders();
        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);

            multiValueMap.add(key, value);
        }
        RestTemplate restTemplate = new RestTemplate();

        String queryUrl = url + (StringUtils.endsWith(url, "?") ? "":"?") + param;
        HttpEntity request1 = new HttpEntity(multiValueMap);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(request1);
        ForWordResponseExtractor forWordResponseExtractor = new ForWordResponseExtractor();
        String data = restTemplate.execute(new URI(queryUrl),  HttpMethod.GET, requestCallback, forWordResponseExtractor);
        JSONObject jsonObject=JSONObject.fromObject(data);
        String file_url = "";
        try{
//            if("0".equals(urlType)){
                file_url = String.valueOf(jsonObject.getJSONObject("result").getJSONArray("result").getJSONObject(0).get("FileUrl"));
//            }else {
//                file_url = String.valueOf(jsonObject.getJSONObject("result").getJSONArray("DS").getJSONObject(0).get("FILE_URL"));
//            }
        }catch (Exception e){
        }
        return file_url;
    }

    public JSONObject getDataFromInterfaceOrRedis(String StationId,String startTime,String type,URL url){
        String redisKey = type + StationId + startTime;
        if(!PublicUtil.isEmpty(redisUtils.get(redisKey))){
            JSONObject jon = (JSONObject)redisUtils.get(redisKey);
            if (jon.get("Datas").equals("null")){
                redisUtils.remove(redisKey);
            }
            return jon;
        }else {
            JSONObject jsonOdata = JSONObject.fromObject(PublicUtil.getResultData(url));
            redisUtils.set(redisKey,jsonOdata);
            return jsonOdata;
        }
    }
}
