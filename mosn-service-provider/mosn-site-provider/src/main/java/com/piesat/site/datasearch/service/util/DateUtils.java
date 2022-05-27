package com.piesat.site.datasearch.service.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期工具类
 */
public class DateUtils {

    private static SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    private static SimpleDateFormat datetime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static Date getNowDate(){
        return new Date();
    }

    public static String createRandomNo(Date date){
        String randomNo = "";
        Random random = new Random();
        String fourRandom = random.nextInt(9999) + "";
        int randLength = fourRandom.length();
        //四位随机数，不足四位的补0
        if(fourRandom.length()<4){//不足四位的随机数补充0
            for(int i = 1; i <= 4 - randLength; i++){
                fourRandom = '0' + fourRandom;
            }
        }
        randomNo = "NO".concat(sdf.format(date)+fourRandom);

        return randomNo;
    }
    /**
     * 获得以 yyyy-MM-dd 为形式的当前时间字符串
     * @return
     */
    public static String getCurrentTimeByDay() {
        String time = date.format(new Date(System.currentTimeMillis()));
        return time;
    }

    /**
     * 获得以 yyyy-MM-dd HH:mm:ss 为形式的当前时间字符串
     * @return
     */
    public static String getCurrentTimeBySecond() {
        String time = datetime.format(new Date(System.currentTimeMillis()));
        return time;
    }

    /**
     * 获得给定格式的当前时间字符串
     * @param give
     * @return
     */
    public static String getCurrentTime(String give) {
        SimpleDateFormat temp = new SimpleDateFormat(give);
        return temp.format(new Date(System.currentTimeMillis()));
    }

    /**
     * 将String转化成date
     * @param str
     * @param sfgs
     * @return
     * @throws ParseException
     */
    public static Date pStringToDate(String str, String sfgs)
            throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat(sfgs);
        return sf.parse(str);
    }

    /**
     * 将String转化成date 格式为yyyy-MM-dd hh:mm:ss
     * @param str
     * @return
     * @throws ParseException
     */
    public static Date pStringToDate(String str) throws ParseException {
        return datetime.parse(str);
    }

    /**
     * 转换成日期格式的字符串 格式为yyyy-MM-dd
     * @param o
     * @return
     */
    public static String dateFormat(Date o) {
        if (o == null) {
            return "";
        }
        return sdf.format(o);
    }

    /**
     * 转换成时间格式的字符串 格式为yyyy-MM-dd hh:mm:ss
     * @param o
     * @return
     */
    public static String dateTimeFormat(Date o) {
        if (o == null) {
            return "";
        }
        return datetime.format(o);
    }

    /**
     * 转换成给定时间格式的字符串
     * @param d
     * @param format
     * @return
     */
    public static String getDateFormat(Date d, String format) {
        return new SimpleDateFormat(format).format(d);
    }

    /**
     * 日期格式化(yyyy年MM月dd日)
     * @param date
     * @return
     */
    public static String fDateCNYR(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日");
    }

    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm)
     * @param date
     * @return
     */
    public static String fDateCNYRS(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日 HH点");
    }

    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm)
     * @param date
     * @return
     */
    public static String fDateCNYRSF(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日 HH:mm");
    }

    /**
     * 日期格式化(yyyy年MM月dd日 HH:mm:ss)
     * @param date
     * @return
     */
    public static String fDateCNYRSFM(Date date) {
        return getDateFormat(date, "yyyy年MM月dd日 HH:mm:ss");
    }

    /**
     * 根据给定的时间格式字符串截取给定格式的字符串
     * @param d
     * @param format
     * @return
     * @throws ParseException
     */
    public static String getDateFormat(String d, String format) throws ParseException {
        Date date = datetime.parse(d);
        return getDateFormat(date, format);
    }

    /**
     * 通过字符串获得long型时间
     * @param dateStr
     * @return
     */
    public static long getDateFromStr(String dateStr) {
        long temp = 0L;
        Date date = null;
        try {
            date = datetime.parse(dateStr);
        } catch (Exception e) {
            e.printStackTrace();
            return temp;
        }
        temp = date.getTime();
        return temp;
    }

    /**
     * 日期格式化（2014-03-04）
     * @param dat
     * @return
     * @throws ParseException
     */
    public static Date fDate(Date dat) throws ParseException {
        String dateStr = datetime.format(dat);
        return datetime.parse(dateStr);
    }

    /**
     * 格式化字符串，将2013-10-20 00:00:00.000000简化为2013-10-20 00:00:00
     * @param str
     * @return
     * @throws ParseException
     */
    public static String getFormatStringDay(String str) throws ParseException {
        Date date = datetime.parse(str);
        return datetime.format(date);
    }

    /**
     * 如果字符串长度大于25则截取前25个字符串后续改成省略号
     * @param str
     * @return
     */
    public static String showCount(String str) {
        if (str != null) {
            if (str.length() > 25) {
                str = str.substring(0, 25);
                str = str + "...";
            }
        } else {
            str = "";
        }
        return str;
    }

    /**
     * 是否符合日期格式yyyy-MM-dd
     * @param day
     * @return
     */
    public static boolean isFormatDay(String day) {
        return day.matches("(([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]{1}|[0-9]{1}[1-9][0-9]{2}|[1-9][0-9]{3})-(((0[13578]|1[02])-(0[1-9]|[12][0-9]|3[01]))|((0[469]|11)-(0[1-9]|[12][0-9]|30))|(02-(0[1-9]|[1][0-9]|2[0-8]))))|((([0-9]{2})(0[48]|[2468][048]|[13579][26])|((0[48]|[2468][048]|[3579][26])00))-02-29)");
    }

    /**
     * 是否符合时间格式HH:mm:ss
     * @param time
     * @return
     */
    public static boolean isFormatTime(String time) {
        return time.matches("(0[1-9]|1[0-9]|2[0-4]):(0[1-9]|[1-5][0-9]):(0[1-9]|[1-5][0-9])(\\.000000)?");
    }

    /**
     * 是否符合时间格式yyyy-MM-dd HH:mm:ss
     * @param time
     * @return
     */
    public static boolean isFormat(String time) {
        String[] temp = time.split(" ");
        return isFormatDay(temp[0]) && isFormatTime(temp[1]);
    }

    /**
     * 通过给定的年、月、周获得该周内的每一天日期
     * @param year
     * @param month
     * @param week
     * @return
     */
    public static List<Date> getDayByWeek(int year, int month, int week) {
        List<Date> list = new ArrayList<Date>();
        // 先滚动到该年.
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        // 滚动到月:
        c.set(Calendar.MONTH, month - 1);
        // 滚动到周:
        c.set(Calendar.WEEK_OF_MONTH, week);
        // 得到该周第一天:
        for (int i = 0; i < 6; i++) {
            c.set(Calendar.DAY_OF_WEEK, i + 2);
            list.add(c.getTime());
        }
        // 最后一天:
        c.set(Calendar.WEEK_OF_MONTH, week + 1);
        c.set(Calendar.DAY_OF_WEEK, 1);
        list.add(c.getTime());
        return list;
    }

    /**
     * 获得当前日期是本月的第几周
     * @return
     */
    public static int getCurWeekNoOfMonth() {
        Date date = new Date(System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * 获得当前日期是星期几
     * @param dat
     * @return
     */
    public static int getCurWeekNo(String dat) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(dat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * 获得当前的年份
     * @return
     */
    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获得当前的月份
     * @return
     */
    public static int getCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.MONTH) + 1;
    }

    /**
     * 获得当前的日期天
     * @return
     */
    public static int getCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.DATE);
    }

    /**
     * 获取当月最后一天
     * @param date
     * @param format
     * @return
     */
    public static String lastDayOfMoth(Date date, String format){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH,1);
        cal.add(Calendar.MONTH,1);
        cal.add(Calendar.DATE, -1);
        date =  cal.getTime();;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 获取当月最后一天
     * @param date
     * @param format
     * @return
     */
    public static String firstDayOfMoth(Date date, String format){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, 0);
        date =  cal.getTime();;
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 转换成字符串方法，其中如果是Integer格式的返回0，如果是Double格式的返回0.0
     * @param o
     * @return
     */
    public static String toString(Object o) {
        if (o == null) {
            if (o instanceof Integer) {
                return "0";
            }
            if (o instanceof Double) {
                return "0.0";
            }
            return "";
        } else {
            return o.toString();
        }
    }

    /**
     * 清空字符串，如果为“”则转换成null
     * @param src
     * @return
     */
    public static String emptyString2Null(String src) {
        if (src != null) {
            if ("".equals(src)) {
                src = null;
            }
        }
        return src;
    }

    /**
     * 转化成可在hql中使用的字符串
     * 1,2 转为 '1','2'
     * @param ids
     * @return
     */
    public static String formatIds(String ids){
        if(ids!=null&&ids!="") {
            String[] id = ids.split(",");
            StringBuffer idsStr = new StringBuffer();
            for(String str : id){
                idsStr.append("'"+str+"',");
            }
            return idsStr.toString().substring(0,idsStr.length()-1);
        } else {
            return "";
        }
    }

    /**
     * 获取当前日期前一天
     * @param date
     * @return
     */
    public static Date getSpecifiedDayBefore(Date date){
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int day = c.get(Calendar.DATE);
        c.set(Calendar.DATE, day-1);
        date = c.getTime();
        return date;
    }

    /**
     * 比较两个日期的大小
     * @param date1
     * @param date2
     * @return
     */
    public boolean bjDate(Date date1, Date date2){
        if (date1.getTime() > date2.getTime()) {
            return true;
        }
        return false;
    }
}
