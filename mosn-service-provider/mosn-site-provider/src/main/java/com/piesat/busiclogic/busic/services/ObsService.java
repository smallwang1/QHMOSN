package com.piesat.busiclogic.busic.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.MapLikeType;
import com.piesat.busiclogic.busic.entities.StationInfo;
import com.piesat.busiclogic.busic.repositories.ProcedureRepository;
import com.piesat.busiclogic.busic.repositories.StationInfoRepository;
import com.piesat.busiclogic.busic.supports.responseExtractor.ForWordResponseExtractor;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.util.CallInterfaceUtils;
import com.piesat.common.util.JsonUtils;
import com.piesat.common.util.PublicUtil;
import com.piesat.common.util.TqServiceUtils;
import com.piesat.jdbc.dataSource.DBContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

@Slf4j
@Service
public class ObsService {

    @Resource
    private StationInfoRepository stationInfoRepository;

    @Resource
    private ProcedureRepository procedureRepository;

    @Value("${mdes.truninterface}")
    private String url;

    @Value("${cmadass.returnCode}")
    private String codeName;

    @Value("${cmadass.rightCode}")
    private String rightCode;

    @Value("${cmadass.dataCodeName}")
    private String dataCodeName;

    private static final String errorValue = "999999";

    private static final String gt = "(";

    private static final String get = "[";

    private static final String le = ")";

    private static final String let = "]";

    private static final String begin = "begin";

    private static final String end = "end";

    private static final String beginValue = "beginValue";

    private static final String endValue = "endValue";

    private static final String mark = "1";

    private static final String unMark = "0";

    private static final String count = "count";

    private static final String data = "data";

    public Map<String, Object> getAllObsDataByCondition(String interFaceParams, String range, String elementKey,
                                                        String stationKey, String replaceKey) throws Exception {

        Map<String, Object> result = new HashMap<String, Object>();
        ObjectMapper objectMapper = new ObjectMapper();

        MapLikeType type = objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, String.class);
        HashMap<String, String> interfaceMap = objectMapper.readValue(interFaceParams, type);
        HashMap<String, String> rangeMap = objectMapper.readValue(range, type);

        Map<String, Long> countMap = new HashMap<String, Long>();

        Map<String, Map<String, BigDecimal>> rangeData = getRangeMap(rangeMap);

        Map<String, StationInfo> stationInfos = stationInfoRepository.getAllStationInfo();

        List<Map<String, String>> baseDataFromCmadass = new ArrayList<>();

        // 过程数据max 、min 进行统计
        if (PublicUtil.isEmpty(elementKey)) {
            baseDataFromCmadass = getBaseDataFromCmadass(interfaceMap);
        } else if ((elementKey.indexOf("MAX") != -1) || elementKey.indexOf("MIN") != -1) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:SS");
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMddHHmmSS");
            String element = elementKey.substring(4);
            String project = elementKey.substring(0, 3);
            String timeRange = interfaceMap.get("timeRange");
            String adminCodes = interfaceMap.get("adminCodes");
            int stIndex = timeRange.indexOf("(");
            int seIndex = timeRange.indexOf(",");
            int eIndex = timeRange.indexOf("]");
            String startTime = timeRange.substring(stIndex + 1, seIndex);
            String endTime = timeRange.substring(seIndex + 1, eIndex);
            DBContextHolder.setDataSourceName("cmadaasDataSource");
            try {
                List<Map<String, Object>> middleData = procedureRepository.getProduceData(elementKey, sf.format(sf1.parse(startTime)), sf.format(sf1.parse(endTime)), adminCodes);
                baseDataFromCmadass = this.statisMaxMinData(middleData, project, elementKey);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                DBContextHolder.clear();
            }
        } else {
            baseDataFromCmadass = getBaseDataFromCmadass(interfaceMap);
            if (elementKey.equals("PRE_1h")) {
                List<Map<String, String>> swData = new ArrayList<>();
                try {
//                    swData = this.getSWData(interfaceMap.get("times"));
//                    baseDataFromCmadass.addAll(swData);
                } catch (Exception e) {
                }
                baseDataFromCmadass = Misc.sortListMapDesc((List<Map<String, String>>) baseDataFromCmadass, "PRE_1h");
            }
        }

        // 特殊处理过程降水添加透明图
//		if(elementKey.equals("SUM_PRE_1h")){
//			// 出透明图
//			this.excuteTransparent(baseDataFromCmadass,elementKey);
//		}

        if ("PRE_1h".equals(elementKey)) {// 逐小时降水为0 过滤
            List<Map<String, String>> handleBaseData = new ArrayList<>();
            for (int i = 0; i < baseDataFromCmadass.size(); i++) {
                Map<String, String> map = baseDataFromCmadass.get(i);
                if (!"0".equals(map.get("PRE_1h"))) {
                    handleBaseData.add(map);
                }
            }
            baseDataFromCmadass = handleBaseData;
        } else if ("SUM_PRE_1h".equals(elementKey)) {
            List<Map<String, String>> handleBaseData = new ArrayList<>();
            for (int i = 0; i < baseDataFromCmadass.size(); i++) {
                Map<String, String> map = baseDataFromCmadass.get(i);
                if (!"0".equals(map.get("SUM_PRE_1h"))) {
                    handleBaseData.add(map);
                }
            }
            baseDataFromCmadass = handleBaseData;
        }

        // 如果是水文站数据、统一结果返回值
//		if(elementKey.equals("DRP")){
//			baseDataFromCmadass = this.handleShuiWenData(baseDataFromCmadass);
//		}

        // 过程平均气温,保留1位小数
        if (PublicUtil.isEmpty(elementKey)) {
        } else if (elementKey.equals("AVG_TEM")) {
            baseDataFromCmadass = this.baseDataFormat(baseDataFromCmadass, elementKey);
        }

        if (!PublicUtil.isEmpty(baseDataFromCmadass)) {

            // 请求区域中含安徽省进行与安徽站网系统匹配
            if ((interfaceMap.containsKey("adminCodes") && interfaceMap.get("adminCodes").indexOf("63") != -1) ||
                    "SURF_CHN_MUL_MIN".equals(interfaceMap.get("dataCode")) || "V15551".equals(elementKey)) {
                baseDataFromCmadass.stream().forEach(map -> {
                    try {
                        markAndReplaceData(map, rangeData, elementKey, stationKey, stationInfos, StringUtils.isBlank(replaceKey) ? null : replaceKey.split(","));
                        if (!PublicUtil.isEmpty(map.get("latitude")) && !PublicUtil.isEmpty(map.get("longitude"))) {
                            map.put("Lat", Misc.getPoint2(Double.valueOf(map.get("latitude"))));
                            map.put("Lon", Misc.getPoint2(Double.valueOf(map.get("longitude"))));
                        }
                        if (!PublicUtil.isEmpty(map.get("City")) || !PublicUtil.isEmpty(map.get("city"))) {
                            String cnty = Objects.isNull(map.get("cnty")) ? map.get("Cnty") : map.get("cnty");
                            map.put("cnty", map.get("City").trim() + cnty);
                        }
//                        if (!PublicUtil.isEmpty(map.get("City")) && !PublicUtil.isEmpty(map.get("cnty"))) {
//                            map.put("cnty", map.get("City") + map.get("cnty"));
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("for each data fail.error data is :" + map);
                    }
                });
            } else {
                baseDataFromCmadass.stream().forEach(map -> { // 非安徽数据统一站名字段
                    // 统一站名字段
                    if (!PublicUtil.isEmpty(map.get("Station_name"))) {
                        map.put("stationName", map.get("Station_name"));
                    }
                });
            }

            for (Entry<String, Map<String, BigDecimal>> entry : rangeData.entrySet()) {
                count(baseDataFromCmadass, entry.getKey(), countMap);
            }
            result.put(count, countMap);


            // 针对降水数据的按区县划分、标记出最大值
            Map<String, Integer> indexMap = new HashMap<>();
            if (baseDataFromCmadass.size() > 0 && !"V15551".equals(elementKey) && !PublicUtil.isEmpty(elementKey) && !"DRP".equals(elementKey)) {
                for (int i = 0; i < baseDataFromCmadass.size(); i++) {
                    Map emap = baseDataFromCmadass.get(i);
                    String area_code = String.valueOf(emap.get("Admin_Code_CHN"));
                    String element_value = String.valueOf(emap.get(elementKey));
                    if (indexMap.containsKey(area_code)) { // 比较最大值替换
                        int maxIndex = Integer.valueOf(String.valueOf(indexMap.get(area_code)));
                        String element_value_max = baseDataFromCmadass.get(maxIndex).get(elementKey);
                        if (!"--".equals(element_value_max) && (Double.valueOf(element_value) > Double.valueOf(element_value))) {
                            indexMap.put(area_code, i);
                        }
                    } else {
                        if (!"--".equals(element_value)) {
                            indexMap.put(area_code, i);
                        }
                    }
                }
            }

            // 按区域划分给各个区域数据打点最值
            for (Map.Entry<String, Integer> entry : indexMap.entrySet()) {
                Map map = baseDataFromCmadass.get(entry.getValue());
                map.put("best_one_in_code", "yes");
            }

            // 结果集中有时间字段，统一成北京时区
            for (int i = 0; i < baseDataFromCmadass.size(); i++) {
                Map<String, String> dataMap = baseDataFromCmadass.get(i);
                if (dataMap.containsKey("DataTime")) {
                    dataMap.put("DataTime", Misc.changeDateTimeZone(dataMap.get("DataTime")));
                } else if (dataMap.containsKey("Datetime")) {
                    dataMap.put("DataTime", Misc.changeDateTimeZone(dataMap.get("Datetime")));
                }
            }
            result.put(data, Misc.filterListData(baseDataFromCmadass));
            return result;
        } else {
            return null;
        }
    }

    /**
     * 数据格式化
     *
     * @param baseDataFromCmadass
     * @param elementKey
     * @return
     */
    private List<Map<String, String>> baseDataFormat(List<Map<String, String>> baseDataFromCmadass, String elementKey) {
        DecimalFormat df = new DecimalFormat("#.0");
        for (int i = 0; i < baseDataFromCmadass.size(); i++) {
            Map<String, String> eMap = baseDataFromCmadass.get(i);
            eMap.put(elementKey, df.format(Double.valueOf(eMap.get(elementKey))));
        }
        return baseDataFromCmadass;
    }

    private List<Map<String, String>> handleShuiWenData(List<Map<String, String>> baseDataFromCmadass) {
        List<Map<String, String>> shuiWenData = new ArrayList<>();
        for (int i = 0; i < baseDataFromCmadass.size(); i++) {
            Map<String, String> neleMap = new HashMap<>();
            Map<String, String> elMap = baseDataFromCmadass.get(i);
            neleMap.put("PRE_1h", elMap.get("DRP"));
            neleMap.put("latitude", elMap.get("LTTD"));
            neleMap.put("longitude", elMap.get("LGTD"));
            neleMap.put("Station_Id_C", elMap.get("STCD"));
            neleMap.put("stationName", elMap.get("STNM"));
            neleMap.put("Datetime", elMap.get("Datetime"));
            neleMap.put("Station_levl", "sw");
            shuiWenData.add(neleMap);
        }
        return shuiWenData;
    }

//	/**
//	 * 站点数据生成透明图
//	 * @param baseDataFromCmadass
//	 */
//	private void excuteTransparent(List<Map<String, String>> baseDataFromCmadass,String eleKey) {
//		double[][] data2d = new double[baseDataFromCmadass.size()][3];
//		DecimalFormat df   = new DecimalFormat("######0.00");
//		for (int i = 0; i < baseDataFromCmadass.size(); i++) {
//			Map<String, String> eMap = baseDataFromCmadass.get(i);
//			String lat = eMap.get("Lat");
//			String lon = eMap.get("Lon");
//			String value = eMap.get(eleKey);
//			data2d[i][0] = Double.valueOf(df.format(Double.valueOf(lon)));
//			data2d[i][1] = Double.valueOf(df.format(Double.valueOf(lat)));
//			data2d[i][2] = Double.valueOf(value);
//		}
//		double[] interpolationBounds = {114.711730,29.207180,119.807565,34.870431};
//		double[] mapBounds = {114.711730,29.207180,119.807565,34.870431};
//		double[] dataInterval = {0.1,5,10,25,50,100,250,9999};
//		String boundryFile = "F:\\mdes\\config\\boundary.shp"; // 边界文件路径
//		String dataIntervalStyle = "F:\\mdes\\config\\pre.sld";//降水渲染样式文件路径
////		String dataIntervalStyle = "";
//		String fileNane = System.currentTimeMillis() + ".png";
//		String transparentImgPath = "F:\\mdes\\config\\" + fileNane;//输出路径
//		try {
//			ProcessRain.getProcessRainTransparentImg(data2d,interpolationBounds,mapBounds,dataInterval,boundryFile,dataIntervalStyle,transparentImgPath);
//		}catch (Exception e){
//			System.out.println(e.getMessage());
//		}
//	}

    private static void count(List<Map<String, String>> list, String markName, Map<String, Long> countMap) {

        long count = list.stream().filter(map -> StringUtils.equals(map.get(markName), mark)).count();
        countMap.put(markName, count);
    }

    private static Map<String, Map<String, BigDecimal>> getRangeMap(HashMap<String, String> rangeMap) {

        Map<String, Map<String, BigDecimal>> result = new HashMap<String, Map<String, BigDecimal>>();

        for (Entry<String, String> entry : rangeMap.entrySet()) {

            Map<String, BigDecimal> map = new HashMap<String, BigDecimal>(4);
            String name = entry.getKey();
            String value = entry.getValue();

            if (StringUtils.startsWith(value, gt)) {
                map.put(begin, BigDecimal.ZERO);
            } else if (StringUtils.startsWith(value, get)) {
                map.put(begin, BigDecimal.valueOf(-1));
            } else {
                throw new RuntimeException("unkown start range char. key is :" + name);
            }

            if (StringUtils.endsWith(value, le)) {
                map.put(end, BigDecimal.ZERO);
            } else if (StringUtils.endsWith(value, let)) {
                map.put(end, BigDecimal.ONE);
            } else {
                throw new RuntimeException("unkown end range char. key is :" + name);
            }

            value = value.replace(gt, "").replace(get, "").replace(le, "").replace(let, "");
            String[] values = value.split(",");
            map.put(beginValue, new BigDecimal(values[0]));
            map.put(endValue, new BigDecimal(values[1]));
            result.put(name, map);
        }
        return result;
    }

    private static void markAndReplaceData(Map<String, String> map,
                                           Map<String, Map<String, BigDecimal>> rangeData,
                                           String elementKey,
                                           String stationKey,
                                           Map<String, StationInfo> stationInfos,
                                           String[] replaceKey) throws Exception {

        replaceData(map, stationInfos.get(map.get(stationKey)), replaceKey);
        BigDecimal elementValue = new BigDecimal(errorValue);
        if (map != null && !StringUtils.isBlank(map.get(elementKey))) {
            elementValue = new BigDecimal(map.get(elementKey));
        } else {
            return;
        }
        for (Entry<String, Map<String, BigDecimal>> entry : rangeData.entrySet()) {

            String rangeName = entry.getKey();
            Map<String, BigDecimal> value = entry.getValue();
            if (elementValue.compareTo(value.get(beginValue)) > value.get(begin).intValue() &&
                    elementValue.compareTo(value.get(endValue)) < value.get(end).intValue()) {
                map.put(rangeName, mark);
            } else {
                map.put(rangeName, unMark);
            }
        }
    }

    private static void replaceData(Map<String, String> map, StationInfo stationInfo, String[] replaceKey) throws Exception {

        if (replaceKey == null || replaceKey.length == 0) {
            return;
        }
        if (Objects.isNull(stationInfo)) {
            for (int i = 0; i < replaceKey.length; i++) {
                if ("stationName".equals(replaceKey[i])) { // 站网系统无数据、用天擎数据
                    String station_name = map.get("Station_Name");
                    if (Objects.isNull(station_name)) {
                        station_name = map.get("Station_name");
                    }
                    map.put(replaceKey[i], station_name);
                }
            }
            return;
        }
        for (int i = 0; i < replaceKey.length; i++) {
            Field field = stationInfo.getClass().getDeclaredField(replaceKey[i]);
            field.setAccessible(true);
            Object data = field.get(stationInfo);
            if ("stationName".equals(replaceKey[i]) && data == null) { // 站网系统无数据、用天擎数据
                String station_name = map.get("Station_Name");
                if (Objects.isNull(station_name)) {
                    station_name = map.get("Station_name");
                }
                map.put(replaceKey[i], station_name);
            } else {
                map.put(replaceKey[i], data == null ? map.get(replaceKey[i]) : data.toString());
            }

        }
    }

    private List<Map<String, String>> getBaseDataFromCmadass(Map<String, String> paramsMap) throws Exception {

        HashMap map = new HashMap();
        map.putAll(paramsMap);
        paramsMap = TqServiceUtils.assembleMap(map);
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String, String> multiValueMap = new HttpHeaders();
        multiValueMap.add("Content-Type", "application/json; charset=UTF-8");
        multiValueMap.add("Accept", "*/*");
        multiValueMap.add("Accept", MediaType.APPLICATION_JSON.toString());
        String queryUrl = url + (StringUtils.endsWith(url, "?") ? "" : "?") + CallInterfaceUtils.getParams(paramsMap);
        log.info("request---->{}", queryUrl);
        HttpEntity<String> httpEntity = new HttpEntity<String>("", multiValueMap);
        RequestCallback requestCallback = restTemplate.httpEntityCallback(httpEntity);
        ForWordResponseExtractor forWordResponseExtractor = new ForWordResponseExtractor();
        String data = restTemplate.execute(new URI(queryUrl), HttpMethod.GET, requestCallback, forWordResponseExtractor);
        HashMap<String, String> allData = JsonUtils.resolveJSON(data);
        String code = allData.get(codeName);
        if (StringUtils.equals(code, rightCode)) {
            ObjectMapper objectMapper = new ObjectMapper();
            CollectionLikeType type = objectMapper.getTypeFactory().constructCollectionLikeType(List.class, Map.class);
            return objectMapper.readValue(allData.get(dataCodeName), type);
        }
        return null;
    }

    /**
     * 过程数据统计.
     *
     * @param handleData
     * @param project
     * @param element
     * @return
     */
    public List<Map<String, String>> statisMaxMinData(List<Map<String, Object>> handleData, String project, String element) {
        List<Map<String, Object>> dataList = new ArrayList<>();
        Map<String, Map<String, Object>> statisMap = new HashMap<>();
        for (int i = 0; i < handleData.size(); i++) {
            Map<String, Object> elementMap = handleData.get(i);
            String Station_Id_C = String.valueOf(elementMap.get("STATION_ID_C"));
            if (statisMap.containsKey(Station_Id_C)) {
                Double value = Double.valueOf(String.valueOf(elementMap.get(element.toUpperCase())));
                Double maxOrMinValue = Double.valueOf(String.valueOf(statisMap.get(Station_Id_C).get(element.toUpperCase())));
                if ("MAX".equals(project)) { // 统计最大值
                    if (value > maxOrMinValue) {
                        statisMap.put(Station_Id_C, elementMap);
                    }
                } else if ("MIN".equals(project)) {// 统计最小值
                    if (value < maxOrMinValue) {
                        statisMap.put(Station_Id_C, elementMap);
                    }
                }
            } else {
                statisMap.put(Station_Id_C, elementMap);
            }
        }
        for (Map.Entry<String, Map<String, Object>> entry : statisMap.entrySet()) {
            dataList.add(entry.getValue());
        }

        if ("MAX".equals(project)) {
            dataList = this.sortListMapAsc(dataList, element.toUpperCase());
        } else if ("MIN".equals(project)) {
            dataList = this.sortListMapDesc(dataList, element.toUpperCase());
        }
        return this.suteTianQField(dataList, element);
    }

    public Map<String, String> handleMapData(Map<String, Object> objectMap) {
        Map<String, String> result = new HashMap();
        for (Map.Entry entry : objectMap.entrySet()) {
            result.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
        }
        return result;
    }

    public List<Map<String, String>> suteTianQField(List<Map<String, Object>> dataList, String element) {
        List<Map<String, String>> rList = new ArrayList<>();
        for (Map<String, Object> map : dataList) {
            Map eMap = new HashMap();
            eMap.put("Admin_Code_CHN", String.valueOf(map.get("ADMIN_CODE_CHN")));
            eMap.put("Lat", String.valueOf(map.get("LAT")));
            eMap.put("Station_Id_C", String.valueOf(map.get("STATION_ID_C")));
            eMap.put("Station_levl", String.valueOf(map.get("STATION_LEVL")));
            eMap.put("DataTime", String.valueOf(map.get("DATATIME")));
            eMap.put(element, String.valueOf(map.get(element.toUpperCase())));
            rList.add(eMap);
        }
        return rList;
    }

    public List<Map<String, Object>> sortListMapAsc(List<Map<String, Object>> list, String key) {
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Double date1 = Double.valueOf(String.valueOf(o1.get(key)));
                Double date2 = Double.valueOf(String.valueOf(o2.get(key)));
                return date2.compareTo(date1);
            }
        });
        return list;
    }

    public List<Map<String, Object>> sortListMapDesc(List<Map<String, Object>> list, String key) {
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Double date1 = Double.valueOf(String.valueOf(o1.get(key)));
                Double date2 = Double.valueOf(String.valueOf(o2.get(key)));
                return date1.compareTo(date2);
            }
        });
        return list;
    }

    /**
     * list<Map<String,String>> 比较大小
     *
     * @param oList
     * @param compareKey
     * @return
     */
    public List<Map<String, Object>> sortList(List<Map<String, Object>> oList, String compareKey) {
        if (oList.size() > 0) {
            Collections.sort(oList, new Comparator<Map>() {
                @Override
                public int compare(Map o1, Map o2) {
                    int ret = 0;
                    //比较两个对象的顺序，如果前者小于、等于或者大于后者，则分别返回-1/0/1
                    ret = o2.get(compareKey).toString().compareTo(o1.get(compareKey).toString());//逆序的话就用o2.compareTo(o1)即可
                    return ret;
                }
            });
        }
        return oList;
    }

    /**
     * 获取水文数据
     *
     * @param times
     * @return
     */
    public List<Map<String, String>> getSWData(String times) throws Exception {
        List<Map<String, String>> dataList = new ArrayList<>();
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("interfaceId", "getSWSJDataByTime");
        paramMap.put("eleValueRanges", "DRP:(0,9999]");
        paramMap.put("dataCode", "SURF_AH_SWZY_PRE_HOR");
        paramMap.put("orderby", "Datetime:desc");
        paramMap.put("elements", "STCD,STNM,LGTD,LTTD,DRP,Q_DRP,Datetime,DATA_ID");
        paramMap.put("dataFormat", "json");
        paramMap.put("times", times);
        try {
            dataList = getBaseDataFromCmadass(paramMap);
            dataList = this.handleShuiWenData(dataList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
