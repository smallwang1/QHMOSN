package com.piesat.busiclogic.busic.dataset.service.impl;

import com.piesat.busiclogic.busic.dataset.dao.DataSetDao;
import com.piesat.busiclogic.busic.dataset.service.DataSetService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import com.piesat.busiclogic.common.Util.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class DataSetServiceImpl implements DataSetService {

    @Autowired
    private DataSetDao dataSetDao;

    @Autowired
    private HttpUtil httpUtil;

    // 图片服务接口地址
    private static String File_URL = "http://10.129.90.148:9300/dir/api/queryByData";
    /**
     * 获取产品图
     * @return
     */
    @Override
    public Map<String,String> getProduct(String productParams){
        Map<String,String> resultMap = new HashMap<>();
        String result = httpUtil.post(JSONObject.fromObject(productParams),File_URL);
        // 获取透明图
        JSONObject json = JSONObject.fromObject(result);
        JSONArray arr = json.getJSONArray("result");
        for(int i=0;i <arr.size();i++){
            if(String.valueOf(JSONObject.fromObject(arr.get(i)).get("File_Name")).indexOf("_transparent")>0){
                resultMap.put("FILE_NAME",String.valueOf(JSONObject.fromObject(arr.get(i)).get("File_Name")));
                resultMap.put("Url",String.valueOf(JSONObject.fromObject(arr.get(i)).get("urlAddress")));
            }
        }
        return resultMap;
    }


    /**
     * 远程调用接口获取图片
     * @param response
     */
    @Override
    public void getImg(HttpServletResponse response,String url,String filename) throws Exception{
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

    @Override
    public String getBase64Str(HttpServletResponse response, String url, String s) throws Exception {
        URL readURl = new URL(url);
        URLConnection connection = readURl.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(5000);
        connection.connect();
        //获取流
        InputStream inputStream = connection.getInputStream();
        byte[] data = new byte[inputStream.available()];
        inputStream.read(data);
        return Base64.getEncoder().encodeToString(data);
    }

    public void getTextPWD(){

    }



    @Override
    public List<Map<String, Object>> getDamageData(String starttime, String endtime) {
        return dataSetDao.getDamageData(starttime,endtime);
    }
}
