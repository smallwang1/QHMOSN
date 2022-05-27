///**
// * 类名称: HbaseHelper.java
// * 包名称: com.piesat.jdbc
// * <p>
// * 修改履历:
// * 日期       2020/4/10 11:36
// * 修正者     Thomas
// * 主要内容   初版做成
// * <p>
// */
//package com.piesat;
//
//import com.piesat.common.core.constant.GlobalConstant;
//import com.piesat.common.util.DecodeUtil;
//import org.apache.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.hadoop.hbase.HbaseTemplate;
//import org.springframework.stereotype.Component;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//
//import static java.lang.String.valueOf;
//
///**
// * hbase 操作类
// *
// * @Author Thomas 2020/4/10 11:36
// * The world of programs is a wonderful world
// */
//@Component
//public class HbaseHelper {
//
//    Logger logger = LogManager.getLogger(HbaseHelper.class);
//
//
//    @Autowired
//    private HbaseTemplate hbaseTemplate;
//
//
//    /**
//     * 根据时间戳，按照规则生成rowKey
//     *
//     * @param time      时间戳
//     * @param stationId 站点编号
//     * @return
//     */
//    public String getRowKey(long time, String stationId) {
//
//        long dif = GlobalConstant.minute - (time / GlobalConstant.one_minute);
//        int number = DecodeUtil.StringToInteger(stationId.substring(1)) % 5;
//        String rowKey = String.valueOf(number) + stationId + String.valueOf(dif);
//        return rowKey;
//    }
//
//    /**
//     * 根据rowKey ,站点,获取产品rowKey的时间戳
//     *
//     * @param rowKey    hbase rowKey
//     * @param stationId 站点编号
//     * @return
//     */
//    public long getRuleTime(String rowKey, String stationId) {
//        int number = DecodeUtil.StringToInteger(stationId.substring(1)) % 5;
//        String row = number + stationId;
//        String replace = rowKey.replace(row, "");
//        //时间戳
//        long ts = (GlobalConstant.minute - Long.parseLong(replace)) * GlobalConstant.one_minute;
//        return ts;
//    }
//
//    public HbaseTemplate getHbaseTemplate() {
//        return hbaseTemplate;
//    }
//
//    public static void main(String[] args) throws ParseException {
//        //050425100006239
//        HbaseHelper hbaseHelper = new HbaseHelper();
//        long ruleTime = hbaseHelper.getRuleTime("05801573732768", "58015");
//        System.out.println(ruleTime);
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
////        String format = sdf.format(new Date(1576033860000L));
////        //1576066213461
////        System.out.println(format);
//
//        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        String latest = "2018-07-23 00:01:00";
//        // time  2018-07-23 00:00:00 2018-07-23 00:01:00
//        long ts = Long.parseLong(valueOf(format.parse(latest).getTime() - 8 * 60 * 60 * 1000));
//        long l2 = 99999999 - (ts / 60000);
//        int number = Integer.parseInt("I2056".substring(4)) % 5;
//        String rowKey = valueOf(number) + "I2056" + valueOf(l2);
//        System.out.println(rowKey);
//    }
//}
