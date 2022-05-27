package com.piesat.busiclogic.busic.controller;

import com.piesat.busiclogic.busic.entities.IndexAdvert;
import com.piesat.busiclogic.busic.entities.IndexRotation;
import com.piesat.busiclogic.busic.entities.WeatherDataGrid;
import com.piesat.busiclogic.busic.services.IndexService;
import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.common.anno.Description;
import com.piesat.common.core.support.BaseController;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotEmpty;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/index/noAuth")
@Description("共享系统首页数据controller")
public class IndexController extends BaseController {

    @Autowired
    private IndexService indexService;

    /**
     * 获取指定站点天气预报
     * @param stationId 站号
     * @return
     */
    @RequestMapping(value = "/noSign/getWeatherData", method = RequestMethod.GET)
    public Wrapper getWeather(@RequestParam String stationId){
        try {
            WeatherDataGrid result =  indexService.getWeatherForcast(stationId);
            return  WrapMapper.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
    }

    /**
     * 获取指定经纬度天气预报
     * @return
     */
    @RequestMapping(value = "/noSign/getWeatherDataByLonAndLat", method = RequestMethod.GET)
    public Wrapper getWeatherDataByLonAndLat(@RequestParam String lat,@RequestParam String lon){
        try {
            WeatherDataGrid result =  indexService.getWeatherDataByLonAndLat(lat,lon);
            return  WrapMapper.ok(result);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error(e.getMessage());
        }
    }


    @Description("文件资源预览")
    @RequestMapping(value = "/noSign/view", method = RequestMethod.GET)
    public void searchpic(@RequestParam String url, @RequestParam String name, HttpServletResponse response) {
        try {
            if (!PublicUtil.isEmpty(Misc.getContentype(name))) {
                response.setContentType(Misc.getContentype(name));
            } else {
                response.setContentType(Misc.getContentype(".txt"));
            }
            response.setHeader("Content-disposition ", "inline");
            URL readURl = new URL(url);
            URLConnection connection = readURl.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(5000);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024];
            int n;
            while ((n = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, n);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Description("文件资源预览")
    @RequestMapping(value = "/noSign/viewTxt", method = RequestMethod.GET)
    public void viewFile(@RequestParam String url, @RequestParam String name, HttpServletResponse response) {
        try {
            if (!PublicUtil.isEmpty(Misc.getContentype(name))) {
                response.setContentType(Misc.getContentype(name));
            } else {
                response.setContentType(Misc.getContentype(".txt"));
            }
            response.setHeader("Content-disposition ", "inline");
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] b = decoder.decodeBuffer(url);
            String str=  new String(b);
            String s1 =  str.replace("http://www.ahamic.cn","http://10.129.90.148");
            URL readURl = new URL(s1);
            URLConnection connection = readURl.openConnection();
            connection.setConnectTimeout(1000);
            connection.setReadTimeout(5000);
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            byte[] buff = new byte[1024];
            int n;
            while ((n = inputStream.read(buff)) != -1) {
                outputStream.write(buff, 0, n);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Description("获取站点日出日落时间")
    @RequestMapping(value = "/stationSunDate", method = RequestMethod.GET)
    public Wrapper stationSunDate(@NotEmpty(message = "id 不能为空") String stationId) {
        try {
            Map<String,String> resultMap = indexService.getSunData(stationId);
            return WrapMapper.ok(resultMap);
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    /**
     * -------------------------------------------------------
     * 轮播图设置
     */

    /**
     * -------------------------------------------
     * 轮播跳转图管理
     */

    @Description("新增轮播设置记录")
    @RequestMapping(value = "/addIndexRotation", method = RequestMethod.POST)
    public Wrapper addIndexAdvert(String  indexRotationArray) {
        try {
            indexService.addIndexRotation(indexRotationArray);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("删除轮播图片")
    @RequestMapping(value = "/deleteIndexRotation", method = RequestMethod.GET)
    public Wrapper deleteIndexRotation(@NotEmpty(message = "id 不能为空") String ids) {
        try {
            indexService.deleteIndexRotation(ids);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("编辑轮播图片")
    @RequestMapping(value = "/editIndexRotation", method = RequestMethod.POST)
    public Wrapper deleteIndexRotation(IndexRotation indexRotation) {
        try {
            indexService.editIndexRotation(indexRotation);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }


    @Description("轮播列表信息查询")
    @RequestMapping(value = "/getIndexRotationList", method = RequestMethod.GET)
    public Wrapper getIndexRotationList(IndexRotation indexRotation, HttpServletRequest request) {
        try{
         return   WrapMapper.ok(indexService.getIndexRotationList(indexRotation,request));
        }catch (Exception e){
           return  WrapMapper.error(e.getMessage());
        }
//        return this.handleResult(indexService.getIndexRotationList(indexRotation));
    }

    /**
     * -------------------------------------------------------
     * 首页图片显示管理
     */
    @Description("新增广告图片")
    @RequestMapping(value = "/addIndexAdvert", method = RequestMethod.POST)
    public Wrapper addIndexAdvert(IndexAdvert indexAdvert) {
        try {
            indexService.addIndexAdvert(indexAdvert);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("编辑广告图片")
            @RequestMapping(value = "/editIndexAdvert", method = RequestMethod.POST)
    public Wrapper editIndexAdvert(IndexAdvert indexAdvert) {
        try {
            indexService.editIndexAdvert(indexAdvert);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("启停状态设置")
    @RequestMapping(value = "/setIndexAdvert", method = RequestMethod.POST)
    public Wrapper setIndexAdvert(@RequestParam String status,@RequestParam String id) {
        try {
            indexService.setIndexAdvert(status,id);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("删除广告图片")
    @RequestMapping(value = "/deleteIndexAdvert", method = RequestMethod.GET)
    public Wrapper AddIndexAdvert(@NotEmpty(message = "id 不能为空") String ids) {
        try {
            indexService.deleteIndexAdvert(ids);
            return WrapMapper.ok();
        } catch (Exception e) {
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("广告列表信息查询")
    @RequestMapping(value = "/getIndexAdvertList", method = RequestMethod.GET)
    public Wrapper getIndexAdvertList(@RequestParam long currentPage, @RequestParam int pageSize,IndexAdvert indexAdvert) {
        return this.handleResult(indexService.getIndexAdvertList(currentPage, pageSize,indexAdvert));
    }


    @Description("获取推荐产品")
    @RequestMapping(value = "/getIndexProduct", method = RequestMethod.GET)
    public Wrapper getIndexProduct() {
        return this.handleResult(indexService.getIndexProduct());
    }

    @Description("获取热门产品")
    @RequestMapping(value = "/getHotProduct", method = RequestMethod.GET)
    public Wrapper getHotProduct() {
        return this.handleResult(indexService.getHotProduct());
    }


    /**
     * ---------------------------------------
     * 资料栏信息保存
     */
    @RequestMapping(value = "/addIndexDataColumn",method = RequestMethod.POST)
    public Wrapper AddIndexDataColumn(@RequestParam String idArray){
        try{
          indexService.addIndexDataColumn(idArray);
          return   WrapMapper.ok();
        }catch (Exception e){
          return   WrapMapper.error(e.getMessage());
        }
    }


    @RequestMapping(value = "/addDataBase",method = RequestMethod.POST)
    public Wrapper addDataBase(@RequestParam String id,@RequestParam String base64){
        try{
            indexService.addDataBase(id,base64);
            return   WrapMapper.ok();
        }catch (Exception e){
            return   WrapMapper.error(e.getMessage());
        }
    }

    @Description("资料回显查询")
    @RequestMapping(value = "/getIndexDataColumn",method = RequestMethod.GET)
    public Wrapper getIndexDataColumn(){
        try{
            return WrapMapper.ok(indexService.getIndexDataColumn());
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }

    @Description("资料树结构")
    @RequestMapping(value = "/getIndexDataTree",method = RequestMethod.GET)
    public Wrapper getIndexDataTree(){
        try{
            return WrapMapper.ok(indexService.getIndexDataTree());
        }catch (Exception e){
            return WrapMapper.error(e.getMessage());
        }
    }
}
