package com.piesat.busiclogic.webGis.handle;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.dao.StationDataDao;
import com.piesat.cimiss.common.util.XmlReaderUtils;
import com.piesat.common.util.TqServiceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StationDataService {

    private static final String ALG_NODE_NAME = "algorithm";

    private static final String INTERFACE_NODE_TYPE = "cimiss";

    private static final String CURRENT_NODE_NAME = "current";

    private static final String MODEL_PATH = "path";

    private static final String DATA_CODE_NAME = "dataCode";

    private static final String BASE_URL_NAME = "baseUrl";

    private static final String INTERFACE_NODE_NAME = "interfaces";

    private static final String INTERFACE_ID_NAME = "interfaceId";

    private static final String NECESSITY_NAME = "necessity";

    private static final String ELEMENT_NAME = "elements";

    private static final String DATA_KEY_NAME = "dataKey";

    @Autowired
    private StationDataDao stationDataDao;

    /**
     * 获取气象要素点数据
     * @param xmlConfig
     * @return
     * @throws Exception
     */
    public List<Map<String,String>> getData(Map<String, String> xmlConfig) throws Exception{
        String dataCode  = xmlConfig.get("dataCode");
        Map<String, String> modelConfig = XmlReaderUtils.getModelConfig(xmlConfig, xmlConfig.get(MODEL_PATH), DATA_CODE_NAME, BASE_URL_NAME,
                INTERFACE_NODE_NAME, INTERFACE_ID_NAME, NECESSITY_NAME);
        if(modelConfig == null || modelConfig.isEmpty()) {
            throw new RuntimeException("read model config file fail.use user config and return null . file path is : "+xmlConfig.get(MODEL_PATH));
        }
        modelConfig.put("dataCode",dataCode);
        List<Map<String,String>> dataList = TqServiceUtils.readData(modelConfig, BASE_URL_NAME, modelConfig.get(ELEMENT_NAME), DATA_KEY_NAME);
        return StationDataService.filterListData(dataList);
    }


    /**
     * 获取站点历史数据
     * @return
     */
    public List<Map<String,String>> getStationData(Map<String, String> xmlConfig) throws Exception{
        Map<String, String> modelConfig = XmlReaderUtils.getModelConfig(xmlConfig, xmlConfig.get(MODEL_PATH), DATA_CODE_NAME, BASE_URL_NAME,
                INTERFACE_NODE_NAME, INTERFACE_ID_NAME, NECESSITY_NAME);
        if(modelConfig == null || modelConfig.isEmpty()) {
            throw new RuntimeException("read model config file fail.use user config and return null . file path is : "+xmlConfig.get(MODEL_PATH));
        }
        List<Map<String,String>> dataList = TqServiceUtils.readData(modelConfig, BASE_URL_NAME, modelConfig.get(ELEMENT_NAME), DATA_KEY_NAME);
        return  dataList;
    }

    public List<Map<String, String>> getSunData(String stationId) {
//        Map<String,Object> map = stationDataDao.getLonAndLat(stationId);
        Calendar now = Calendar.getInstance();
//      TimeUtils.calcSunriseAndSunset(now.get(Calendar.YEAR),now.get(Calendar.MONTH),Calendar.DAY_OF_MONTH,map.get("LAT"),map.get("LON"));
        return  null;
    }



    public static List<Map<String,String>> filterListData(List<Map<String,String>> dataList){
        for (int i = 0; i < dataList.size(); i++) {
            Map<String,String> eleMap = dataList.get(i);
            for (Map.Entry<String, String> entry : eleMap.entrySet()) {
                // 替换缺测为 --
                if("999998".equals(entry.getValue()) || "999999".equals(entry.getValue())){
                    eleMap.put(entry.getKey(),"--");
                }
            }
        }
        return  dataList;
    }

    /**
     * 添加风向
     * @param resultList
     * @return
     */
    public List<Map<String, String>> handleDirecition(List<Map<String, String>> resultList) {
        for (int i = 0; i < resultList.size(); i++) {
            if(resultList.get(i).containsKey("WIN_D_INST")){
                resultList.get(i).put("WIN_D_INST", Misc.getDirection(resultList.get(i).get("WIN_D_INST")));
            }
            if(resultList.get(i).containsKey("WEP_Now")){
                if(resultList.get(i).containsKey("PRE_1h")){
                    Double pre_1 = Double.valueOf(resultList.get(i).get("PRE_1h"));
                    if(pre_1 < 0.1){
                        resultList.get(i).put("WEP_Now", "0");
                    } else if(0.1d <=pre_1 && pre_1 <= 5){
                        resultList.get(i).put("WEP_Now", "7");
                    }else if( 5< pre_1 && pre_1<= 15){
                        resultList.get(i).put("WEP_Now", "8");
                    }else {
                        resultList.get(i).put("WEP_Now", "9");
                    }
                }else{
                    resultList.get(i).put("WEP_Now", "0");
                }
            }
        }
       return resultList;
    }


    public Map<String,String> getTypoonNameInfo(){
        List<Map<String,Object>> nameList = stationDataDao.getTypoonData();
        Map<String,String> map = new HashMap<>();
        for (int i = 0; i < nameList.size(); i++) {
            Map<String,Object> map1 = nameList.get(i);
            map.put(String.valueOf(map1.get("NAME_EN")),String.valueOf(map1.get("NAME")));
        }
        return map;
    }
}
