package com.piesat.busiclogic.webGis.web;

import com.piesat.busiclogic.busic.entities.DayWeather;
import com.piesat.busiclogic.busic.entities.Weather;
import com.piesat.busiclogic.busic.entities.WeatherDataGrid;
import com.piesat.busiclogic.busic.services.IndexService;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.entity.CimissQueryParam;
import com.piesat.busiclogic.webGis.entity.TyphoonData;
import com.piesat.busiclogic.webGis.entity.TyphoonDataPoint;
import com.piesat.busiclogic.webGis.handle.StationDataService;
import com.piesat.cimiss.common.util.CimissUtils;
import com.piesat.common.anno.Description;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * webgis站点数据web
 */
@RestController
@CrossOrigin
@RequestMapping("/noAuth/cimiss")
public class StationDataController {

    @Autowired
    private StationDataService stationDataService;

    @Autowired
    private IndexService indexService;


    @Description("获取cimiss站点数据")
    @RequestMapping(value = "/searchdata", method = RequestMethod.POST)
    public Wrapper searchData(@RequestBody CimissQueryParam cimissQueryParam) {
        try {
            List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
            return WrapMapper.ok(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("获取站点的实时数据")
    @RequestMapping(value = "/getStationData", method = RequestMethod.POST)
    public Wrapper searchDataByStationId(CimissQueryParam cimissQueryParam) {
        try {
            List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
            List<Map<String,String>> dataList = stationDataService.handleDirecition(resultList);
            return WrapMapper.ok(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }



    @Description("获取站点的实时数据")
    @RequestMapping(value = "/getStationDataMix", method = RequestMethod.GET)
    public Wrapper getStationDataMix(CimissQueryParam cimissQueryParam) {
        try {
            String timeRange = cimissQueryParam.getTimeRang();
            String endtime= "";
            if(timeRange.indexOf("]") != -1){
                endtime = timeRange.substring(timeRange.indexOf(",")+1,timeRange.indexOf("]"));
            }else{
                endtime = timeRange.substring(timeRange.indexOf(",")+1,timeRange.indexOf(")"));
            }
            SimpleDateFormat sf  = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sfr1  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy");

            Date endDate = sf.parse(endtime);
            Date dnow = new Date();
            String year = sdf1.format(dnow);
            List<Map<String,String>> resultList = new ArrayList<>();
            Boolean flag =  endDate.before(dnow);
            resultList = stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
            if(flag == false) {
                WeatherDataGrid weatherDataGrid = new WeatherDataGrid();
                if (endtime.equals(sf.format(dnow))) {  // 结束时间和当前时间相同，返回未来数据
                    try{
                        weatherDataGrid = indexService.getWeatherForcastFor(cimissQueryParam.getStaIds());
                    }catch (Exception e){
                    }
                    if ("SURF_CHN_MUL_HOR".equals(cimissQueryParam.getDataCode())) {
                        List<Weather> hourForcast =  weatherDataGrid.getHourData();
                        for (int i = 0; i < hourForcast.size(); i++) {
                            Map m = new HashMap();
                            Weather weather = hourForcast.get(i);
                            m.put("PRE_1h",weather.getPre_pre_fore());
                            m.put("WIN_S_INST",weather.getWin_s());
                            m.put("TEM",weather.getTem());
                            m.put("Datetime",weather.getTime());
                            resultList.add(m);
                        }
                    } else {
                        List<DayWeather> dayForcast =  weatherDataGrid.getHalfDayData();
                        Map map = resultList.get(resultList.size()-1);
                        if(map.containsKey("PRE_Time_0808") && "--".equals(map.get("PRE_Time_0808"))){
                            resultList.remove(resultList.size()-1);
                        }

                        for (int i = 0; i < dayForcast.size(); i++) {
                            Map m = new HashMap();
                            DayWeather dayWeather = dayForcast.get(i);
                            m.put("PRE_Time_0808",dayWeather.getPre_sum());
                            m.put("PRE_Time_2020",dayWeather.getPre_sum());
                            m.put("TEM_Max",dayWeather.getTem_d());
                            m.put("TEM_Min",dayWeather.getTem_u());
                            m.put("WIN_S_Max",dayWeather.getWin_s_u());
                            String time = year + "-"+dayWeather.getTime() + " 00:00:00";
                            m.put("Datetime",time);
                            resultList.add(m);
                        }
                    }
                } else {  // 选择指定预报时间
                    try{
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(endDate);
                        calendar.add(calendar.HOUR_OF_DAY, 8);
                        weatherDataGrid = indexService.getWeatherForcastForGis(cimissQueryParam.getStaIds(),sf.format(calendar.getTime()));
                    }catch (Exception e){
                    }
                    if ("SURF_CHN_MUL_HOR".equals(cimissQueryParam.getDataCode())) {
                        List<Weather> hourForcast =  weatherDataGrid.getHourData();
                        for (int i = 0; i < hourForcast.size(); i++) {
                            Map m = new HashMap();
                            Weather weather = hourForcast.get(i);
                            m.put("PRE_1h",weather.getPre_pre_fore());
                            m.put("WIN_S_Max",weather.getWin_s());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(sfr1.parse(weather.getTime()+":00"));
                            calendar.add(calendar.HOUR_OF_DAY, -8);
                            m.put("TEM",weather.getTem());
                            m.put("Datetime",sfr1.format(calendar.getTime()));
                            resultList.add(m);
                        }
                    } else {
                        Map map = resultList.get(resultList.size()-1);
                        if(map.containsKey("PRE_Time_0808") && "--".equals(map.get("PRE_Time_0808"))){
                            resultList.remove(resultList.size()-1);
                        }
                        List<DayWeather> dayForcast =  weatherDataGrid.getHalfDayData();
                        for (int i = 0; i < dayForcast.size(); i++) {
                            Map m = new HashMap();
                            DayWeather dayWeather = dayForcast.get(i);
                            m.put("PRE_Time_0808",dayWeather.getPre_sum());
                            m.put("PRE_Time_2020",dayWeather.getPre_sum());
                            m.put("TEM_Max",dayWeather.getTem_d());
                            m.put("TEM_Min",dayWeather.getTem_u());
                            m.put("WIN_S_Max",dayWeather.getWin_s_u());
                            String time = year + "-"+dayWeather.getTime() + " 00:00:00";
                            m.put("Datetime",time);
                            resultList.add(m);
                        }
                    }
                }
            }
            return WrapMapper.ok(resultList);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("获取站点的逐小时累计数据-针对过程降水")
    @RequestMapping(value = "/getStationDataSum", method = RequestMethod.GET)
    public Wrapper getStationDataSum(CimissQueryParam cimissQueryParam) {
        try {
            List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
            List<Map<String,String>> dataList = this.handlePrecudureData(resultList);
            return WrapMapper.ok(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("获取台风编号")
    @RequestMapping(value = "/gettyphoonNumber", method = RequestMethod.GET)
    public Wrapper searchtyphoonNumber(String times) {
        try {
            CimissQueryParam cimissQueryParam = new CimissQueryParam();
            cimissQueryParam.setInterfaceId("getTyphInfoByTimeRange");
            SimpleDateFormat sf = new SimpleDateFormat("yyyy");
            Date dnow = new Date();
            cimissQueryParam.setDataCode("SEVP_WEFC_TYP_WT");

            if(times.equals(sf.format(dnow))){
                cimissQueryParam.setTimeRang(this.handleDate(times));
            }else {
                cimissQueryParam.setTimeRang(this.handleDate(times));
            }
            List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
            List<Map<String,String>> dataList = this.handleTypyoonNumber(resultList);
            dataList = Misc.sortListMapDesc(dataList,"NUM_NATI");
            List<Map<String,String>> returnList = this.excluteExceptionData(dataList);
            return WrapMapper.ok(returnList);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    private List<Map<String, String>> excluteExceptionData(List<Map<String, String>> dataList) {
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < dataList.size(); i++) {
            Map map = dataList.get(i);
            String num = (String) map.get("NUM_NATI");
            if(num.length()>=4){
                list.add(map);
            }
        }
        return list;
    }

    @Description("获取台风数据")
    @RequestMapping(value = "/gettyphoonData", method = RequestMethod.GET)
    public Wrapper searchtyphoonData(String times,String typhCIds) {
        try {
           CimissQueryParam cimissQueryParam = new CimissQueryParam();
           cimissQueryParam.setTimeRang(this.handleDate(times));
           cimissQueryParam.setElements(Misc.getPropValue("transparent.properties","typhoon_param"));
           cimissQueryParam.setDataCode("SEVP_WEFC_TYP_WT");
           cimissQueryParam.setInterfaceId("getTyphByTimeRangeAndTyphCids");
           cimissQueryParam.setReportCenters("BABJ");
           cimissQueryParam.setOrderBy("Datetime:asc,Validtime:asc");
           cimissQueryParam.setTyphCIds(typhCIds);
           List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
            // 排序去除预报数据
//           List<Map<String,String>> dataList1 = this.handResultData(resultList);
//           List<Map<String,String>> dataList = Misc.sortListMap(resultList,"Datetime");
           TyphoonData typhoonData =  this.handleTyphoonData(resultList);
            typhoonData = handleListData(typhoonData);
           return WrapMapper.ok(typhoonData);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }



    @Description("获取站点统计数据")
    @RequestMapping(value = "/getStatisticsData", method = RequestMethod.GET)
    public Wrapper getStatisticsData(CimissQueryParam cimissQueryParam) {
        String dataCode = cimissQueryParam.getDataCode();
        try {
            List<Map<String,String>> dataResult = new ArrayList<>();
            String endTime = cimissQueryParam.getTimeRang();
            int strStartIndex = endTime.indexOf(",");
            int endIndex = 0;
            if(endTime.indexOf(")")!= -1){
                endIndex = endTime.indexOf(")");
            }else {
                endIndex = endTime.indexOf("]");
            }
            System.out.println();
            String eTime = endTime.substring(strStartIndex + 1,endIndex);
            System.out.println(" ====站点详情请求时间===========");
            System.out.println(endTime);
            if("SURF_CHN_MUL_HOR".equals(cimissQueryParam.getDataCode())){

//                ExecutorService executorService = Executors.newFixedThreadPool(10);

                List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
                // 1小时
                String timeRange = this.handleTimeRange(eTime,"1",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange);
                List<Map<String,String>> resultList1= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                // 3小时
                String timeRange3 = this.handleTimeRange(eTime,"3",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange3);
                List<Map<String,String>> resultList3= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
                // 6小时
                String timeRange6 = this.handleTimeRange(eTime,"6",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange6);
                List<Map<String,String>> resultList6= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
                // 12小时
                String timeRange12 = this.handleTimeRange(eTime,"12",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange12);
                List<Map<String,String>> resultList12= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                // 24小时
                String timeRange24 = this.handleTimeRange(eTime,"24",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange24);
                List<Map<String,String>> resultList24= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                // 48小时
                String timeRange48 = this.handleTimeRange(eTime,"48",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange48);
                List<Map<String,String>> resultList48= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                // 72小时
                String timeRange72 = this.handleTimeRange(eTime,"72",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange72);
                List<Map<String,String>> resultList72= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
                dataResult.add(resultList.get(0));
                dataResult.add(resultList1.get(0));
                dataResult.add(resultList3.get(0));
                dataResult.add(resultList6.get(0));
                dataResult.add(resultList12.get(0));
                dataResult.add(resultList24.get(0));
                dataResult.add(resultList48.get(0));
                dataResult.add(resultList72.get(0));
            }else if("SURF_CHN_MUL_DAY".equals(dataCode)){
                cimissQueryParam.setDataCode("SURF_CHN_MUL_DAY");
                List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
                String pre = this.calculatePre(resultList);
                Map m = new HashMap();
                m.put("STATIC_PRE",pre);

                // 1日
                String timeRange = this.handleTimeRange(eTime,"1",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange);
                List<Map<String,String>> resultList1= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
                String pre1 = this.calculatePre(resultList1);
                Map m1 = new HashMap();
                m1.put("STATIC_PRE",pre1);

                // 7日
                String timeRange3 = this.handleTimeRange(eTime,"7",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange3);
                List<Map<String,String>> resultList3= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                String pre2 = this.calculatePre(resultList3);
                Map m2 = new HashMap();
                m2.put("STATIC_PRE",pre2);
                // 15日
                String timeRange6 = this.handleTimeRange(eTime,"15",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange6);
                List<Map<String,String>> resultList6= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                String pre3 = this.calculatePre(resultList6);
                Map m3 = new HashMap();
                m3.put("STATIC_PRE",pre3);

                // 30日
                String timeRange12 = this.handleTimeRange(eTime,"30",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange12);
                List<Map<String,String>> resultList12= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                String pre4 = this.calculatePre(resultList12);
                Map m4 = new HashMap();
                m4.put("STATIC_PRE",pre4);


                // 60日
                String timeRange24 = this.handleTimeRange(eTime,"60",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange24);
                List<Map<String,String>> resultList24= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                String pre5 = this.calculatePre(resultList24);
                Map m5 = new HashMap();
                m5.put("STATIC_PRE",pre5);

                // 90日
                String timeRange48 = this.handleTimeRange(eTime,"90",cimissQueryParam.getDataCode());
                cimissQueryParam.setTimeRang(timeRange48);
                List<Map<String,String>> resultList48= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));

                String pre6 = this.calculatePre(resultList48);
                Map m6 = new HashMap();
                m6.put("STATIC_PRE",pre6);

                dataResult.add(m);
                dataResult.add(m1);
                dataResult.add(m2);
                dataResult.add(m3);
                dataResult.add(m4);
                dataResult.add(m5);
                dataResult.add(m6);
            }
            return WrapMapper.ok(dataResult);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("获取站点的实时数据")
    @RequestMapping(value = "/getStationDateApp", method = RequestMethod.POST)
    public Wrapper getStationDataByApp(CimissQueryParam cimissQueryParam) {
        try {
            List<Map<String,String>> resultList= stationDataService.getData(CimissUtils.bean2map(cimissQueryParam));
            List<Map<String,String>> reList=  new ArrayList<>();
            reList.add(resultList.get(0));
            List<Map<String,String>> dataList = stationDataService.handleDirecition(reList);
            return WrapMapper.ok(dataList);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @RequestMapping(value = "/stationdata", method = RequestMethod.POST)
    public Wrapper searchDatabystationid(CimissQueryParam cimissQueryParam) {
        try {
            List<Map<String,String>> resultMap = stationDataService.getStationData(CimissUtils.bean2map(cimissQueryParam));
            return WrapMapper.ok(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }


    public String handleTimeRange(String endTime,String time,String dateCode) throws Exception{
        Calendar calendar = Calendar.getInstance();
        String timeRange = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
        Date d = format.parse(endTime);
        calendar.setTime(d);
        if("SURF_CHN_MUL_HOR".equals(dateCode)){
            calendar.add(Calendar.HOUR_OF_DAY,-Integer.valueOf(time));
            String startTime = format.format(calendar.getTime());
            timeRange = "("+startTime+","+endTime+"]";
        }else if("SURF_CHN_MUL_DAY".equals(dateCode)){
            calendar.add(Calendar.DAY_OF_MONTH,-Integer.valueOf(time));
            String startTime = format.format(calendar.getTime());
            timeRange = "("+startTime+","+endTime+"]";
        }
        return timeRange;
    }

    public String calculatePre(List<Map<String,String>> dataList){
        if(dataList.size()>0){
            DecimalFormat format = new DecimalFormat("0.00");
            double sum_pre = 0d;
            for (int i = 0; i < dataList.size(); i++) {
                double pre = 0d;
                Map map = dataList.get(i);
                if(map.containsKey("PRE_Time_0808") && !"--".equals(String.valueOf(map.get("PRE_Time_0808")))){
                    pre = Double.valueOf(String.valueOf(map.get("PRE_Time_0808")));
                }else if(map.containsKey("PRE_Time_2020") && !"--".equals(String.valueOf(map.get("PRE_Time_2020")))){
                    pre = Double.valueOf(String.valueOf(map.get("PRE_Time_2020")));
                }
                if(pre < 9999){
                    sum_pre += pre;
                }
            }
            return String.valueOf(format.format(sum_pre));
        } else {
            return "--";
        }
    }

    /**
     * 台风编号数据处理
     * @param dataList
     * @return
     */
    public List<Map<String,String>> handleTypyoonNumber(List<Map<String,String>> dataList) throws Exception{
        List<Map<String,String>> nimberList = new ArrayList<>();
        Map<String,String> typhoonNameInfo = stationDataService.getTypoonNameInfo();
        Map<String,String> numberMap = new HashMap<>();
        Map<String,String> timeMap = new HashMap<>();
        for (int i = 0; i < dataList.size(); i++) {
            Map map = dataList.get(i);
            if(!map.get("Num_Nati").equals("--") && Double.valueOf(String.valueOf(map.get("Num_Nati"))) < 99900){
                numberMap.put(String.valueOf(map.get("Num_Nati")),String.valueOf(map.get("TYPH_Name")));
                if(!timeMap.containsKey(map.get("Num_Nati"))){
                    timeMap.put(String.valueOf(map.get("Num_Nati")),String.valueOf(map.get("Datetime")));
                }
            }
        }
        Calendar ca = Calendar.getInstance();
        ca.setTime(new Date());
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        for(Map.Entry<String,String> entry : numberMap.entrySet()){
            Map<String,String> map = new HashMap();
            Object o = typhoonNameInfo.get(entry.getValue());
            map.put("TYPH_NAME",o==null?entry.getValue():String.valueOf(o));
            map.put("NUM_NATI",entry.getKey());
            String DateTime = Misc.changeDateTimeZone(timeMap.get(entry.getKey()));
            Date date = sf.parse(DateTime);
            Date dateNow = sf.parse(sf.format(new Date()));
//            if(((dateNow.getTime() - date.getTime())/(24 * 60 * 60 * 1000)) <= 1){ // 标记当前生效的台风数据。
//                map.put("STATUS","1");
//            } else {
                map.put("STATUS","0");
//            }
            nimberList.add(map);
        }
        return nimberList;
    }

    /**
     * 台风数据、结构转换
     * @param dataList
     * @return
     */
    public TyphoonData  handleTyphoonData(List<Map<String,String>> dataList) throws Exception{
        TyphoonData typhoonData = new TyphoonData();
        List<TyphoonDataPoint> pointList = new ArrayList<>();

        if(dataList.size() > 0){
            typhoonData.setBegin_time(dataList.get(0).get("Datetime"));
            typhoonData.setEnd_time(dataList.get(dataList.size()-1).get("Datetime"));
            typhoonData.setTfbh(dataList.get(0).get("Num_INati"));
            typhoonData.setName(dataList.get(0).get("TYPH_Name"));
        }
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
        Date d = new Date();
        Calendar ecl = Calendar.getInstance();
        for (int i = 0; i < dataList.size(); i++) {
             Map map = dataList.get(i);
             Date handleDate = sf.parse(String.valueOf(map.get("Datetime")));
             if(handleDate.before(d)){
                TyphoonDataPoint typhoonDataPoint = new TyphoonDataPoint();
                typhoonDataPoint.setLatitude(Double.valueOf(String.valueOf(map.get("Lat"))));
                typhoonDataPoint.setLongitude(Double.valueOf(String.valueOf(map.get("Lon"))));
                typhoonDataPoint.setMove_dir(map.get("MoDir_Future").equals("--")?"":Misc.getDirect(String.valueOf(map.get("MoDir_Future"))));
                typhoonDataPoint.setMove_speed(map.get("MoSpeed_Futrue").equals("--")?0.0d:Double.valueOf(String.valueOf(map.get("MoSpeed_Futrue"))));
                typhoonDataPoint.setPressure(map.get("PRS").equals("--")?0.0d:Double.valueOf(String.valueOf(map.get("PRS"))));
                ecl.setTime(sf.parse(String.valueOf(map.get("Datetime"))));
                ecl.add(Calendar.HOUR_OF_DAY,Integer.valueOf(String.valueOf(map.get("Validtime"))));
                typhoonDataPoint.setTime(Misc.changeDateTimeZone(sf.format(ecl.getTime())));
//                 typhoonDataPoint.setTime(Misc.changeDateTimeZone(String.valueOf(map.get("Datetime"))));
                typhoonDataPoint.setPower(map.get("Typh_Intsy").equals("--")?0.0d:Double.valueOf(String.valueOf(map.get("Typh_Intsy"))));
                typhoonDataPoint.setSpeed(map.get("WIN_S_Conti_Max").equals("--")?0.0d:Double.valueOf(String.valueOf(map.get("WIN_S_Conti_Max"))));
                if(!map.get("WIN_S_Conti_Max").equals("--")){
                    String stong = this.getStrongBySpeed(String.valueOf(map.get("WIN_S_Conti_Max")));
                    typhoonDataPoint.setStrong(stong);
                }else{
                    typhoonDataPoint.setStrong("");
                }
                typhoonDataPoint.setRadius7(map.get("WING_A7_Bear1").equals("--")?0.0d:Double.valueOf(String.valueOf(map.get("WING_A7_Bear1"))));
                typhoonDataPoint.setRadius10(map.get("WING_A10_Bear1").equals("--")?0.0:Double.valueOf(String.valueOf(map.get("WING_A10_Bear1"))));
                typhoonDataPoint.setRadius12(map.get("WING_A12_Bear1").equals("--")?0.0:Double.valueOf(String.valueOf(map.get("WING_A12_Bear1"))));

                // 获取风圈半径数据
                 String r7Se = map.get("Radiu_Bear1_WING_A7").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear1_WING_A7"));
                 String r7sw = map.get("Radiu_Bear2_WING_A7").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear2_WING_A7"));
                 String r7Ne = map.get("Radiu_Bear3_WING_A7").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear3_WING_A7"));
                 String r7Nw = map.get("Radiu_Bear4_WING_A7").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear4_WING_A7"));

                 String r10Se = map.get("Radiu_Bear1_WING_A10").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear1_WING_A10"));
                 String r10sw = map.get("Radiu_Bear2_WING_A10").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear2_WING_A10"));
                 String r10Ne = map.get("Radiu_Bear3_WING_A10").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear3_WING_A10"));
                 String r10Nw = map.get("Radiu_Bear4_WING_A10").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear4_WING_A10"));


                 String r12Se = map.get("Radiu_Bear1_WING_A12").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear1_WING_A12"));
                 String r12sw = map.get("Radiu_Bear2_WING_A12").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear2_WING_A12"));
                 String r12Ne = map.get("Radiu_Bear3_WING_A12").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear3_WING_A12"));
                 String r12Nw = map.get("Radiu_Bear4_WING_A12").equals("--")?"0.0":String.valueOf(map.get("Radiu_Bear4_WING_A12"));
                if(!r7Se.equals("0.0")){
                    typhoonDataPoint.getRadius7_quad().put("ne",Double.valueOf(r7Se));
                }
                if(!r7sw.equals("0.0")){
                    typhoonDataPoint.getRadius7_quad().put("se",Double.valueOf(r7sw));
                }
                if(!r7Ne.equals("0.0")){
                    typhoonDataPoint.getRadius7_quad().put("sw",Double.valueOf(r7Ne));
                }
                if(!r7Nw.equals("0.0")){
                    typhoonDataPoint.getRadius7_quad().put("nw",Double.valueOf(r7Nw));
                }
                if(!r10Se.equals("0.0")){
                    typhoonDataPoint.getRadius10_quad().put("ne",Double.valueOf(r10Se));
                }
                if(!r10sw.equals("0.0")){
                    typhoonDataPoint.getRadius10_quad().put("se",Double.valueOf(r10sw));
                }
                if(!r10Ne.equals("0.0")){
                    typhoonDataPoint.getRadius10_quad().put("sw",Double.valueOf(r10Ne));
                }
                if(!r10Nw.equals("0.0")){
                    typhoonDataPoint.getRadius10_quad().put("nw",Double.valueOf(r10Nw));
                }
                if(!r12Se.equals("0.0")){
                    typhoonDataPoint.getRadius12_quad().put("ne",Double.valueOf(r12Se));
                }
                if(!r12sw.equals("0.0")){
                    typhoonDataPoint.getRadius12_quad().put("se",Double.valueOf(r12sw));
                }
                if(!r12Ne.equals("0.0")){
                    typhoonDataPoint.getRadius12_quad().put("sw",Double.valueOf(r12Ne));
                }
                if(!r12Nw.equals("0.0")){
                    typhoonDataPoint.getRadius12_quad().put("nw",Double.valueOf(r12Nw));
                }
                if(map.containsKey("Validtime") && "0".equals(map.get("Validtime"))){
                    pointList.add(typhoonDataPoint);
                }else{
                    if(pointList.size() > 0){
                        pointList.get(pointList.size()-1).getForecast_middle().add(typhoonDataPoint);
                    }
                }}
            }
            if(pointList.size()>0){
                typhoonData.setPoints(pointList);
            }
        return typhoonData;
    }

    /**
     * 台风时间段拼接
     * @param times
     * @return
     * @throws Exception
     */
    public String handleDate(String times) throws Exception{
        String timeRange = "";
        Date d1 = new Date();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHH0000");
        String nowYear = sf.format(d1);

        if(times.equals(nowYear)){
            timeRange = "("+ times+"0101000000,"+ sf1.format(d1)+"]";
        }else{
            timeRange = "("+ times+"0101000000,"+ times+"1231235900"+"]";
        }
        return timeRange;
    }

    /**
     * 台风数据处理
     * @param typhoonData
     * @return
     */
    public TyphoonData handleListData(TyphoonData typhoonData){
        List<TyphoonDataPoint> dataList = typhoonData.getPoints();
        for (int i = 0; i < dataList.size(); i++) {
            TyphoonDataPoint typhoonDataPoint = dataList.get(i);
            List<TyphoonDataPoint> eList = typhoonDataPoint.getForecast_middle();
            Map<String,List<TyphoonDataPoint>> eMap = new HashMap<>();
            eMap.put("points",eList);
            List<Map<String,List<TyphoonDataPoint>>> list = new ArrayList<>();
            list.add(eMap);
            typhoonDataPoint.setForecast(list);
        }
        return typhoonData;
    }


    public String getStrongBySpeed(String speed){
        Double s = Double.valueOf(speed);
        if(s < 17.2){
            return "热带低压";
        }else if(s >= 17.2 && s< 24.5){
            return "热带风暴";
        } else if(s >= 24.5 && s < 32.7 ){
            return "强热带风暴";
        } else if(s >= 32.7 && s < 41.4){
            return "台风";
        } else if(s >= 41.4 && s < 51.0)  {
            return "强台风";
        } else if(s >= 51.0){
            return "超强台风";
        }
        return String.valueOf(s);
    }



    private List<Map<String, String>> handResultData(List<Map<String, String>> resultList) {
        List<Map<String,String>> data  = new ArrayList<>();
        for (int i = 0; i < resultList.size(); i++) {
            Map<String,String> map = resultList.get(i);
            if("0".equals(map.get("Validtime"))){
                data.add(map);
            }
        }
        return data;
    }


    public TyphoonDataPoint handleData(TyphoonDataPoint typhoonDataPoint){
        Double r7 = typhoonDataPoint.getRadius7();
        Double r10 = typhoonDataPoint.getRadius10();
        Double r12 = typhoonDataPoint.getRadius12();
        return null;
    }

    private List<Map<String, String>> handlePrecudureData(List<Map<String, String>> resultList) {
        DecimalFormat df = new DecimalFormat("#.0");
        for (int i = 0; i < resultList.size(); i++) {
            Map map = resultList.get(i);
                if(i>0){
                    // 统计小时降水、
                    if(map.containsKey("PRE_1h") ){
                        String lastPre = resultList.get(i-1).get("PRE_1h");
                        if("--".equals(lastPre)){ lastPre = "0";}
                        String nowPre = resultList.get(i).get("PRE_1h");
                        if("--".equals(nowPre) || Double.valueOf(nowPre) > 99990){ nowPre = "0";}
                        resultList.get(i).put("PRE_1h",String.valueOf(df.format(Double.valueOf(lastPre) + Double.valueOf(nowPre))));
                    }
                    // 统计0808降水
                    if(map.containsKey("PRE_Time_0808")){
                        String lastPre_0808 = resultList.get(i-1).get("PRE_Time_0808");
                        String nowPre_8080 = resultList.get(i).get("PRE_Time_0808");
                        if("--".equals(lastPre_0808)){ lastPre_0808 ="0";}
                        if("--".equals(nowPre_8080) || Double.valueOf(nowPre_8080) > 99990){ nowPre_8080 ="0";}
                        resultList.get(i).put("PRE_Time_0808",String.valueOf(df.format(Double.valueOf(nowPre_8080) + Double.valueOf(lastPre_0808))));
                    }
                    // 统计2020降水
                    if(map.containsKey("PRE_Time_2020")){
                        String lastPre_2020 = resultList.get(i-1).get("PRE_Time_2020");
                        String nowPre_2020 = resultList.get(i).get("PRE_Time_2020");
                        if("--".equals(lastPre_2020)) {lastPre_2020 = "0";}
                        if("--".equals(nowPre_2020) || Double.valueOf(nowPre_2020) > 99990){ nowPre_2020 = "0";}
                        resultList.get(i).put("PRE_Time_2020",String.valueOf(df.format(Double.valueOf(lastPre_2020) + Double.valueOf(nowPre_2020))));
                    }
                }
        }
        return resultList;
    }
}
