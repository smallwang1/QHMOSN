package com.piesat.busiclogic.webGis.web;

import com.piesat.busiclogic.common.Util.Misc;
import com.piesat.busiclogic.webGis.entity.SettleParams;
import com.piesat.busiclogic.webGis.entity.Transparent;
import com.piesat.busiclogic.webGis.entity.TransparentParam;
import com.piesat.busiclogic.webGis.handle.TransparentService;
import com.piesat.cimiss.common.util.CimissUtils;
import com.piesat.common.anno.Description;
import com.piesat.common.core.wrapper.WrapMapper;
import com.piesat.common.core.wrapper.Wrapper;
import com.piesat.common.util.PublicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/dataSet/noAuth")
public class TransparentController {

    @Autowired
    private TransparentService transparentService;

    @Description("获取服务图片")
    @RequestMapping(value = "/pic", method = RequestMethod.GET)
    public void searchpic(@RequestParam String url, HttpServletResponse response){
        try{
            String url1 =new String(org.apache.commons.codec.binary.Base64.decodeBase64(url));
            response.setContentType("image/png");
            response.setHeader("Content-disposition ", "inline; filename="+new String("ceshi.png".getBytes("UTF-8")));
            transparentService.getImg(response,url1,"");
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @Description("获取透明图片base64编码信息、默认获取天衍抓取数据,如果时次没有数据获取自生产雷达图")
    @RequestMapping(value = "/planeBase64", method = RequestMethod.GET)
    public Wrapper searchproductpic(TransparentParam transparentParam, HttpServletResponse response){
        try{
            Map<String,String> map =  transparentService.getProduct(CimissUtils.bean2map(transparentParam));
            if("APP".equals(transparentParam.getType())){
                map = new HashMap<>();
            }
            if(PublicUtil.isEmpty(map)){
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmm");
                Map<String,String> paramMap = new HashMap<>();
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(sf.parse(transparentParam.getTimes()));
                calendar.add(Calendar.MINUTE,-20);
                paramMap.put("interfaceId","apiTimeSectionQuery");
                paramMap.put("element",transparentParam.getElements());
                paramMap.put("url",transparentParam.getUrl());
                String timeRange = "("+sf.format(calendar.getTime())+"00,"+transparentParam.getTimes()+"00]";
                paramMap.put("timeRange",timeRange);
                paramMap.put("coordinate","coordinate_radar");
                paramMap.put("orderBy","DataTime:desc");
                if("bfbb4bcf72f9b74f4979a127d3dbc1374736".equals(transparentParam.getDataCode())){ //组合反射率
                    paramMap.put("dataCode","9338fd32yebc0y4c8cya19by9500f46c89a2");
                    map = transparentService.getProduct(paramMap);
                }else if("7fdcd354jc6f5j4bd5j9709ja27a2c0aec7e".equals(transparentParam.getDataCode())){ // 回波顶高
                    paramMap.put("dataCode","f1032cf9mfb02m4b4bm95b5mab572a93f670");
                    map = transparentService.getProduct(paramMap);
                }else if("225ac0d6lfe54l4c9fl9c8bl95cfed03b21b".equals(transparentParam.getDataCode())){ //  垂直积分液态水含量
                    paramMap.put("dataCode","c86dd58561f876440d6ae996e4e02094a840");
                    map = transparentService.getProduct(paramMap);
                }else if("d1f2c8cdd2331d4121d9a16d0eaca6c4fbd6".equals(transparentParam.getDataCode())){ //  雷达雨强组合
                    paramMap.put("dataCode","8d23d57f755d074b357b7ae7bad0937a1f0a");
                    map = transparentService.getProduct(paramMap);
                }
                transparentParam.setCoordinate("coordinate_decode");
            }
            if(!PublicUtil.isEmpty(map)){
                Transparent transparent =  transparentService.getBase64Str(response,transparentParam.getCoordinate(),
                        String.valueOf(map.get("FileUrl")),"");
                transparent.setDataTime(map.get("Datetime"));
                return WrapMapper.ok(transparent);
            } else {
                return WrapMapper.ok(new Transparent());
            }
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error();
        }
    }

    @Description("获取雷达图片")
    @RequestMapping(value = "/planeBaseRatation", method = RequestMethod.GET)
    public Wrapper planeBaseRatation(TransparentParam transparentParam, HttpServletResponse response){
        try{
//            transparentService.getProduct(CimissUtils.bean2map(transparentParam));
            Map<String,String> map =  new HashMap<>();

            if(!PublicUtil.isEmpty(map)){
                Transparent transparent =  transparentService.getBase64Str(response,transparentParam.getCoordinate(),
                        String.valueOf(map.get("FileUrl")),transparentParam.getType());
                return WrapMapper.ok(transparent);
            } else {
                return WrapMapper.ok(new Transparent());
            }
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error();
        }
    }


    @Description("获取fy2g压缩图片")
    @RequestMapping(value = "/fy2gBase64", method = RequestMethod.GET)
    public Wrapper fy2gBase64(SettleParams settleParams, HttpServletResponse response){
        try{
            if(!"d9a0cc85dbe3fd42ffd86bfd52aa38fe8f0d1".equals(settleParams.getDataCode())){ //风云4A产品、查询时区转换
                String timeRange = settleParams.getTimeRange();
                String startTime = timeRange.substring(timeRange.indexOf("(")+1,timeRange.indexOf(","));
                String endTime = timeRange.substring(timeRange.indexOf(",")+1,timeRange.indexOf("]"));
                SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
                Calendar ca = Calendar.getInstance();
                ca.setTime(sf.parse(startTime));
                ca.add(Calendar.HOUR_OF_DAY,-8);
                startTime = sf.format(ca.getTime());
                ca.setTime(sf.parse(endTime));
                ca.add(Calendar.HOUR_OF_DAY,-8);
                endTime = sf.format(ca.getTime());
                timeRange = "("+startTime+","+endTime+"]";
                settleParams.setTimeRange(timeRange);
            }
            Map<String,String> map = transparentService.getProductFy2g(settleParams);
            if(!PublicUtil.isEmpty(map)){
                Transparent transparent =  transparentService.getBase64StrCompression(response,
                        String.valueOf(map.get("FileUrl")));
                if("25ecfcbc81470849fe8b5a68c357d229d058".equals(settleParams.getDataCode())){ // 风云2G
                    String value = Misc.getPropValue("transparent.properties","coordinate_FY2G");
                    if(!PublicUtil.isEmpty(value)){
                        transparent.setBbox(Arrays.asList(value.split(",")));
                    }
                }else{ // 风云4A
                    String value = Misc.getPropValue("transparent.properties","coordinate_FY4A");
                    if(!PublicUtil.isEmpty(value)){
                        transparent.setBbox(Arrays.asList(value.split(",")));
                    }
                }
                transparent.setDataTime(map.get("Datetime"));
                return WrapMapper.ok(transparent);
            } else {
                return WrapMapper.ok(new Transparent());
            }
        }catch (Exception e){
            e.printStackTrace();
            return WrapMapper.error();
        }
    }
}
