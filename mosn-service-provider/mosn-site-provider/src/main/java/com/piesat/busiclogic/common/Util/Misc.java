package com.piesat.busiclogic.common.Util;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.piesat.common.util.PublicUtil;
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.XML;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.*;
import java.math.BigDecimal;
import java.net.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @ClassName Misc
 * @Descripton todo
 * @Author sjc
 * @Date 2020/2/4 13:50
 **/
public abstract class Misc {

    /**
     * 校正字符串
     *
     * @param str
     *            字符串
     * @param maxlen
     *            最大长度
     * @return 校正后的字符串。如果长度超过最大长度maxlen，超出部分会被截断
     */
    public static String adjustStringLength(String str, int maxlen) {
        return (str != null && str.length() > maxlen) ? str.substring(0, maxlen) : str;
    }

    /**
     * 校正字符串
     *
     * @param str
     *            字符串
     * @param maxlen
     *            最大长度
     * @param nullvalue
     *            str为null时的值
     * @return 校正后的字符串。如果字符串为null，返回nullvalue；如果字符串长度超过最大长度maxlen，超出部分会被截断
     */
    public static String adjustStringLength(String str, int maxlen, String nullvalue) {
        return (str == null) ? nullvalue : ((str.length() > maxlen) ? str.substring(0, maxlen) : str);
    }

    /**
     * 把字节数组（byte[]）转换成16进制字符串
     *
     * @param bytes
     *            字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();

        if (bytes == null) {
            return null;
        }

        for (int i = 0; i < bytes.length; i++) {
            String hv = Integer.toHexString(bytes[i] & 0xFF);
            if (hv.length() < 2) {
                sb.append(0);
            }
            sb.append(hv);
        }

        return sb.toString();
    }

    /**
     * 获取文件中的字符串
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String getStringByInputStream(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String lineText = null;
            while ((lineText = bufferedReader.readLine()) != null) {
                sb.append(lineText);
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (bufferedReader != null)
                bufferedReader.close();
        }
        return sb.toString();
    }

    /**
     * 拼接多个表示类路径的字符串，形成一个完整的类路径
     *
     * @param pathes
     *            类路径
     * @return 拼接的类路径。判断前一路径的结尾字符和后一路径2的开头字符是否为类路径分隔符，必要时删除或添加类路径分隔符进行拼接
     */
    public static String concatClassPathes(String... pathes) {
        String concatedPath = "";

        if (pathes == null || pathes.length < 1) {
            return concatedPath;
        }

        concatedPath = pathes[0];
        for (int i = 1; i < pathes.length; i++) {
            concatedPath = concatPath(concatedPath, pathes[i], "/");
        }

        return concatedPath;
    }

    /**
     * 拼接多个表示文件路径的字符串，形成一个完整的文件路径
     *
     * @param pathes
     *            文件路径
     * @return 拼接的文件路径。判断前一路径的结尾字符和后一路径2的开头字符是否为文件路径分隔符，必要时删除或添加文件路径分隔符进行拼接
     */
    public static String concatFilePathes(String... pathes) {
        String concatedPath = "";

        if (pathes == null || pathes.length < 1) {
            return concatedPath;
        }

        concatedPath = pathes[0];
        for (int i = 1; i < pathes.length; i++) {
            concatedPath = concatPath(concatedPath, pathes[i], File.separator);
        }

        return concatedPath;
    }

    /**
     * 用指定的分隔符拼接两个路径，形成一个完整的路径
     *
     * @param path1
     *            路径1
     * @param path2
     *            路径2
     * @param separator
     *            路径分隔符
     * @return 拼接的路径。判断路径1的结尾字符和路径2的开头字符是否为指定的分隔符，必要时删除或添加分隔符进行拼接
     */
    public static String concatPath(String path1, String path2, String separator) {
        String path = "";

        if (path1.endsWith(separator) && path2.startsWith(separator)) {
            path = path1 + path2.substring(1);
        } else if (path1.endsWith(separator) || path2.startsWith(separator)) {
            path = path1 + path2;
        } else {
            path = path1 + separator + path2;
        }

        return path;
    }




    /**
     * 把对象列表转换成字符串列表
     *
     * @return 字符串列表
     */
    public static String convertToString(Object obj) {
        if(obj != null){
            return obj.toString();
        }
        return "";
    }
    /*
     * @param objList
     *            对象列表
     * @return 字符串列表
     */
    public static List<String> convertToStringList(List<? extends Object> objList) {
        List<String> strList = null;

        if (objList == null) {
            return strList;
        }

        strList = new ArrayList<>();

        for (Object obj : objList) {
            strList.add((String) obj);
        }

        return strList;
    }

    /**
     * 把对象列表转换成指定类型的列表
     *
     * @param objList
     *            对象列表
     * @return 指定类型的列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> convertToTypedList(Class<T> clz, List<? extends Object> objList) {
        List<T> typedList = null;

        if (objList == null) {
            return typedList;
        }

        typedList = new ArrayList<T>();

        for (Object obj : objList) {
            typedList.add((T) obj);
        }

        return typedList;

    }

    /**
     * 用参数替换字符串中的占位符"#[0-9]"
     *
     * @param msg
     *            字符串
     * @param args
     *            参数
     * @return 替换了占位符的字符串
     */
    public static String format(String msg, String... args) {
        if (msg != null && msg.length() > 0 && msg.indexOf('#') > -1) {
            StringBuilder sb = new StringBuilder();
            boolean isArg = false;

            for (int i = 0; i < msg.length(); i++) {
                char c = msg.charAt(i);
                if (isArg) {
                    isArg = false;
                    if (Character.isDigit(c)) {
                        int val = Character.getNumericValue(c);
                        if (val >= 0 && val < args.length) {
                            sb.append(args[val]);
                            continue;
                        }
                    }
                    sb.append('#');
                }
                if (c == '#') {
                    isArg = true;
                    continue;
                }
                sb.append(c);
            }

            if (isArg) {
                sb.append('#');
            }

            return sb.toString();
        }

        return msg;
    }

    /**
     * 获取类路径
     *
     * @return 类路径
     */
    public static String getClassLocation() {
        String path = "";

        URL url = Misc.class.getResource("/");

        try {
            URI uri = url.toURI();
            path = uri.getPath();
        } catch (URISyntaxException e) {
            path = url.getPath();
        }

        return path;
    }

    /**
     * 获取当前时间
     *
     * @return 当前时间
     */
    public static Date getCurrentTime() {
        return new Date(System.currentTimeMillis());
    }
    /**
     * 取得异常堆栈信息
     *
     * @param e
     *            异常
     * @return 异常堆栈信息
     */
    public static String getExceptionStackTrace(Exception e) {

        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);

            // 将出错的栈信息输出到printWriter中
            e.printStackTrace(pw);

            pw.flush();
            sw.flush();

        } finally {
            if (sw != null) {
                try {
                    sw.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (pw != null) {
                pw.close();
            }
        }

        return (sw == null) ? "" : sw.toString();
    }

    /**
     * 把一个用分割符分割的ID规则码列表表示解析成一个ID集合
     * <p>
     * 用分割符分割的ID规则码形如：<br>
     * 用逗号分割：x01.x02.x03; <br>
     * 用横杠分割：-01-02-03-; <br>
     * 用下划线分割：_a01_a02_03_;
     *
     * @param rules
     *            用分隔符分割的规则码列表
     * @param delimitor
     *            分割符
     * @param omitempty
     *            是否忽略空值
     * @return ID集合
     */
    public static Set<String> getIdsFromDividedRules(List<String> rules, String delimitor, boolean omitempty) {
        Set<String> ids = new HashSet<>();
        if (isEmpty(rules)) {
            return ids;
        }

        for (String rule : rules) {
            String[] arr = Misc.parseToArray(rule, delimitor);
            for (String id : arr) {
                if (omitempty == true && Misc.isEmpty(id)) {
                    continue;
                }
                ids.add(id);
            }
        }

        return ids;
    }

    /**
     * 把一个用分割符分割的ID规则码列表表示解析成一个ID集合
     *
     * @param rule
     *            用分隔符分割的规则码
     * @param delimitor
     *            分割符
     * @param omitempty
     *            是否忽略空值
     * @return ID集合
     */
    public static Set<String> getIdsFromDividedRules(String rule, String delimitor, boolean omitempty) {
        return getIdsFromDividedRules(Arrays.asList(rule), delimitor, omitempty);
    }

    public static String getTimeSpan(long millis) {
        return getTimeSpan(millis, true);
    }

    /**
     * 将表示时间跨度的时长转换为字符型时长
     *
     * @param millis
     *            毫秒级时长
     * @return 字符型时长表示：*天*时*分*秒
     */
    public static String getTimeSpan(long millis, boolean hasSecond) {
        StringBuilder sb = new StringBuilder();

        long days = millis / (1000 * 60 * 60 * 24);
        if (days > 0) {
            sb.append(days).append("天");
        }

        long hours = (millis % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        if (hours > 0) {
            sb.append(hours).append("时");
        }

        long minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        if (minutes > 0) {
            sb.append(minutes).append("分");
        }

        if (hasSecond) {
            long seconds = (millis % (1000 * 60)) / 1000;
            if (seconds > 0) {
                sb.append(seconds).append("秒");
            }
        }

        return sb.toString();
    }

      /**
     * 获取两个时间的时间差
     *
     * @param endTime
     *            开始时间
     * @param startTime
     *            结束时间
     * @return 时间差
     */
    public static long getTimeSpan(String endTime, String startTime, String format) {
        Date end = parseToDate(endTime, format);
        Date start = parseToDate(startTime, format);

        if (end != null & start != null) {
            return end.getTime() - start.getTime();
        } else {
            return 0;
        }
    }



    /**
     * 获取一个32位的UUID值
     *
     * @return 32位的UUID值
     */
    public static String getUUID32() {
        return UUID.randomUUID().toString().replace("-", "");
    }


    /**
     * 判断数组参数是否为空
     *
     * @param list
     *            数组
     * @return 当数组为null或长度为0时返回true，否则返回false
     */
    public static boolean isEmpty(Collection<?> list) {
        return (list == null || list.size() <= 0);
    }

    /**
     * 判断数组参数是否为空
     * @return 当数组为null或长度为0时返回true，否则返回false
     */
    public static <K, V> boolean isEmpty(Map<K, V> map) {
        return (map == null || map.size() <= 0);
    }

    /**
     * 判断字符串参数是否为空，默认经过trim操作后进行判断
     *
     * @param str
     *            字符串
     * @return 当字符串为null或""时返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return isEmpty(str, true);
    }

    /**
     * 判断字符串参数是否为空
     *
     * @param str
     *            字符串
     * @param afterTrim
     *            是否经过trim操作之后再判断
     * @return 当字符串为null或""时返回true，否则返回false
     */
    public static boolean isEmpty(String str, boolean afterTrim) {

        if (str == null) {
            return true;
        }

        String t = (afterTrim ? str.trim() : str);

        return (t.length() <= 0);
    }

    /**
     * 判断数组参数是否为空
     *
     * @param array
     *            数组对象
     * @return 当数组为null或长度小于等于0时返回true，否则返回false
     */
    public static <T> boolean isEmpty(T[] array) {
        return (array == null || array.length <= 0);
    }

    /**
     * 判断字符串参数是否代表true
     *
     * @param str
     *            字符串
     * @return 当字符串（不区分大小写）为"1", "y", "yes", "true"时返回true，否则返回false
     */
    public static boolean isTrue(String str) {
        if ("1".equals(str) || "y".equalsIgnoreCase(str) || "yes".equalsIgnoreCase(str) || "true".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 把列表拼接成一个指定分隔符分割的字符串
     *
     * @param list
     * @return 拼接的字符串
     */
    public static String join(Collection<? extends Object> list) {
        return join(list, ",", false);
    }

    public static String join(Collection<? extends Object> list, boolean omitNullAndEmpty) {
        return join(list, ",", omitNullAndEmpty);
    }

    public static String join(Collection<? extends Object> list, String delimitor) {
        return join(list, delimitor, false);
    }

    public static String join(Collection<? extends Object> list, String delimitor, boolean omitNullAndEmpty) {
        StringBuilder sb = new StringBuilder();

        if (list == null)
            return "";

        for (Object obj : list) {
            if ((obj == null || obj.toString().length() == 0) && omitNullAndEmpty) {
                continue;
            }

            if (sb.length() > 0) {
                sb.append(delimitor);
            }

            if (obj != null) {
                sb.append(obj.toString());
            } else {
                sb.append("NULL");
            }
        }

        return sb.toString();
    }

    /**
     * 把字符串解析成一个byte值
     *
     * @param src
     *            待解析的字符串
     * @param def
     *            缺省值
     * @return 解析的byte值。如果src不是一个byte值，则返回指定的缺省值。
     */
    public static byte parseByte(String src, byte def) {
        byte temp = 0;

        try {
            temp = Byte.parseByte(src);

        } catch (Exception e) {
            temp = def;
        }

        return temp;
    }
    /**
     * 把字符串解析成一个double值
     *
     * @param src
     *            待解析的字符串
     * @param def
     *            缺省值
     * @return 解析的double值。如果src不是一个double值，则返回指定的缺省值。
     */
    public static double parseDouble(String src, double def) {
        double temp = 0;

        try {
            temp = Double.parseDouble(src);

        } catch (Exception e) {
            temp = def;
        }

        return temp;
    }

    /**
     * 把字符串解析成一个float值
     *
     * @param src
     *            待解析的字符串
     * @param def
     *            缺省值
     * @return 解析的float值。如果src不是一个float值，则返回指定的缺省值。
     */
    public static float parseFloat(String src, float def) {
        float temp = 0;

        try {
            temp = Float.parseFloat(src);

        } catch (Exception e) {
            temp = def;
        }

        return temp;
    }

    /*
     * @param src
     *            待解析的字符串
     * @param def
     *            缺省值
     * @return 解析的int值。如果src不是一个int值，则返回指定的缺省值。
     */
    public static int parseInt(String src, int def) {
        int temp = 0;

        try {
            temp = Integer.parseInt(src);

        } catch (Exception e) {
            temp = def;
        }

        return temp;
    }

    /**
     * 把字符串解析成一个long值
     *
     * @param src
     *            待解析的字符串
     * @param def
     *            缺省值
     * @return 解析的long值。如果src不是一个long值，则返回指定的缺省值。
     */
    public static long parseLong(String src, long def) {
        long temp = 0;

        try {
            temp = Long.parseLong(src);

        } catch (Exception e) {
            temp = def;
        }

        return temp;
    }

    /**
     * 把字符串解析成一个short值
     *
     * @param src
     *            待解析的字符串
     * @param def
     *            缺省值
     * @return 解析的short值。如果src不是一个short值，则返回指定的缺省值。
     */
    public static short parseShort(String src, short def) {
        short temp = 0;

        try {
            temp = Short.parseShort(src);

        } catch (Exception e) {
            temp = def;
        }

        return temp;
    }

    /**
     * 用指定的分隔符把字符串分割成一个数组
     *
     * @param clz
     *            数组类型
     * @param toParse
     *            待分割的字符串
     * @param delimitor
     *            分隔符
     * @return 数组
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] parseToArray(Class<T> clz, String toParse, String delimitor) {
        if (toParse == null || toParse.length() == 0) {
            return null;
        }

        return (T[]) toParse.split(delimitor);
    }

    public static String[] parseToArray(String toParse, String delimitor) {
        String[] a = parseToArray(String.class, toParse, delimitor);

        return a;
    }

    /**
     * 把日期格式字符串转换成日期
     *
     * @param dateString
     *            字符串
     * @param format
     *            格式
     * @return 日期
     */
    public static Date parseToDate(String dateString, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(dateString);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     *@desc date 类型转string类型

     *@params date

     */
    public static String parseToString(Date date){
        SimpleDateFormat dateformat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        if(date == null){
            return null;
        }else{
            return dateformat.format(date);
        }
    }


    /**
     * 用指定的分隔符把字符串分割成一个列表
     *
     * @param toParse
     *            待分割的字符串
     * @param delimitor
     *            分隔符
     * @return 列表
     */
    public static <T> List<T> parseToList(String toParse, String delimitor) {
        return parseToList(toParse, delimitor, true);
    }

    /**
     * 用指定的分隔符把字符串分割成一个列表
     *
     * @param toParse
     *            待分割的字符串
     * @param delimitor
     *            分隔符
     * @param trim
     *            是否清楚字符串两边的空格
     * @return 列表
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> parseToList(String toParse, String delimitor, boolean trim) {
        if (toParse == null) {
            return null;
        }

        List<T> list = new ArrayList<T>();
        if (toParse.length() == 0) {
            return list;
        }

        String[] arr = toParse.split(delimitor);
        for (String str : arr) {
            list.add((T) str);
        }

        return list;
    }
    /**
     * 获取字符串中按指定分割符分割后的字符串列表，不包含最后一位
     *
     * @param rule
     *            “-aaa[-bbb-ccc-dddd]-”形式的规则码
     * @param dec
     *            分割符
     * @return 分割后的字符串列表
     */
    public static List<String> splitRule(String rule, String dec) {
        return splitRule(rule, dec, false);
    }

    /**
     * 获取字符串中按指定分割符分割后的字符串列表
     *
     * @param rule
     *            “-aaa[-bbb-ccc-dddd]-”形式的规则码
     * @param dec
     *            分割符
     * @param includeLast
     *            是否包括最后一个ID
     * @return 分割后的字符串列表
     */
    public static List<String> splitRule(String rule, String dec, boolean includeLast) {

        List<String> result = new ArrayList<String>();

        if (!Misc.isEmpty(rule) && !rule.equals(dec)) {
            // 规则码不是空
            // 并且不是只有分割线

            // 去掉头尾的"-"后，按"-"拆分成数组
            String[] t = rule.substring(1, rule.length() - 1).split(dec);

            if (includeLast) {
                // 如果包括自己（最后一个）
                result = Arrays.asList(t);
            } else {
                // 如果不包括自己（最后一个）
                result = Arrays.asList(t).subList(0, t.length - 1);
            }
        }
        List<String> ids = new ArrayList<String>(result);
        return ids;
    }

    /**
     * 把参数列表转换成SQL查询子句中的IN语句
     *
     * @param vals
     *            参数列表
     * @return IN语句
     */
    public static <T> String toWhereIn(Collection<T> vals) {
        if (vals == null || vals.size() == 0) {
            return null;
        }

        String str = "";

        for (int i = 0; i < vals.size(); i++) {
            Object[] arr = vals.toArray();
            Object val = arr[i];

            if (i > 0) {
                str += ",";
            }

            if (val instanceof String) {
                str += "'" + (String) val + "'";
            } else if (val instanceof Integer) {
                str += ((Integer) val).intValue();
            } else if (val instanceof Float) {
                str += ((Float) val).floatValue();
            } else if (val instanceof Double) {
                str += ((Double) val).doubleValue();
            } else if (val instanceof Long) {
                str += ((Long) val).longValue();
            } else if (val instanceof Short) {
                str += ((Short) val).shortValue();
            } else if (val instanceof Byte) {
                str += ((Byte) val).byteValue();
            } else {
                str += "'" + (String) val + "'";
            }
        }

        return str;
    }

    /**
     * 把参数列表转换成SQL查询子句中的IN语句
     * @return IN语句
     */
    public static String toWhereIn(Integer length) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0;i < length;i++){
            sb.append("?,");
        }

        return sb.substring(0,sb.length()-1);
    }

    /**
     * 把字符串的首字母变成大写
     *
     * @param src
     *            字符串
     * @return 首字母变成大写后的字符串
     */
    public static String wordCap(String src) {
        if (src == null) {
            return src;
        } else if (src.length() == 1) {
            return src.toUpperCase();
        } else {
            return src.substring(0, 1).toUpperCase() + src.substring(1);
        }
    }


    /**
     * 	 M
     * 	获取格式为 yyyy-MM 的年月字符串
     * 	的上个月份的年月字符串
     *
     * @param dateStr
     * 				格式为 yyyy-MM 的原日期格式字符串
     * @return
     * 		格式为 yyyy-MM 的上个月的日期格式字符串
     *
     * @throws Exception
     */
    public static String getLastMonth(String dateStr) throws Exception {
        return getLastMonth(dateStr, 0, -1);
    }
    /** 获取格式为 yyyy-MM 的下个月的年月字符串 */
    public static String getNextMonth(String dateStr) throws Exception {
        return getLastMonth(dateStr, 0, 1);
    }

    /**
     *   M
     * 	获取日期格式为 yyyy-MM 的
     *  指定年后/前 月后/前
     *  的日期格式字符串
     *
     * @param dateStr
     * 				格式为 yyyy-MM 的原日期格式字符串
     * @param addYear
     *  			增加的年份
     * @param addMonth
     *  			增加的月份
     * @return
     * 		指定格式为 yyyy-MM 的转换后的日期字符串
     *
     * @throws Exception
     */
    public static String getLastMonth(String dateStr,int addYear, int addMonth) throws Exception {
        return getLastMonth(dateStr, addYear, addMonth, 0, "yyyy-MM");
    }

    /**
     * 	 M
     *	根据指定格式转换指定日期字符串
     *	获取指定年后/前 月后/前 天数后/前
     *	的原日期格式字符串
     *
     * @param dateStr
     * 				原日期格式字符串
     * @param addYear
     * 				增加的年份
     * @param addMonth
     * 				增加的月份
     * @param addDate
     * 				增加的天数
     * @param format
     * 				转换格式
     * @return
     * 		转换后的日期字符串
     *
     * @throws Exception
     */
    public static String getLastMonth(String dateStr,int addYear, int addMonth, int addDate, String format) throws Exception {
        SimpleDateFormat sdf = null;
        Date sourceDate = null;
        Calendar cal = null;
        try {
            sdf = new SimpleDateFormat(format);
            sourceDate = sdf.parse(dateStr);
            cal = Calendar.getInstance();
            cal.setTime(sourceDate);
            cal.add(Calendar.YEAR, addYear);
            cal.add(Calendar.MONTH, addMonth);
            cal.add(Calendar.DATE, addDate);
            dateStr = sdf.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            sdf = null;sourceDate = null;cal = null;
        }
        return dateStr;
    }

    /**
     * Get a full new node path by concat path1 and path1
     *
     * @param path1
     *            the first path
     * @param path2
     *            the second path relative to path1
     * @return new node path
     */
    public static String concatNodePath(String path1, String path2) {
        String path = "";
        if (path1.endsWith("/") && path2.startsWith("/")) {
            path = path1 + path2.substring(1);
        } else if (path1.endsWith("/") || path2.startsWith("/")) {
            path = path1 + path2;
        } else {
            path = path1 + "/" + path2;
        }

        return path;
    }

    /**
     * 拼接两个表示类路径或相对路径的字符串，形成一个完整的类路径
     *
     * @param path1
     *            类路径1
     * @param path2
     *            类路径2
     * @return 拼接的类路径。判断类路径1的结尾字符和类路径2的开头字符是否为"/"，必要时删除或添加"/"进行拼接
     */
    public static String concatClassPath(String path1, String path2) {
        String path = "";
        if (path1.endsWith("/") && path2.startsWith("/")) {
            path = path1 + path2.substring(1);
        } else if (path1.endsWith("/") || path2.startsWith("/")) {
            path = path1 + path2;
        } else {
            path = path1 + "/" + path2;
        }

        return path;
    }

    /**
     * 获取文件名。
     * <p>
     * 从全路径文件名中获取文件名
     *
     * @param fullPathName
     *            全路径文件名
     * @return 文件名
     */
    public static String getFileName(String fullPathName) {
        int pos = fullPathName.lastIndexOf(File.separator);

        if (pos > 0) {
            return fullPathName.substring(pos + 1);
        } else {
            return fullPathName;
        }
    }


    /** 判断字符串是否是整数 **/
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     *  规范籍贯和出生地（省+市、省+县）
     * @param str
     * @return
     */
    public static String getNative (String str){
        if(!Misc.isEmpty(str)){
            if(str.contains("北京")){
                return "北京";
            }else if (str.contains("天津")) {
                return "天津";
            }else if (str.contains("上海")) {
                return "上海";
            }else if (str.contains("重庆")) {
                return "重庆";
            }else if (str.contains("香港")) {
                return "香港";
            }else if (str.contains("澳门")) {
                return "澳门";
            }else if (str.contains("台湾")) {
                return "台湾";
            }else if (str.contains("六枝特区")) {
                return "贵州六枝";
            }else if (str.contains("神农架林区")) {
                return "湖北神农架";
            }

            else if(str.contains("省")){
                String str1 = str.substring(0,str.indexOf("省"));
                String str2 = "";
                if(str.contains("市") && !str.contains("自治州") && !str.contains("省直辖县级行政区划")){
                    if(str.endsWith("县")){
                        str2 = str.substring(str.indexOf("市") + 1,str.indexOf("县"));
                        if(str2.length() == 1 ){
                            str2 += "县";
                        }else if (str2.endsWith("自治")) {
                            str2 = str2.substring(0,2);
                        }
                    }
                    else if(str.endsWith("市")) {
                        if(str.indexOf("市") == str.lastIndexOf("市")){
                            str2 = str.substring(str.indexOf("省") + 1 , str.indexOf("市"));
                        }else {
                            if(str.endsWith("市辖区")){
                                str2 = str.substring(str.indexOf("省") + 1 , str.lastIndexOf("市") - 1);
                            }else {
                                str2 = str.substring(str.indexOf("市") + 1 , str.lastIndexOf("市"));
                            }
                        }
                    }
                    else if (str.endsWith("区")) {
                        if(str.endsWith("市辖区")){
                            str2 = str.substring(str.indexOf("省") + 1 , str.lastIndexOf("市") - 1);
                        }else {
                            str2 = str.substring(str.indexOf("省") + 1 , str.lastIndexOf("市"));
                        }
                    }
                }else if(str.contains("自治州")) {
                    if(str.endsWith("市")) {
                        str2 = str.substring(str.indexOf("州") + 1 , str.lastIndexOf("市"));
                        if(str2.length() == 1){
                            str2 += "市";
                        }
                    }else if (str.endsWith("县")){
                        str2 = str.substring(str.indexOf("州") + 1,str.indexOf("县"));
                        if(str2.length() == 1 ){
                            str2 += "县";
                        }else if (str2.endsWith("自治")) {
                            str2 = str2.substring(0,2);
                        }
                    }else if (!str.endsWith("市") && !str.endsWith("县")) {
                        str2 = str.substring(str.indexOf("省") + 1);
                    }
                }else if(str.contains("省直辖县级行政区划")) {
                    if(str.endsWith("省直辖县级行政区划")) {
                        str2 = "";
                    }else if(str.endsWith("市")) {
                        str2 = str.substring(str.indexOf("划") + 1 , str.lastIndexOf("市"));
                    }else if (str.endsWith("县")) {
                        str2 = str.substring(str.indexOf("划") + 1 , str.lastIndexOf("县"));
                        if(str2.length() == 1 ){
                            str2 += "县";
                        }else if (str2.endsWith("自治")) {
                            str2 = str2.substring(0,2);
                        }
                    }
                }
                if (str2.endsWith("自治")) {
                    str2 = str2.substring(0,2);
                }
                return str1 + str2;
            }

            else if (str.contains("自治区")){
                String str1 = str.substring(0, str.indexOf("自"));
                if(!str1.contains("内蒙古")) {
                    str1 = str1.substring(0 , 2);
                }
                String str2 = "";

                if(str.contains("市") && !str.contains("自治州") && !str.contains("盟") && !str.contains("地区") && !str.contains("自治区直辖县级行政区划")){
                    if(str.endsWith("县")) {
                        str2 = str.substring(str.indexOf("市") + 1,str.indexOf("县"));
                        if(str2.length() == 1 ){
                            str2 += "县";
                        }else if (str2.endsWith("自治")) {
                            str2 = str2.substring(0,2);
                        }
                    } else if(str.endsWith("旗")){
                        str2 = str.substring(str.indexOf("市") + 1);
                    } else if(!str.endsWith("县") && !str.endsWith("旗")) {
                        if(str.indexOf("市") == str.lastIndexOf("市")){
                            str2 = str.substring(str.indexOf("区") + 1 , str.indexOf("市"));
                        }else {
                            if(str.endsWith("市辖区")){
                                str2 = str.substring(str.indexOf("区") + 1 , str.lastIndexOf("市") - 1);
                            }else {
                                str2 = str.substring(str.indexOf("市") + 1 , str.lastIndexOf("市"));
                            }
                        }
                    }
                }else if (str.contains("自治州")) {
                    if(str.endsWith("自治州")) {
                        str2 = str.substring(str.indexOf("区") + 1,str.lastIndexOf("自"));
                    }else if (str.endsWith("市")) {
                        str2 = str.substring(str.indexOf("州") + 1 , str.lastIndexOf("市"));
                    }else if (str.endsWith("县")) {
                        str2 = str.substring(str.indexOf("州") + 1 , str.lastIndexOf("县"));
                        if(str2.length() == 1 ){
                            str2 += "县";
                        }else if (str2.endsWith("自治")) {
                            str2 = str2.substring(0,2);
                        }
                    }
                }else if (str.contains("盟")) {
                    if(str.endsWith("盟")) {
                        str2 = str.substring(str.indexOf("区") + 1,str.indexOf("盟"));
                    }else if(str.endsWith("市")) {
                        str2 = str.substring(str.indexOf("盟") + 1 , str.lastIndexOf("市"));
                    }else if(str.endsWith("县")) {
                        str2 = str.substring(str.indexOf("盟") + 1 , str.lastIndexOf("县"));
                        if(str2.length() == 1 ){
                            str2 += "县";
                        }else if (str2.endsWith("自治")) {
                            str2 = str2.substring(0,2);
                        }
                    }else if (str.endsWith("旗")) {
                        str2 = str.substring(str.indexOf("盟") + 1);
                    }
                }else if (str.contains("地区")) {
                    if(str.endsWith("地区")) {
                        str2 = str.substring( str.indexOf("区") + 1,str.lastIndexOf("地"));
                    }else if(str.endsWith("市")) {
                        str2 = str.substring(str.lastIndexOf("区") + 1 , str.lastIndexOf("市"));
                    }else if(str.endsWith("县")) {
                        str2 = str.substring(str.lastIndexOf("区") + 1 , str.lastIndexOf("县"));
                        if(str2.length() == 1 ){
                            str2 += "县";
                        }else if (str2.endsWith("自治")) {
                            str2 = str2.substring(0,2);
                        }
                    }
                }else if (str.contains("自治区直辖县级行政区划")) {
                    str1 = str1.substring(0 , 2);
                    if(str.endsWith("自治区直辖县级行政区划")) {
                        str2 = "";
                    }else if (str.endsWith("市")) {
                        str2 = str.substring(str.indexOf("划") + 1, str.lastIndexOf("市"));
                    }
                }
                if (str2.endsWith("自治") || str2.contains("伊犁")) {
                    str2 = str2.substring(0,2);
                }
                return str1 + str2;
            }
        }
        return str;
    }

    /**
     * 是否date大于等于todate
     * @param date
     * @param toDate
     * @return
     */
    public static boolean dateGteTo(Date date, Date toDate) {
        return date.after(toDate) || date.equals(toDate);
    }

    /**
     * 是否date小于等于todate
     * @param date
     * @param toDate
     * @return
     */
    public static boolean dateLteTo(Date date, Date toDate) {
        return date.before(toDate) || date.equals(toDate);
    }

    /**
     * 是否date大于todate
     * @param date
     * @param toDate
     * @return
     */
    public static boolean dateGTo(Date date, Date toDate) {
        return date.after(toDate);
    }

    /**
     * 是否date小于todate
     * @param date
     * @param toDate
     * @return
     */
    public static boolean dateLTo(Date date, Date toDate) {
        return date.before(toDate);
    }

    /**
     * 获取指定日期 几 年后\前   的日期
     * @param date
     * @param timeLength
     * @return
     */
    public static Date getAroundDateByYear (Date date, String timeLength) {
        try {
            if (date == null) {
                date = new Date();
            }
            if (!isNumber(timeLength)) {
                return date;
            }
            int length = (int) Math.floor(Double.parseDouble(timeLength) * 12);
            return getAroundDate(date, 0, length, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     *  *
     *	获取指定 指定日期 几  年后/前 月后/前 天数后/前 的日期
     * @param date
     * @param addYear
     * @param addMonth
     * @param addDate
     * @return
     * @throws Exception
     */
    public static Date getAroundDate(Date date, int addYear, int addMonth, int addDate) throws Exception {
        Calendar calendar = null;
        try {
            calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, addYear);
            calendar.add(Calendar.MONTH, addMonth);
            calendar.add(Calendar.DATE, addDate);
            date = calendar.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e.getMessage());
        } finally {
            calendar = null;
        }
        return date;
    }

    /**
     * 正数、负数、小数
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        //
        Pattern pattern = Pattern.compile("^(-?[0-9]+\\.?[0-9]*)$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()){
            return false;
        }
        return true;
    }


    /**
     * 判断字符串是否为正数
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = null;
//		Pattern pattern = Pattern.compile("^(-?[0-9]+.?[0-9]*)$");
//		Pattern pattern = Pattern.compile("^[0-9]+([.]{1}[0-9]+){0,1}$");
        if(str.contains(".")) {
            pattern = Pattern.compile("^([0-9]{1,}[.][0-9]+)$");
        }else {
            pattern = Pattern.compile("^([0-9]{1,})$");
        }
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }


    /**
     * 判断字符串是否为负数
     *
     * @param str
     * @return
     */
    public static boolean isNumericNegative (String str) {
        Pattern pattern = Pattern.compile("-[0-9]+(.[0-9]+)?");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()){
            return false;
        }
        return true;
    }

    /**
     * 比较字符串大小，可以为空
     */

    public static int compareTo(String str1,String str2) {
        if (Misc.isEmpty(str1) && Misc.isEmpty(str2)) {
            return 0;
        }else if (Misc.isEmpty(str1)) {
            return 1;
        }else if (Misc.isEmpty(str2)) {
            return -1;
        }else if (!Misc.isEmpty(str1) && !Misc.isEmpty(str2)){
            return str1.compareTo(str2);
        }
        return 0;
    }
    /**
     * 取得规则码中所有的ID
     *
     * @param rule
     *            “-aaa[-bbb-ccc-dddd]-”形式的规则码
     * @param includeLast
     *            是否包括最后一个ID
     * @return ID列表
     */
    public static List<String> splitRule(String rule, boolean includeLast) {
        List<String> result = new ArrayList<String>();

        if (!Misc.isEmpty(rule) && !rule.equals("-")) {
            // 规则码不是空
            // 并且不是只有分割线

            // 去掉头尾的"-"后，按"-"拆分成数组
            String[] t = rule.substring(1, rule.length() - 1).split("-");

            if (includeLast) {
                // 如果包括自己（最后一个）
                result = Arrays.asList(t);
            } else {
                // 如果不包括自己（最后一个）
                result = Arrays.asList(t).subList(0, t.length - 1);
            }
        }

        return result;
    }

    /**
     * 判断是否为中文
     *
     * @Auther gaozuo
     *
     * @Version Date 2018-10-10 time 下午7:37:37
     *
     * @param source
     * @return
     */
    public static boolean containsHanScript(String source) {
        for (int i = 0; i < source.length(); ) {
            int codepoint = source.codePointAt(i);
            i += Character.charCount(codepoint);
            if (Character.UnicodeScript.of(codepoint) == Character.UnicodeScript.HAN) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取strings字符串中所有str字符所在的下标
     * @param strings 母字符串
     * @param str 子字符串
     * @return 字符串在母字符串中下标集合，如果母字符串中不包含子字符串，集合长度为零
     */
    public static List<Integer> getIndex(String strings, String str){
        List<Integer> list=new ArrayList<>();
        int flag=0;
        while (strings.indexOf(str)!=-1){
            //截取包含自身在内的前边部分
            String aa= strings.substring(0,strings.indexOf(str)+str.length());
            flag=flag+aa.length();
            list.add(flag-str.length());
            strings=strings.substring(strings.indexOf(str)+str.length());
        }
        return list;
    }

    /**
     * 返回格式 :  ?,?,?,?
     * 返回与参数列表相同长度 的 占位符字符串
     * @param list
     * @return
     */
    public static <T> String replacePlaceholderForList(List<String> list) {
        if (isEmpty(list)) {
            return null;
        }
        return toWhereIn(list.size());
    }


    /**
     * 返回格式 :  (field in (?,?,?) or field in (?,?,?))
     * 返回与参数列表相同长度 的 占位符字符串(参数超过1000的情况下使用)
     * @param field 字段名
     * @param list
     * @return
     */
    public static <T> String replacePlaceholderForList(String field, List<String> list) {
        if (isEmpty(list)) {
            return null;
        }
        StringBuffer sb = new StringBuffer();
        sb.append("(");

        for (int i = 0; i < list.size()/1000 + 1; i++) {
            sb.append(" " + field + " in (");
            int length = i == list.size()/1000 ? list.size() - 1000 * i : 1000;
            for (int j = 0; j < length; j++) {
                sb.append("?,");
            }
            sb = new StringBuffer(sb.substring(0, sb.length() - 1));
            sb.append(") or");
        }
        sb = new StringBuffer(sb.substring(0, sb.length() - 2));
        sb.append(")");
        return sb.toString();
    }
    /**
     * 文件格式与浏览器请求contentype之间的替换
     */
    public static String getContentype(String key){
        Map<String,String> map= new HashMap<>();
        map.put(".jpg","image/jpeg");
        map.put(".png","image/png");
        map.put(".gif","image/gif");
        map.put(".txt","text/plain");
        map.put("html","text/html");
        map.put("txt","text/plain");
        map.put(".bmp","image/bmp");
        map.put(".svg","image/svg+xml-compressed");
        map.put(".mp3","audio/mpeg");
        map.put(".rm","application/vnd.rn-realmedia");
        map.put(".avi","video/x-msvideo");
        map.put(".mov","video/quicktime");
        map.put(".mp4","video/mp4");
        map.put(".rmvb","application/vnd.rn-realmedia");
        map.put(".rm","application/vnd.rn-realmedia");
        map.put(".doc","application/msword");
        map.put("docx","application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        map.put(".xls","application/vnd.ms-excel");
        map.put(".ppt","application/vnd.ms-powerpoint");
        return map.get(key);
    }

    /**
     *@desc 将io 流set进入 response对象
     */
    public static void setItoResponse(InputStream inputStream,
                                      HttpServletResponse response){
        ServletOutputStream outputStream = null;
        try {
             outputStream = response.getOutputStream();
             byte[] buffer = new byte[1024];
             while(inputStream.read(buffer)!=-1){
                 outputStream.write(buffer);
             }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     *@desc 文件的copy  tempath 原文件地址
     *                 newpath 新地址
     */
    public  static void copyfile(String tempath,String newpath){

        try {
            InputStream fis = new FileInputStream(new File(tempath));
            OutputStream fos = new BufferedOutputStream(new FileOutputStream(new File(newpath)));
            byte[] buffer = new byte[1024];
            while(fis.read(buffer)!=-1){
                fos.write(buffer);
            }
            fos.flush();
            fis.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * xml格式字符串转换为json
     * @return map数据转换后的json
     * @throws Exception
     */
    public static String xmlToJson1(String xmlString){
        String string = null;
        try {
            //将传的map转化为json格式
            final Map<String,Object> map = xmlmap(xmlString);
            ObjectMapper mapper = new ObjectMapper();
            string = mapper.writeValueAsString(map);
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return string;
    }

    public static Map<String, Object> xmlmap(String xmlString) throws DocumentException {
        //解析xml格式字符串
        Document doc = DocumentHelper.parseText(xmlString);
        //得到XML的根节点
        Element rootElement = doc.getRootElement();
        final Map<String, Object> map = DomMap(rootElement);
        return map;
    }

    public static Map<String, Object> DomMap(Element element){
        //保持顺序
        Map<String, Object> map = new LinkedHashMap<>();
        //得到返回包含子元素的列表进行循环操作
        List elements = element.elements();
        for (Object el : elements) {
            Element e = (Element)el;
            //判断是否该元素只含有text或是空元素
            if(e.isTextOnly()){
                //将包含元素的local name放进put方法中
                put(map,e.getName(),e.getStringValue());
            }
            else{
                //如果该元素下还有其他元素，继续循环
                put(map,e.getName(),DomMap(e));
            }
        }
        return map;
    }


    /**
     * listmap 排序顺序
     * @param list
     * @param key
     * @return
     */
    public static List<Map<String,String>> sortListMap(List<Map<String,String>> list,String key){
        Collections.sort(list, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                Double date1 = Double.valueOf(String.valueOf(o1.get(key)));
                Double date2 =  Double.valueOf(String.valueOf(o2.get(key)));
                return date1.compareTo(date2);
            }
        });
        return list;
    }


    /**
     * listmap 排序顺序
     * @param list
     * @param key
     * @return
     */
    public static List<Map<String,Object>> sortListMapString(List<Map<String,Object>> list,String key){
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                String date1 = String.valueOf(o1.get(key));
                String date2 = String.valueOf(o2.get(key));
                return date1.compareTo(date2);
            }
        });
        return list;
    }

    /**
     * listmap 排序顺序
     * @param list
     * @param key
     * @return
     */
    public static List<Map<String,Object>> sortListMapObject(List<Map<String,Object>> list,String key){
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            @Override
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Double date1 = Double.valueOf(String.valueOf(o1.get(key)));
                Double date2 =  Double.valueOf(String.valueOf(o2.get(key)));
                return date2.compareTo(date1);
            }
        });
        return list;
    }

    /**
     * listmap 排序倒序
     * @param list
     * @param key
     * @return
     */
    public static List<Map<String,String>> sortListMapDesc(List<Map<String,String>> list,String key){
        Collections.sort(list, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                Double date1 = Double.valueOf(String.valueOf(o1.get(key)));
                Double date2 =  Double.valueOf(String.valueOf(o2.get(key)));
                return date2.compareTo(date1);
            }
        });
        return list;
    }

    private static void put(Map<String, Object> map,String key,Object value){
        //根据key取出值进行判断
        final Object obj = map.get(key);
        //取出的值为空表示之前没有存过该键
        if(obj==null){
            map.put(key,value);
        }
        //判断当前对象是否为list
        else if(obj instanceof List){
            ((List) obj).add(value);
        }
        //如果取出的值不为空且非list类型
        else {
            List<Object> list = new ArrayList<>();
            list.add(obj);
            list.add(value);
            map.put(key,list);
        }
    }
    /**
     * 读取 classpath 下 指定的properties配置文件，加载到Properties并返回Properties
     * @param name 配置文件名，如：mongo.properties
     * @return
     */
    public static Properties getConfig(String name){
        Properties props=null;
        try{
            props = new Properties();
            InputStream in = PropertyUtil.class.getClassLoader().getResourceAsStream(name);
            BufferedReader bf = new BufferedReader(new InputStreamReader(in));
            props.load(bf);
            in.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return props;
    }

    public static String getPropValue(String name,String key) throws Exception{

        if(key == null || "".equals(key.trim())){
            return null;
        }
        Resource resource = new ClassPathResource(name);
        InputStream in = resource.getInputStream();
        Properties prop = new Properties();
        prop.load(in);
        String value = prop.getProperty(key);
        if(value == null){
            return null;
        }
        value = value.trim();
        if(value.startsWith("${") && value.endsWith("}") && value.contains(":")){
            int indexOfColon = value.indexOf(":");
            String envName = value.substring(2,indexOfColon);
            String envValue = System.getenv(envName);
            if(envValue == null){
                return value.substring(indexOfColon+1,value.length()-1);
            }
            return envValue;
        }
        return value;
    }

    /**
     * 读取文件内容
     * @param fileName
     * @return
     */
    public static String readFileContent(String fileName) {
        //读取文件
        BufferedReader br = null;
        StringBuffer sb = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "GBK")); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return String.valueOf(sb);
    }

    /**
     * 字符串正则截取
     * @param soap
     * @param rgex
     * @return
     */
    public static String getSubUtil(String soap,String rgex){
        String result ="";
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            int i = 1;
            result=result+m.group(i);
            i++;
        }
        return result;
    }

    //递归获取树型结构数据
    public static List<Map<String, Object>> recursion(String id, List<Map<String, Object>> listData) throws Exception{
        List<Map<String, Object>> treeList = new ArrayList<Map<String, Object>>();
        Iterator it = listData.iterator();
        while (it.hasNext()){
            Map<String, Object> map = (Map<String, Object>)it.next();
            String pid = String.valueOf(map.get("pid"));
            if(PublicUtil.isEmpty(pid)){
                pid = String.valueOf(map.get("PID"));
            }
            if (id.equals(pid)){
                treeList.add(map);
                //使用Iterator，以便在迭代时把listData中已经添加到treeList的数据删除，迭代次数
                it.remove();
            }
        }
        for(Map<String, Object> map:treeList){
            List<Map<String, Object>> treeList123 =  recursion(map.get("id").toString(), listData);
            if(!PublicUtil.isEmpty(treeList123)){
                map.put("childrenList", treeList123);
            }
        }
        return treeList;
    }

    public static JSONObject xml2JsonString(String xml){
        JSONObject xmlJson = XML.toJSONObject(xml);
        return xmlJson;

    }

    public static String createLinkStringByGet(Map<String, String> params) throws UnsupportedEncodingException {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            value = URLEncoder.encode(value, "UTF-8");
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public static String getDirection(String data){
        if("--".equals(data)){
            return "";
        }
        double d = Double.valueOf(data);
        if(d>=0 && d<=22.5){
            return "北风";
        } else if(d > 22.5 && d<= 67.5){
            return "东北风";
        }else if(d > 67.5 && d<=112.5){
            return "东风";
        }else if(d > 112.5 && d<= 157.5){
            return "东南风";
        }else if(d > 157.5 && d<= 202.5){
            return "南风";
        }else if(d > 202.5 && d<=247.5){
            return "西南风";
        }else if(d > 247.5 && d<= 292.5){
            return "西风";
        }else if(d > 292.5 && d<= 337.5){
            return "西北风";
        }else if(d > 337.5 && d<= 360){
            return "北风";
        }
        return "";
    }

    public static String getDirect(String data){
        if("--".equals(data)){
            return "";
        }
        double d = Double.valueOf(data);
        if(d>=0 && d<=30){
            return "北";
        } else if(d > 30 && d<= 60){
            return "东北";
        }else if(d > 60 && d<=120){
            return "东";
        }else if(d > 120 && d<= 150){
            return "东南";
        }else if(d > 150 && d<= 210){
            return "南";
        }else if(d > 210 && d<=240){
            return "西南";
        }else if(d > 240 && d<= 300){
            return "西";
        }else if(d > 300 && d<= 330){
            return "西北";
        }else if(d > 330 && d<= 360){
            return "北";
        }
        return "";
    }

    public static List<Map<String,String>> filterListData(List<Map<String,String>> dataList){
        for (int i = 0; i < dataList.size(); i++) {
            Map<String,String> eleMap = dataList.get(i);
            for (Map.Entry<String, String> entry : eleMap.entrySet()) {
                // 替换缺测为 --
                if("999998".equals(entry.getValue()) || "999999".equals(entry.getValue())|| "999990".equals(entry.getValue())){
                    eleMap.put(entry.getKey(),"--");
                }
            }
        }
        return  dataList;
    }

    public static List<Object> sortMap(Map<String,Object> map){
        List<Object> list = new ArrayList();
        Object[] key = map.keySet().toArray();
        Arrays.sort(key);
        for (int i = 0; i < key.length; i++) {
            list.add(map.get(key[i]));
        }
        return list;
    }

    /**
     * 按行读取文本文件存入list
     * @param file
     * @return
     * @throws IOException
     */
    public static List<String> getContentLineByIo(File file) throws IOException {
        List<String> list = new ArrayList<>();
        if(file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            while ((s = br.readLine()) != null) {
                list.add(s);
            }
        }
        return list;
    }


    /**
     * 从流中将数据读入到list
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static List<String> getListByIo(InputStream inputStream) throws IOException {
        List<String> list = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        String s;
        while ((s = br.readLine()) != null) {
            list.add(s);
        }
        return list;
    }

    public static InputStream getIoByUrl(String url) throws Exception{
        URL readURl = new URL(url);
        URLConnection connection = readURl.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(5000);
        connection.connect();
        return connection.getInputStream();
    }

    public static boolean generateImage(String base64Code, String path) {
        if (null == base64Code) {
            return false;
        }
        byte[] base64Byte = Base64.decodeBase64(base64Code);
        try {
            OutputStream out = new FileOutputStream(path);
            out.write(base64Byte);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static List<Map<String, Object>> removeRepeatMapByKey(List<Map<String, Object>>
                                                                         list, String mapKey){
        if (PublicUtil.isEmpty(list)) return null;

        //把list中的数据转换成msp,去掉同一id值多余数据，保留查找到第一个id值对应的数据
        List<Map<String, Object>> listMap = new ArrayList<>();
        Map<String, Map> msp = new HashMap<>();
        for(int i = list.size()-1 ; i>=0; i--){
            Map map = list.get(i);
            String id = (String)map.get(mapKey);
            map.remove(mapKey);
            msp.put(id, map);
        }
        //把msp再转换成list,就会得到根据某一字段去掉重复的数据的List<Map>
        Set<String> mspKey = msp.keySet();
        for(String key: mspKey){
            Map newMap = msp.get(key);
            newMap.put(mapKey, key);
            listMap.add(newMap);
        }
        return listMap;
    }

    /**
     * 改变图片 像素
     *
     * @param file
     * @param qality 参数qality是取值0~1范围内  清晰程度  数值越小分辨率越低
     * @param imageType 图片写出类型 比如 jpg
     * @return
     * @throws IOException
     */
    public static File compressPictureByQality(File file, float qality, String imageType) throws IOException {
        BufferedImage src = null;
        FileOutputStream out = null;
        ImageWriter imgWrier;
        ImageWriteParam imgWriteParams;
        System.out.println("开始设定压缩图片参数");
        // 指定写图片的方式为 jpg
        imgWrier = ImageIO.getImageWritersByFormatName(imageType).next();
        imgWriteParams = new javax.imageio.plugins.jpeg.JPEGImageWriteParam(
                null);
        // 要使用压缩，必须指定压缩方式为MODE_EXPLICIT
        imgWriteParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        // 这里指定压缩的程度，参数qality是取值0~1范围内，
        imgWriteParams.setCompressionQuality(qality);
        imgWriteParams.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
        ColorModel colorModel = ImageIO.read(file).getColorModel();// ColorModel.getRGBdefault();
        imgWriteParams.setDestinationType(new javax.imageio.ImageTypeSpecifier(
                colorModel, colorModel.createCompatibleSampleModel(32, 32)));
        System.out.println("结束设定压缩图片参数");
        if (!file.exists()) {
            System.out.println("Not Found Img File,文件不存在");
            throw new FileNotFoundException("Not Found Img File,文件不存在");
        } else {
            System.out.println("Not Found Img File,文件不存在");
            System.out.println("图片转换前大小" + file.length() + "字节");
            src = ImageIO.read(file);
            out = new FileOutputStream(file);
            imgWrier.reset();
            // 必须先指定 out值，才能调用write方法, ImageOutputStream可以通过任何
            // OutputStream构造
            imgWrier.setOutput(ImageIO.createImageOutputStream(out));
            // 调用write方法，就可以向输入流写图片
            imgWrier.write(null, new IIOImage(src, null, null),
                    imgWriteParams);
            out.flush();
            out.close();
            System.out.println("图片转换后大小" + file.length() + "字节");
            return file;
        }
    }

    /**
     * java 对网络图片进行压缩
     * @param inputStream
     * @return
     * @throws Exception
     */
    public static String compressToBASE64(InputStream inputStream,double scale,float outqual) throws Exception{
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Thumbnails.of(inputStream)
                .scale(scale)  // 0.5
                .outputQuality(outqual).toOutputStream(out); // 1f
                // 导出到 ByteArrayOutputStream 中

        //将ByteArrayOutputStream转为base64
        return java.util.Base64.getEncoder().encodeToString(out.toByteArray());
    }

    public static List<String> connectTimeData(String dayTime){
        List<String> stringList = new ArrayList<>();
        int d1 = 0;
        String d = "";
        for (int i = 0; i < 8; i++) {
            d1 = 02 + i * 3;
            if(d1>=10){
                d = String.valueOf(d1);
            }else{
                d = "0" +  d1;
            }
            stringList.add(dayTime + d +"0000");
        }
        return stringList;
    }


    public static String changeDateTimeZone(String times){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d = sf.parse(times);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            c.add(Calendar.HOUR_OF_DAY,8);
            return sf.format(c.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 读取匹配文件夹下含有关键字文件
     * @param folder
     * @param keyword
     * @return
     */
    public static List<File> searchFile(File folder, final String keyword) {
        List<File> result = new ArrayList<File>();
        if (folder.isFile())
            result.add(folder);

        File[] subFolders = folder.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                }
                if (file.getName().toLowerCase().contains(keyword)) {
                    return true;
                }
                return false;
            }
        });
        if (subFolders != null) {
            for (File file : subFolders) {
                if (file.isFile()) {
                    // 如果是文件则将文件添加到结果列表中
                    result.add(file);
                }
            }
        }
        return result;
    }
    /**
     * 矩阵数据稀释
     * @param oraginalData
     * @return
     */
    public static float[][] changePrecision(int xPercent,int yPercent,float[][] oraginalData) {

        int xsize = oraginalData.length;
        int ysize = oraginalData[0].length;

        int nxSize = xsize / xPercent;
        int nySize = ysize / yPercent;
        float[][] data2 = new float[nxSize][nySize];

        for (int i = 0; i < nxSize; i++) {
            for (int j = 0; j < nySize; j++) {
                data2[i][j] = oraginalData[xPercent * i][ yPercent * j ];
            }
        }
        return data2;
    }

    /**
     * 二维转换为一维数组
     * @param data1d
     * @param data2d
     */
    public static void arr2dto1dByY2X(float[] data1d, float[][] data2d) {
        int c = 0;
        for (int j = 0; j < data2d[0].length; j++) {
            for (int i = 0; i < data2d.length; i++) {
                data1d[c] = data2d[i][j];
                c++;
            }
        }
    }

    /**
     * 二维转换为一维数组
     * @param data1d
     * @param data2d
     */
    public static void arr2dto1dByY2XNew(float[] data1d, float[][] data2d) {
        int c = 0;
        for (int i = 0; i < data2d.length; i++) {
            for (int j = 0; j < data2d[0].length; j++) {
                data1d[c] = data2d[i][j];
                c++;
            }
        }
    }


    public static float[][] arrXYChange(float[][] data2d){
        float[][] d2 = new float[data2d[0].length][data2d.length];
        for (int i = 0; i < d2.length; i++) {
            for (int j = 0; j < d2[0].length; j++) {
                d2[i][j] = data2d[j][i];
            }
        }
        return d2;
    }

    /**
     * 一维度数组格式调整
     * @param d1
     * @return
     */
    public static float[] handleData(float[] d1){
        DecimalFormat fnum= new DecimalFormat("##0.0");
        float[] rdata = new float[d1.length];
        for (int i = 0; i < d1.length; i++) {
            rdata[i] =Float.valueOf(fnum.format(d1[i]));
        }
        return rdata;
    }

    public static float[] shearData(float[] data,int xCount,int yCount,int xs,int ys){
        float[][] meData = new float[xCount][yCount];
        Misc.arr1dto2dByY2X(data,meData);
        float[][] d2Data = new float[xCount - xs][yCount - ys];
        for (int i1 = 0; i1 < d2Data[0].length; i1++) {
            for (int i = 0; i < d2Data.length; i++) {
                d2Data[i][i1] = meData[i][i1];
            }
        }
        float[] d1Data = new float[(xCount - xs) * (yCount - ys)];
        Misc.arr2dto1dByY2X(d1Data,d2Data);
        return  d1Data;
    }

    public static float[][] shearDataNew(float[][] data,int xCount,int yCount,int xs,int ys){
        float[][] d2Data = new float[xCount - xs][yCount - ys];
        for (int i = 0; i < d2Data.length; i++) {
            for (int j = 0; j < d2Data[0].length; j++) {
                d2Data[i][j] = data[i][j];
            }
        }
        return  d2Data;
    }


    /**
     * 一位数组转换成二维矩阵
     */
    public static void arr1dto2dByY2X(float[] data1d, float[][] data2d) {
        int k = 0;
        for (int j = 0; j < data2d[0].length; j++) {
            for (int i = 0; i <  data2d.length; i++) {
                    data2d[i][j] = data1d[k];
                k++;
            }
        }
    }

    /**
     * 一位数组转换成二维矩阵
     */
    public static void arr1dto2dByY2Xnew(float[] data1d, float[][] data2d) {
        int k = 0;
        for (int i = 0; i < data2d.length; i++) {
            for (int j = 0; j < data2d[0].length; j++) {
                data2d[i][j] = data1d[k];
                k++;
            }
        }
    }

    /**
     * 将二维数组上下翻转
     */
    public static float[][]  upDown(float[][] d){
        float[][] d1 = new float[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d1[i][j] = d[i][d[0].length - j - 1];
            }
        }
        return d1;
    }


    /**
     * 将二维数组上下翻转
     */
    public static float[][]  upDownPre(float[][] d){
        float[][] d1 = new float[d.length][d[0].length];
        for (int i = 0; i < d.length; i++) {
            for (int j = 0; j < d[0].length; j++) {
                d1[i][j] = d[d.length - i - 1][j];
            }
        }
        return d1;
    }



    /**
     * 两个数相减
     * @param f1
     * @param f2
     * @return
     */
    public static float subtract(float f1,float f2){
        BigDecimal b1 = new BigDecimal(String.valueOf(f1));
        BigDecimal b2 = new BigDecimal(String.valueOf(f2));
        return b1.subtract(b2).floatValue();
    }


    /**
     * 将string类型数值类型保留两位有效数字
     * @param num
     * @return
     */
    public static String getPoint2(Double num){
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(num);
    }
}
