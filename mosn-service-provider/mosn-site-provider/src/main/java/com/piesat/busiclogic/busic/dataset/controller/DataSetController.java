//package com.piesat.busiclogic.busic.dataset.controller;
//
//import com.piesat.busiclogic.busic.dataset.service.DataSetService;
//import com.piesat.busiclogic.common.Util.Misc;
//import com.piesat.common.anno.Description;
//import com.piesat.common.core.wrapper.WrapMapper;
//import com.piesat.common.core.wrapper.Wrapper;
//import net.sf.json.JSONObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.FileCopyUtils;
//import org.springframework.web.bind.annotation.*;
//import javax.servlet.http.HttpServletResponse;
//import java.io.*;
//import java.net.URL;
//import java.net.URLConnection;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/dataSetOld")
//public class DataSetController {
//
//    @Autowired
//    private DataSetService dataSetService;
//
//
//
//    @Description("获取服务图片接口数据")
//    @RequestMapping(value = "/productpic", method = RequestMethod.POST)
//    public Wrapper searchproductpic(@RequestBody Map<String,Object> params, HttpServletResponse response){
//        try{
//            Map<String,String> map = dataSetService.getProduct(JSONObject.fromObject(params).toString());
//            return WrapMapper.ok(map);
//        }catch (Exception e){
//            e.printStackTrace();
//            return WrapMapper.error();
//        }
//    }
//
//    @Description("获取服务图片")
//    @RequestMapping(value = "/pic", method = RequestMethod.GET)
//    public void searchpic(@RequestParam String url, HttpServletResponse response){
//        try{
//            response.setContentType("image/png");
//            response.setHeader("Content-disposition ", "inline; filename="+new String("ceshi.png".getBytes("UTF-8")));
//            dataSetService.getImg(response,url,"");
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Description("获取服务图片")
//    @RequestMapping(value = "/pic", method = RequestMethod.GET)
//    public void searchBase64(@RequestParam String url, HttpServletResponse response){
//        try{
//
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//
//    @Description("远程调用接口获取文件数据")
//    @RequestMapping(value = "/file", method = RequestMethod.GET)
//    public void getDataByCall(@RequestParam String url, HttpServletResponse response){
//        try{
//            response.setHeader("Content-disposition", "inline; filename="+new String("ceshi.txt".getBytes("gbk")));
//            URL readURl = new URL(url);
//            URLConnection connection = readURl.openConnection();
//            connection.setConnectTimeout(1000);
//            connection.setReadTimeout(5000);
//            connection.connect();
//            byte[] bytes = new byte[1024];
//            //获取流
//            InputStream inputStream = connection.getInputStream();
//            FileCopyUtils.copy(inputStream, response.getOutputStream());
//            inputStream.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//    @Description("获取灾害数据集")
//    @RequestMapping(value = "/searchDamageData", method = RequestMethod.GET)
//    public Wrapper searchDamageData(@RequestParam String starttime,@RequestParam String endtime) {
//        try {
//            List<Map<String,Object>> resultList= dataSetService.getDamageData(starttime,endtime);
//            return WrapMapper.ok(resultList);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return WrapMapper.error();
//        }
//    }
//}
