package com.piesat.busiclogic.webGis.web;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.handle.ForcastDataService;
import com.piesat.common.anno.Description;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.PublicUtil;
import com.piesat.common.util.RedisUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * webgis预报数据
 */
@RestController
@CrossOrigin
@RequestMapping("/noAuth/forcast")
public class ForcastDataController {
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ForcastDataService forcastDataService;

    @Description("预报矩阵数据")
    @RequestMapping(value = "/matrixVerson2", method = RequestMethod.GET)
    public Wrapper forcastMatrix(String times,String modeCode,String elementCode,String forecastLevel) {
        try {
            JSONObject result = forcastDataService.forcastMatrix(times,modeCode,elementCode,forecastLevel);
            JSONObject jsonObject = result.getJSONObject("Datas");
            JSONArray value  = jsonObject.getJSONArray("Values");
            if(!PublicUtil.isEmpty(value)){ // 数据处理
                result.getJSONObject("Datas").put("forcastEleValue",forcastDataService.addCoordinate(
                        value,Float.valueOf(String.valueOf(jsonObject.getJSONObject("Coordinate").getJSONObject("Boundary").get("MinLongitude"))),
                        Float.valueOf(String.valueOf(jsonObject.getJSONObject("Coordinate").getJSONObject("Boundary").get("MinLatitude"))),0.025f,elementCode));
            }
            return WrapMapper.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }
    @Description("预报矩阵数据")
    @RequestMapping(value = "/matrixWind", method = RequestMethod.GET)
    public Wrapper forcastMatrixWind(String times,String modeCode,String uCode,String vCode,String forecastLevel) {
        try {
            JSONObject result = forcastDataService.forcastMatrixWind(times,uCode,vCode,modeCode,forecastLevel);
            return WrapMapper.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("预报矩阵数据、优先从缓存中获取数据")
    @RequestMapping(value = "/matrix", method = RequestMethod.GET)
    public Wrapper forcastMatrix2(String times,String modeCode,String elementCode,String forecastLevel) {
        JSONObject jsonOdata = new JSONObject();
        try {
            if(!PublicUtil.isEmpty(redisUtils.get(times)) && "ER03".equals(elementCode) ){
                jsonOdata =  (JSONObject) redisUtils.get(times);
            }else{
                jsonOdata = forcastDataService.forcastMatrix(times,modeCode,elementCode,forecastLevel);
            }
            JSONObject jsonObject = jsonOdata.getJSONObject("Datas");
            JSONArray value  = jsonObject.getJSONArray("Values");
            if(!PublicUtil.isEmpty(value)){ // 数据处理
                jsonObject.put("forcastEleValue",forcastDataService.addCoordinate(
                        value,Float.valueOf(String.valueOf(jsonObject.getJSONObject("Coordinate").getJSONObject("Boundary").get("MinLongitude"))),
                        Float.valueOf(String.valueOf(jsonObject.getJSONObject("Coordinate").getJSONObject("Boundary").get("MinLatitude"))),0.025f,elementCode));
                if("TMAX".equals(elementCode) || "TMIN".equals(elementCode)){
                    jsonObject.put("Values",forcastDataService.handleTemData(value));
                }
            }
            return WrapMapper.ok(jsonOdata);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("预报矩阵数据、日累计降水数据通过3小时累计")
    @RequestMapping(value = "/matrixDay", method = RequestMethod.GET)
    public Wrapper forcastMatrixDay(String times,String modeCode,String elementCode,String forecastLevel) {
        JSONObject jsonOdata = new JSONObject();
        List<String> timeList = Misc.connectTimeData(times);
        List<JSONObject> dataList = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) { // 指定时间遍历从缓存获取数据
            jsonOdata =  (JSONObject) redisUtils.get(timeList.get(i));
            dataList.add(jsonOdata);
        }
        jsonOdata= forcastDataService.addJsonData(dataList);
        try {
            JSONObject jsonObject = jsonOdata.getJSONObject("Datas");
            JSONArray value  = jsonObject.getJSONArray("Values");
            if(!PublicUtil.isEmpty(value)){ // 数据处理
                jsonObject.put("forcastEleValue",forcastDataService.addCoordinate(
                        value,Float.valueOf(String.valueOf(jsonObject.getJSONObject("Coordinate").getJSONObject("Boundary").get("MinLongitude"))),
                        Float.valueOf(String.valueOf(jsonObject.getJSONObject("Coordinate").getJSONObject("Boundary").get("MinLatitude"))),0.025f,elementCode));
            }
            return WrapMapper.ok(jsonOdata);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }
}
