package com.piesat.busiclogic.webGis.entity;

import lombok.Data;
import org.springframework.core.io.ClassPathResource;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Data
public class CimissQueryParam {

//    private String path = "/space/cmadaas/mdes/busic/template/cimiss_stationData_config.xml";

    private String path  = getconfigPath("cimiss_stationData_config.xml");

    private String userId = "user_xxzx";

    private String pwd = "user_xxzx123";

    // 默认请求小时数据
    private String dataCode = "SURF_CHN_MUL_HOR";//

    private String interfaceId;

    private String elements;

    private String times = getTime();

    private String staLevels;

    private String statEles;

    private String typhCIds;

    private String reportCenters;

    private String point_id;
    /**
     * 查询区间范围
     */
    private String timeRang = getrange();

    /**
     * 要素区间范围
     */
    private String eleValueRanges;

    /**
     * 国内行政编码
     */
    private String adminCodes;

    /**
     * 站号
     */
    private String staIds;

    /**
     *排序字段
     */
    private String orderBy;

    /**
     * 最大返回记录数
     */
    private String limitCnt;


    private String url;

    /**
     * 文件管理系统 参数时间类型
     */
    private String timeDimension;

    /**
     * 默认未传时间点默认为当前时间的前一个小时
     * @return
     */
    public static String getTime(){
        Date d= new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY) - 1);
        Date time= c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        return  df.format(time)+ "0000";
    }

    public static void main(String[] args) {
        String time = CimissQueryParam.getTime();
        System.out.println(time);
    }

    /**
     * 参数未传默认当前时间的前24个小时
     */
    public static String getrange() {
            Date d= new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.set(Calendar.HOUR_OF_DAY,c.get(Calendar.HOUR_OF_DAY) - 32);
            Date time= c.getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
            return  "("+df.format(time)+"0000,"+df.format(d)+"0000"+"]";
    }


    /**
     * 获取配置文件路径
     */
    public static String getconfigPath(String name){
        ClassPathResource classPathResource = new ClassPathResource(name);
        try {
            return classPathResource.getFile().getPath();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
