package com.piesat.busiclogic.webGis.handle;

import com.piesat.busiclogic.common.Util.HttpUtil;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.entity.SettleParams;
import com.piesat.busiclogic.webGis.entity.Transparent;
import com.piesat.common.util.PublicUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Service
public class TransparentService {
    @Autowired
    private HttpUtil httpUtil;

    /**
     * 获取产品图
     * @return
     */
    public Map<String,String> getProduct(Map<String,String> productParams) throws Exception {
        Map<String,String> resultMap = new HashMap<>();
        String interfaceUrl = productParams.get("url");
        productParams.remove("url");
        String result = httpUtil.get(productParams,interfaceUrl);
        // 获取透明图
        JSONObject json = JSONObject.fromObject(result);
        JSONArray arr = json.getJSONArray("result");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        if(arr.size()>0){
            if(String.valueOf(arr.getJSONObject(0).get("FileName")).indexOf("_transparent")>0 || productParams.get("coordinate").equals("coordinate_radar")){
                resultMap.put("FileName",String.valueOf(arr.getJSONObject(0).get("FileName")));
                resultMap.put("FileUrl",String.valueOf(arr.getJSONObject(0).get("FileUrl")));
                if(!PublicUtil.isEmpty(arr.getJSONObject(0).get("DataTime"))){
                    String dateTime = String.valueOf(arr.getJSONObject(0).get("DataTime"));
                    Calendar ca = Calendar.getInstance();
                    ca.setTime(sf.parse(dateTime));
                    ca.add(Calendar.HOUR_OF_DAY,8);
                    resultMap.put("Datetime",sf1.format(ca.getTime()));
                }
            }
        }
//        for(int i=0;i <arr.size();i++){
//
//        }
        return resultMap;
    }

    /**
     * 远程调用接口获取图片
     * @param response
     */
    public void getImg(HttpServletResponse response, String url, String filename) throws Exception{
        URL readURl = new URL(url);
        URLConnection connection = readURl.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(5000);
        connection.connect();
        //获取流
        InputStream inputStream = connection.getInputStream();
        BufferedImage img = ImageIO.read(inputStream);
        if(img!=null){
            // 获取图片格式
            ImageIO.write(img,"png",response.getOutputStream());
        }
    }

    public Transparent getBase64Str(HttpServletResponse response,String qcode, String url,String type) throws Exception {
        URL readURl = new URL(url);
        URLConnection connection = readURl.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(5000);
        connection.connect();
        //获取流
        ByteArrayOutputStream data =  new ByteArrayOutputStream();
        InputStream inputStream = null;
        Transparent transparent = new Transparent();
        try {
            inputStream = connection.getInputStream();
            if("APP".equals(type)){
                transparent.setImgBase64(Misc.compressToBASE64(inputStream,0.5,1f));
            }else{
                int len = -1;
                byte[] by = new byte[1024];
                while ((len = inputStream.read(by))!=-1) {
                    data.write(by, 0, len);
                }
                BASE64Encoder encoder = new BASE64Encoder();
                transparent.setImgBase64(encoder.encode(data.toByteArray()));
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String value = Misc.getPropValue("transparent.properties",qcode);
        if(!PublicUtil.isEmpty(value)){
            transparent.setBbox(Arrays.asList(value.split(",")));
        }
        return transparent;
    }


    public Transparent getBase64StrCompression(HttpServletResponse response, String url) throws Exception {
        URL readURl = new URL(url);
        URLConnection connection = readURl.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(5000);
        connection.connect();
        //获取流
        ByteArrayOutputStream data =  new ByteArrayOutputStream();
        Transparent transparent = new Transparent();
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            transparent.setImgBase64(Misc.compressToBASE64(inputStream,0.4d,1f));
//            int len = -1;
//            byte[] by = new byte[1024];
//            while ((len = inputStream.read(by))!=-1) {
//                data.write(by, 0, len);
//            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        BASE64Encoder encoder = new BASE64Encoder();
//        transparent.setImgBase64(encoder.encode(data.toByteArray()));
        return transparent;
    }

    public Map<String, String> getProductFy2g(SettleParams settleParams) throws Exception {
        Map<String,String> resultMap = new HashMap<>();
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("dataCode",settleParams.getDataCode());
        paramMap.put("interfaceId","apiTimeSectionQuery");
        paramMap.put("elements","DataTime,ProductSort,FileSize,FileUrl,Year,Month,Day,Hour,Min,Second,FileName");
        paramMap.put("eleValueRanges",settleParams.getEleValueRanges());
        paramMap.put("timeRange",settleParams.getTimeRange());
//        paramMap.put("dataformat","JSON");
        paramMap.put("orderBy","DataTime:desc");
        paramMap.put("limitCnt","1");
        String queryUrl = Misc.getPropValue("application.properties","mdes.settleinterface");
        String result = httpUtil.get(paramMap,queryUrl);
        JSONObject json = JSONObject.fromObject(result);
        JSONArray arr = json.getJSONArray("result");
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar ca = Calendar.getInstance();
        for(int i=0;i <arr.size();i++){
            resultMap.put("FileName",String.valueOf(arr.getJSONObject(i).get("FileName")));
            resultMap.put("FileUrl",String.valueOf(arr.getJSONObject(i).get("FileUrl")));
            String dateTime = String.valueOf(arr.getJSONObject(i).get("DataTime"));
            ca.setTime(sf.parse(dateTime));
            ca.add(Calendar.HOUR_OF_DAY,8);
            resultMap.put("Datetime",sf1.format(ca.getTime()));
        }
        return resultMap;
    }
}
