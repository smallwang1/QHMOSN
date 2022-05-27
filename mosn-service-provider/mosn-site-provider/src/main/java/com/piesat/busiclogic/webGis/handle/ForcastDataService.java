package com.piesat.busiclogic.webGis.handle;

import com.alibaba.fastjson.JSON;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.common.Util.WindUtil;
import com.piesat.busiclogic.webGis.entity.GrdElement;
import com.piesat.common.util.PublicUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ForcastDataService {


    public JSONObject forcastMatrix(String queryTime,String modeCode,String elementCode,String forecastLevel) throws Exception {

        // 预报发布时间设置当前一天的20时发布数据
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMdd");
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        String initialtime = sdf.format(calendar.getTime())+"200000";
        if("TMAX".equals(elementCode) || "TMIN".equals(elementCode)){
            queryTime = queryTime +"080000";
            initialtime = sdf.format(calendar.getTime())+"080000";
        }
        // 获取token
        URL urlToken = new URL( Misc.getPropValue("transparent.properties","forcast_token"));
        JSONObject tokenData = JSONObject.fromObject(PublicUtil.getResultData(urlToken));
        JSONObject result = new JSONObject();

        if(!PublicUtil.isEmpty(tokenData) && !PublicUtil.isEmpty(JSONObject.fromObject(tokenData.get("Datas")).get("Token"))){
            Map<String,String> paramMap = new HashMap<>();
            paramMap.put("Token",String.valueOf(JSONObject.fromObject(tokenData.get("Datas")).get("Token")));
            paramMap.put("InitialTime",initialtime);
            paramMap.put("ModeCode",modeCode);
            paramMap.put("MemberCode","BEHF");
            paramMap.put("ElementCode",elementCode);
            paramMap.put("TargetTime",queryTime);
            paramMap.put("ForecastLevel",forecastLevel);
            String lonlatArea = Misc.getPropValue("transparent.properties","coordinate");
            String[] lonlat = lonlatArea.split(",");
            paramMap.put("MinLon",lonlat[0]);
            paramMap.put("MinLat",lonlat[1]);
            paramMap.put("MaxLon",lonlat[2]);
            paramMap.put("MaxLat",lonlat[3]);
            URL url3hforest = new URL(Misc.getPropValue("transparent.properties","forcast_matrix") + Misc.createLinkStringByGet(paramMap));
            result = JSONObject.fromObject(PublicUtil.getResultData(url3hforest));
        }
        return  result;
    }

    public List<GrdElement> addCoordinate(JSONArray flist, float x0, float y0, float size,String elementCode){

        List<GrdElement> grdElements = new ArrayList<>();
        for( int j = 0 ; j < flist.size() ; j++){
            for (int i = 0; i < flist.getJSONArray(0).size(); i++) {
                GrdElement grdElement = new GrdElement();
                grdElement.setX0(String.valueOf(x0 + i*size + 0.005));
                grdElement.setY0(String.valueOf(y0 + j*size + 0.005));
                if("TMAX".equals(elementCode) || "TMIN".equals(elementCode)){
                    grdElement.setValue(Misc.subtract(Float.valueOf(String.valueOf(flist.getJSONArray(j).get(i))),273.15f));
                }else{
                    grdElement.setValue(Float.valueOf(String.valueOf(flist.getJSONArray(j).get(i))));
                }
                grdElements.add(grdElement);
            }
        }
        return grdElements;
    }


    public  Float[] convertData(Float[][] data) {
        Float[] toReturn = new Float[data.length * data[0].length];
        for (int i = 0; i < toReturn.length; i++) {
            int b = i/data[0].length;
            int a = i%data[0].length;
            toReturn[i] = data[b][a];
        }
        return toReturn;
    }

    public JSONObject forcastMatrixWind(String times,String uCode,String vCode ,String modeCode, String forecastLevel)  throws Exception{
        // 获取EDA10_U 方向风速
        JSONObject uJasonObject = this.forcastMatrix(times,modeCode,uCode,forecastLevel);
        JSONObject vJasonObject = this.forcastMatrix(times,modeCode,vCode,forecastLevel);
        if(!PublicUtil.isEmpty(uJasonObject) && !PublicUtil.isEmpty(vJasonObject)){

           JSONObject coorbox =  uJasonObject.getJSONObject("Datas").getJSONObject("Coordinate").getJSONObject("Boundary");
           Float[][] uArry = JSON.parseObject(String.valueOf(uJasonObject.getJSONObject("Datas").getJSONArray("Values")),Float[][].class);
           Float[][] vArry = JSON.parseObject(String.valueOf(vJasonObject.getJSONObject("Datas").getJSONArray("Values")),Float[][].class);
           vJasonObject.getJSONObject("Datas").get("Values");
           JSONObject jo = WindUtil.getDynamicWindStream(uArry,vArry,Float.valueOf(String.valueOf(coorbox.get("MinLongitude"))),Float.valueOf(String.valueOf(coorbox.get("MaxLongitude"))),
                   Float.valueOf(String.valueOf(coorbox.get("MaxLatitude"))),Float.valueOf(String.valueOf(coorbox.get("MinLatitude"))));
            return jo;
        }
        return  null;
    }


    public JSONObject addJsonData(List<JSONObject> jList){
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < jList.size(); i++) {
            JSONObject jsonObject = jList.get(i).getJSONObject("Datas");
            JSONArray eleValue  = jsonObject.getJSONArray("Values");
            for (int jx = 0; jx < eleValue.size(); jx++) {
                JSONArray jyJsonArray = new JSONArray();
                if(i != 0){
                    jyJsonArray = jsonArray.getJSONArray(jx);
                }
                for (int jy = 0; jy < eleValue.getJSONArray(0).size(); jy++) {
                    if(i == 0){
                        jyJsonArray.add(jy,eleValue.getJSONArray(jx).get(jy));
                    }else{
                        jyJsonArray.set(jy,Double.valueOf(String.valueOf(jyJsonArray.get(jy))) + Double.valueOf(String.valueOf(eleValue.getJSONArray(jx).get(jy))));
                    }
                }
                if(i == 0){
                    jsonArray.add(jx,jyJsonArray);
                }else{
                    jsonArray.set(jx,jyJsonArray);
                }
            }
        }
        System.out.println(jsonArray);
        JSONObject jsonObject1 = jList.get(0);
        JSONObject jsonObject2 = jsonObject1.getJSONObject("Datas");
        jsonObject2.put("Values",jsonArray);
        return jsonObject1;
    }

    /**
     * 温度格点数据、转换为摄氏温度
     * @param value
     * @return
     */
    public Object handleTemData(JSONArray value) {
        for (int i = 0; i < value.size(); i++) {
            JSONArray col = value.getJSONArray(i);
            for (int j = 0; j < col.size(); j++) {
                col.set(j,Misc.subtract(Float.valueOf(String.valueOf(col.get(j))) , 273.15f));
            }
        }
        return value;
    }
}
