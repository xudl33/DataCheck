package com.wisea.cloud.common.datacheck.util;

import java.beans.IntrospectionException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.wisea.cloud.common.datacheck.annotation.CheckApi;
import com.wisea.cloud.common.datacheck.annotation.CheckModel;
import com.wisea.cloud.common.datacheck.annotation.NullStrategy;

/**
 * 转换工具类
 * 
 * @author XuDL
 */
@CheckModel
public final class ConverterUtil {
    /** 数字. */
    public static final String DATA_TYPE_NUMBER = "U";

    /** 文字. */
    public static final String DATA_TYPE_CHAR = "C";

    /** 补全类别：SAME. */
    public static final String COMPLEMENT_SAME = "SAME";

    /** 分隔符：元素 */
    public static final String SEPARATOR_ELEMENT = "#EM#";

    /** 分隔符：key-value */
    public static final String SEPARATOR_KEY_VALUE = "#KV#";

    /** Excel导出单元格属性：title */
    public static final String EXCEL_TITLE = "title";

    /** Excel导出单元格属性：color */
    public static final String EXCEL_COLOR = "color";

    /** Excel导出单元格属性：color */
    public static final String EXCEL_ALIGN = "align";

    /** Excel导出单元格属性：width */
    public static final String EXCEL_WIDTH = "width";

    /** Excel导出单元格宽度：最小值 */
    public static final int EXCEL_WIDTH_MIN = 10;

    /** Excel导出单元格宽度：最大值 */
    public static final int EXCEL_WIDTH_MAX = 50;

    /** Excel导出单元格WIDTH：auto */
    public static final String EXCEL_WIDTH_AUTO = "auto";

    /** 时间格式：日期型(yyyy/MM/dd) */
    public static final String FORMATE_DATE = "yyyy/MM/dd";

    /** 时间格式：日期型(yyyy-MM-dd) */
    public static final String FORMATE_DATE_MLINE = "yyyy-MM-dd";

    /** 时间格式：日期型(yyyy年MM月dd日) */
    public static final String FORMATE_DATE_CHINA = "yyyy年MM月dd日";

    /** 时间格式：日期时间24小时制型(yyyy/MM/dd HH:mm:ss) */
    public static final String FORMATE_DATE_TIME_24H = "yyyy/MM/dd HH:mm:ss";

    /** 时间格式：日期时间24小时制型(yyyy-MM-dd HH:mm:ss) */
    public static final String FORMATE_DATE_TIME_24H_MLINE = "yyyy-MM-dd HH:mm:ss";

    /** 时间格式：日期时间24小时制型(yyyy年MM月dd日 HH:mm:ss) */
    public static final String FORMATE_DATE_TIME_24H_CHINA = "yyyy年MM月dd日 HH:mm:ss";

    /** 时间格式：日期时间12小时制型(yyyy/MM/dd hh:mm:ss) */
    public static final String FORMATE_DATE_TIME_12H = "yyyy/MM/dd hh:mm:ss";

    /** 时间格式：时间戳24小时制型(yyyy/MM/dd HH:mm:ss.SSS) */
    public static final String FORMATE_TIME_STAMP_24H = "yyyy/MM/dd HH:mm:ss.SSS";

    /** 时间格式：时间戳24小时制型(yyyy-MM-dd HH:mm:ss.SSS) */
    public static final String FORMATE_TIME_STAMP_24H_MLINE = "yyyy-MM-dd HH:mm:ss.SSS";

    /** 时间格式：时间戳12小时制型(yyyy/MM/dd hh:mm:ss.SSS) */
    public static final String FORMATE_TIME_STAMP_12H = "yyyy/MM/dd hh:mm:ss.SSS";

    /** 正则日期类型:中线 */
    public static final String REGEX_DATE_MIDDELLINE = "^\\d{4}-(0?[1-9]|[1][012])-(0?[1-9]|[12][0-9]|[3][01])$";

    /** 正则日期类型:反斜线 */
    public static final String REGEX_DATE_BACKSLASH = "^\\d{4}/(0?[1-9]|[1][012])/(0?[1-9]|[12][0-9]|[3][01])$";

    /** 正则日期时间类型:中线 */
    public static final String REGEX_DATE_TIME_MIDDELLINE = "^\\d{4}-(0?[1-9]|[1][012])-(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /** 正则日期时间类型:反斜线 */
    public static final String REGEX_DATE_TIME_BACKSLASH = "^\\d{4}/(0?[1-9]|[1][012])/(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])$";

    /** 正则时间戳类型:中线 */
    public static final String REGEX_TIME_STAMP_MIDDELLINE = "^\\d{4}-(0?[1-9]|[1][012])-(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])\\.\\d{3}$";

    /** 正则时间戳类型:反斜线 */
    public static final String REGEX_TIME_STAMP_BACKSLASH = "^\\d{4}/(0?[1-9]|[1][012])/(0?[1-9]|[12][0-9]|[3][01])[\\s]+\\d([0-1][0-9]|2?[0-3]):([0-5][0-9]):([0-5][0-9])\\.\\d{3}$";

    /** 方法名:get */
    public static final String METHOD_GET = "get";

    /** 方法名:set */
    public static final String METHOD_SET = "set";

    /** 字符集编码:UTF-8 */
    public static final String CHATSET_UTF8 = "UTF-8";

    /** 字符集编码:ISO-8859-1 */
    public static final String CHATSET_ISO88591 = "ISO8859-1";

    /** 随机类型:0 数字 */
    public static final String RANDOM_TYPE_NUM = "0";
    /** 随机类型:1 英文 */
    public static final String RANDOM_TYPE_TEXT = "1";
    /** 随机类型:2 数字和英文混合 */
    public static final String RANDOM_TYPE_NUM_AND_TEXT = "2";

    /** 随机 */
    private static Random randGen = new SecureRandom();

    /** Gson */
    public static Gson gson = new Gson();

    /** 字符串随机因子:数字 */
    private static String numbers = "0123456789";

    /** 字符串随机因子: 英文 */
    private static String letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    /** 字符串随机因子:英数混合 */
    private static char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz" + "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();

    /**
     * 构造函数
     */
    private ConverterUtil() {
    }

    /**
     * Date->yyyy/MM/dd HH:mm型的String转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param dt
     *            日期
     * @return String数值
     * 
     */
    public static String toDateTimeString(Date dt) {
        if (dt == null) {
            return null;
        }

        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        return df.format(dt);
    }

    /**
     * Object->Timestamp型的String转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return String数值
     * 
     */
    public static String toTimestampString(Object obj) {
        if (obj == null) {
            return null;
        }
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSSSSS");

        if (obj instanceof Date) {
            return df.format(new Timestamp(((Date) obj).getTime()));
        }
        if (obj instanceof Timestamp) {
            return df.format((Timestamp) obj);
        }
        return obj.toString();
    }

    /**
     * Long->Date转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param date
     *            date
     * @return Date数值
     */
    public static Date toDate(Long date) {
        if (date == null) {
            return null;
        }
        Calendar cl = Calendar.getInstance();
        cl.setTimeInMillis(date);
        return cl.getTime();
    }

    /**
     * Object->Date转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return Date数值
     */
    public static Date toDate(Object obj, String formate) {
        if ((obj == null) || ("".equals(obj))) {
            return null;
        }
        if (obj instanceof Long) {
            return toDate(toLong(obj));
        }
        if (obj instanceof Timestamp) {
            return (Timestamp) obj;
        }
        if (obj instanceof Timestamp) {
            return (Timestamp) obj;
        }
        if (obj instanceof Date) {
            return new Timestamp(((Date) obj).getTime());
        }
        if (obj instanceof String) {
            String temp = obj.toString();
            if (temp.length() == 8) {
                char[] arra = temp.toCharArray();
                String temp1 = new String(arra, 0, 4);
                String temp2 = new String(arra, 4, 2);
                String temp3 = new String(arra, 6, 2);
                obj = temp1 + "/" + temp2 + "/" + temp3;
            }
        }
        DateFormat df = new SimpleDateFormat(formate);
        Date dateTemp = null;
        try {
            dateTemp = df.parse(obj.toString());
        } catch (ParseException e) {
            throw new RuntimeException("日期不合法", e);
        }
        return new Timestamp(dateTemp.getTime());

    }

    /**
     * Object->Timestamp转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * Object
     * 
     * @return Timestamp数值
     * 
     */
    public static Timestamp toTimestamp(Object obj) {
        Date date = toDate(obj, "yyyy/MM/dd HH:mm:ss.SSSSSSSSS");
        return date == null ? null : new Timestamp(date.getTime());
    }

    /**
     * Object->Integer转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return Integer数值
     */
    public static Integer toInteger(Object obj) {
        return toInteger(obj, null);
    }

    /**
     * Object->Integer转换
     * <p>
     * 入参是null时，返回值为nullValue
     * 
     * @param obj
     *            Object
     * @return Integer数值
     */
    public static Integer toInteger(Object obj, Integer nullValue) {
        if (isEmpty(obj)) {
            return nullValue;
        }
        if (obj instanceof Double) {
            return ((Double) obj).intValue();
        }
        return Integer.valueOf(obj.toString());
    }

    /**
     * Object->BigDecimal转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return BigDecimal数值
     */
    public static BigDecimal toBigDecimal(Object obj) {
        return toBigDecimal(obj, null);
    }

    /**
     * Object->BigDecimal转换
     * <p>
     * 入参是null时，返回值为nullValue
     * 
     * @param obj
     *            Object
     * @return BigDecimal数值
     */
    public static BigDecimal toBigDecimal(Object obj, BigDecimal nullValue) {
        if (isEmpty(obj)) {
            return nullValue;
        }
        return new BigDecimal(obj.toString());
    }

    /**
     * 提供精确的加法运算。
     * 
     * @param v1
     *            被加数
     * @param v2
     *            加数
     * @return 两个参数的和
     */

    public static double add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    /**
     * 提供精确的减法运算。
     * 
     * @param v1
     *            被减数
     * @param v2
     *            减数
     * @return 两个参数的差
     */

    public static double sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 提供精确的乘法运算。
     * 
     * @param v1
     * 
     * @param v2
     * 
     * @return 两个参数的乘积
     */
    public static double mul(double d1, double d2) {
        // 进行乘法运算
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 提供精确的除法运算。
     * 
     * @param v1
     *            被除数
     * @param v2
     *            除数
     * @return 两个参数的商
     */
    public static double div(double d1, double d2, int len) {
        // 进行除法运算
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, len, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * Object->String转换
     * <p>
     * 入参是null时，返回值为""
     * 
     * @param obj
     *            Object
     * @return String
     * 
     */
    public static String toNotNullString(Object obj) {
        return obj == null ? "" : toString(obj);
    }

    /**
     * Object->String转换
     * <p>
     * 入参是null时，返回值为nullValue
     * 
     * @param obj
     *            Object
     * @return String
     * 
     */
    public static String toNotNullString(Object obj, String nullValue) {
        return obj == null ? nullValue : toString(obj);
    }

    /**
     * Object->String转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return String
     * 
     */
    public static String toString(Object obj) {
        if (null == obj || "".equals(obj)) {
            return null;
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        if (obj instanceof Integer) {
            return Integer.toString(toInteger(obj));
        }
        if (obj instanceof Double) {
            return Double.toString(toDouble(obj));
        }
        if (obj instanceof Long) {
            return Long.toString(toLong(obj));
        }
        if (obj instanceof BigDecimal) {
            return toBigDecimal(obj).toPlainString();
        }
        if (obj instanceof Timestamp) {
            return dateToString((Date) obj, FORMATE_TIME_STAMP_24H);
        }
        if (obj instanceof Date) {
            return dateToString((Date) obj, FORMATE_DATE_TIME_24H);
        }
        if (obj instanceof Object[]) {
            Object[] objArray = (Object[]) obj;
            return String.valueOf(objArray[0]);
        }
        return String.valueOf(obj);
    }

    /**
     * Object->String转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return String
     * 
     */
    public static String toString(Object obj, String nullValue) {
        if (isEmpty(obj)) {
            return nullValue;
        }
        if (obj instanceof String) {
            return obj.toString();
        }
        if (obj instanceof Integer) {
            return Integer.toString(toInteger(obj));
        }
        if (obj instanceof Double) {
            return Double.toString(toDouble(obj));
        }
        if (obj instanceof Long) {
            return Long.toString(toLong(obj));
        }
        if (obj instanceof BigDecimal) {
            return toBigDecimal(obj).toPlainString();
        }
        if (obj instanceof Timestamp) {
            return dateToString((Date) obj, FORMATE_TIME_STAMP_24H);
        }
        if (obj instanceof Date) {
            return dateToString((Date) obj, FORMATE_DATE_TIME_24H);
        }
        if (obj instanceof Object[]) {
            Object[] objArray = (Object[]) obj;
            return String.valueOf(objArray[0]);
        }
        return String.valueOf(obj);
    }

    /**
     * Object->Boolean转换
     * <p>
     * flase<String> --> false<boolean> TRUE<String> --> true<boolean> 0<String> --> false<boolean> 1<String> --> true<boolean>
     * 
     * @param Obj
     * @return
     */
    public static Boolean toBoolean(Object Obj) {
        if (null != Obj && Obj instanceof Boolean) {
            return ((Boolean) Obj).booleanValue();
        }
        String boo = toNotNullString(Obj);
        if ("true".equalsIgnoreCase(boo)) {
            return true;
        }
        if ("false".equalsIgnoreCase(boo)) {
            return false;
        }
        if ("0".equalsIgnoreCase(boo)) {
            return false;
        }
        if ("1".equalsIgnoreCase(boo)) {
            return true;
        }
        return false;
    }

    // /**
    // * Timestamp转字符串
    // *
    // * @param ts
    // * Timestamp
    // * @param format
    // * 转换格式
    // * @return 字符串
    // */
    // public static String timestampToString(Timestamp ts, String format) {
    // SimpleDateFormat sf = new SimpleDateFormat(format);
    // Calendar cl = Calendar.getInstance();
    // cl.setTimeInMillis(ts.getTime());
    // return sf.format(cl.getTime());
    // }

    /**
     * 日期->字符串转换
     * 
     * @param date
     *            日期
     * @param format
     *            转换格式
     * @return 字符串
     */
    public static String dateToString(Date date, String format) {
        SimpleDateFormat sf = new SimpleDateFormat(format);
        return sf.format(date);
    }

    /**
     * 字符串->日期转换
     * <p>
     * 支持yyyy/MM/dd|yyyy/MM/dd HH:mm.ss|yyyy/MM/dd HH:mm.SSS| yyyy-MM-dd|yyyy-MM-dd HH:mm.ss|yyyy-MM-dd HH:mm.SSS 的转换。如果不在这六种格式之内，返回null。
     * 
     * 日期
     * 
     * @return 字符串
     * @throws ParseException
     */
    public static Date toDate(String str) throws ParseException {
        // 设置日期正则格式
        Map<String, String> regexMap = new HashMap<String, String>();
        regexMap.put("REGEX_DATE_MIDDELLINE", REGEX_DATE_MIDDELLINE);
        regexMap.put("REGEX_DATE_BACKSLASH", REGEX_DATE_BACKSLASH);
        regexMap.put("REGEX_DATE_TIME_MIDDELLINE", REGEX_DATE_TIME_MIDDELLINE);
        regexMap.put("REGEX_DATE_TIME_BACKSLASH", REGEX_DATE_TIME_BACKSLASH);
        regexMap.put("REGEX_TIME_STAMP_MIDDELLINE", REGEX_TIME_STAMP_MIDDELLINE);
        regexMap.put("REGEX_TIME_STAMP_BACKSLASH", REGEX_TIME_STAMP_BACKSLASH);

        SimpleDateFormat sf = null;
        for (String regex : regexMap.keySet()) {
            Pattern pat = Pattern.compile(regexMap.get(regex));
            Matcher mat = pat.matcher(str);
            if (mat.find()) {
                if (regex.equals("REGEX_DATE_MIDDELLINE")) {
                    sf = new SimpleDateFormat("yyyy-MM-dd");
                } else if (regex.equals("REGEX_DATE_BACKSLASH")) {
                    sf = new SimpleDateFormat(FORMATE_DATE);
                } else if (regex.equals("REGEX_DATE_TIME_MIDDELLINE")) {
                    sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                } else if (regex.equals("REGEX_DATE_TIME_BACKSLASH")) {
                    sf = new SimpleDateFormat(FORMATE_DATE_TIME_24H);
                } else if (regex.equals("REGEX_TIME_STAMP_MIDDELLINE")) {
                    sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                } else if (regex.equals("REGEX_TIME_STAMP_BACKSLASH")) {
                    sf = new SimpleDateFormat(FORMATE_TIME_STAMP_24H);
                }
                break;
            }
        }
        if (null == sf) {
            return null;
        }
        return sf.parse(str);
    }

    /**
     * 
     * Object->Double转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return Double型数值
     * 
     */
    public static Double toDouble(Object obj) {
        return toDouble(obj, null);
    }

    /**
     * 
     * Object->Double转换
     * <p>
     * 入参是null时，返回值为nullValue
     * 
     * @param obj
     *            Object
     * @return Double型数值
     * 
     */
    public static Double toDouble(Object obj, Double nullValue) {
        if (isEmpty(obj)) {
            return nullValue;
        }
        return new Double(obj.toString());
    }

    /**
     * 
     * Object->Float转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return Float型数值
     * 
     */
    public static Float toFloat(Object obj) {
        return toFloat(obj, null);
    }

    /**
     * 
     * Object->Float转换
     * <p>
     * 入参是null时，返回值为nullValue
     * 
     * @param obj
     *            Object
     * @return Float型数值
     * 
     */
    public static Float toFloat(Object obj, Float nullValue) {
        if ((obj == null) || (obj.equals(""))) {
            return nullValue;
        }
        return new Float(toString(obj));
    }

    /**
     * 
     * Object->Long转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param obj
     *            Object
     * @return Long型数值
     * 
     */
    public static Long toLong(Object obj) {
        return toLong(obj, null);
    }

    /**
     * 
     * Object->Long转换
     * <p>
     * 入参是null时，返回值为nullValue
     * 
     * @param obj
     *            Object
     * @return Long型数值
     * 
     */
    public static Long toLong(Object obj, Long nullValue) {
        if (isEmpty(obj)) {
            return nullValue;
        }
        if (obj instanceof Long) {
            return Long.valueOf(obj.toString());
        }
        if (obj instanceof Double) {
            return toDouble(obj).longValue();
        }
        if (obj instanceof BigDecimal) {
            return toBigDecimal(obj).longValue();
        }
        if (obj instanceof Date) {
            return toDate(obj, null).getTime();
        }
        return new Long(obj.toString());
    }

    /**
     * 
     * double型数值小数部分舍除
     * <p>
     * 四舍五入
     * 
     * @param value
     *            double型数值
     * @param scale
     *            小数点以后位数
     * @return double型数值
     */
    public static Double getRoundValue(Double value, int scale) {
        double result = 0.0;
        if (null != value) {
            result = new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.HALF_UP).doubleValue();
        }
        return result;
    }

    /**
     * double型数值小数部分舍除
     * <p>
     * 进位
     * 
     * @param value
     *            double型数值
     * @param scale
     *            小数点以后位数
     * @return double型数值
     */
    public static Double getRoundUpValue(Double value, int scale) {
        double result = 0.0;
        if (null != value) {
            result = new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.UP).doubleValue();
        }
        return result;
    }

    /**
     * double型数值小数部分舍除
     * <p>
     * 直接舍掉
     * <p>
     * 
     * @param value
     *            double型数值
     * @param scale
     *            小数点以后位数
     * @return double型数值
     */
    public static Double getRoundDownValue(Double value, int scale) {
        double result = 0.0;
        if (null != value) {
            result = new BigDecimal(String.valueOf(value)).setScale(scale, RoundingMode.DOWN).doubleValue();
        }
        return result;
    }

    /**
     * double型数值小数部分舍除
     * <p>
     * round: 0:直接舍掉 1：四舍五入 2：进位
     * 
     * @param dbValue
     *            double型数值
     * @param round
     *            舍值方式
     * @param scale
     *            小数点以后位数
     * @return double型数值
     */
    public static double getDoubleValueWithScale(double dbValue, int round, int scale) {
        // 0:直接舍掉 1：四舍五入 2：进位
        if (round == 0) {
            dbValue = getRoundDownValue(dbValue, scale);
        } else if (round == 1) {
            dbValue = getRoundValue(dbValue, scale);
        } else if (round == 2) {
            dbValue = getRoundUpValue(dbValue, scale);
        }
        return dbValue;
    }

    /**
     * 字符串按照固定长度填充
     * <p>
     * 文字型：后补空格 数字型：前补0
     * 
     * @param obj
     *            字符串
     * @param objType
     *            变换类型
     * @param objLen
     *            长度
     * @return 变换后的字符串
     */
    public static String convertBySize(Object obj, String objType, int objLen) {
        String ret = null;
        int oldSize = 0;
        int size = objLen;

        if (obj == null || size == 0) {
            ret = "";
        } else {
            ret = obj.toString();
            oldSize = obj.toString().getBytes().length;
        }
        if (DATA_TYPE_NUMBER.equals(objType)) {
            // 无符号
            if (oldSize < size) {
                int m = size - oldSize;
                for (int i = 0; i < m; i++) {
                    // 在最前面增加0
                    ret = "0" + ret;
                }
            } else {
                ret = ret.substring(oldSize - size);
            }
        } else if (DATA_TYPE_CHAR.equals(objType)) {
            // 文字型
            if (oldSize < size) {
                int m = size - oldSize;
                for (int i = 0; i < m; i++) {
                    // 最后半角空格增加
                    ret = ret + " ";
                }
            } else if (oldSize > size) {
                byte[] bytes = ret.getBytes();
                byte[] retBytes = new byte[size];
                for (int i = 0; i < size; i++) {
                    retBytes[i] = bytes[i];
                }
                ret = new String(retBytes);
                if (ret.getBytes().length != size) {
                    ret = ret + " ";
                }
            }
        }
        return ret;
    }

    /**
     * 有符号数字转->Integer转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param signNumber
     *            有符号的字符串
     * @return Integer型数值
     */
    public static Integer convSignNumStrToInteger(String signNumber) {
        if (signNumber == null || "".equals(signNumber)) {
            return null;
        }
        try {
            if (signNumber.substring(0, 1).equals("+")) {
                return new Integer(signNumber.substring(1));
            } else if (signNumber.substring(0, 1).equals("-")) {
                return new Integer(Integer.parseInt(signNumber.substring(1)) * -1);
            } else {
                return new Integer(signNumber);
            }
        } catch (NumberFormatException e) {
            return null;
        }

    }

    /**
     * 有符号数字转->BigDecimal转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param signNumber
     *            有符号的字符串
     * @return BigDecimal型数值
     */
    public static BigDecimal convSignNumStrToBigDecimal(String signNumber) {
        if (signNumber == null || "".equals(signNumber)) {
            return null;
        }
        try {
            if (signNumber.substring(0, 1).equals("+")) {
                return new BigDecimal(signNumber.substring(1));
            } else if (signNumber.substring(0, 1).equals("-")) {
                return new BigDecimal(signNumber.substring(1)).negate();
            } else {
                return new BigDecimal(signNumber);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * BigDecimal->Integer转换(小数部分舍掉)
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param bdNum
     *            BigDecimal型数值
     * @return Integer数值
     */
    public static Integer convBDNumToInteger(BigDecimal bdNum) {
        if (bdNum == null) {
            return null;
        }
        return new Integer(bdNum.setScale(0, RoundingMode.DOWN).intValue());
    }

    /**
     * 16进制String->Integer转换
     * <p>
     * 入参是null时，返回值为null
     * 
     * @param str
     *            String文字列
     * @return Integer数值
     */
    public static Integer convStrToIntegerHex(String str) {
        if (isEmpty(str)) {
            return null;
        }
        try {
            return new Integer(Integer.parseInt(str, 16));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * properties数组型数据按逗号分隔
     * 
     * @param properties
     *            字符串
     * 
     * @return List
     */
    public static List<String> getSplitProperties(String properties) {
        List<String> arrayList = new ArrayList<String>();
        if (isNotEmpty(properties)) {
            String propertieLine = properties.replace("[", "").replace("]", "");
            for (String property : propertieLine.split(",")) {
                arrayList.add(property);
            }
        }
        return arrayList;
    }

    /**
     * 字符串按特定符号进行分割，返回分割后不为空的字符的List
     * 
     * @param line
     *            字符串
     * @param mark
     *            分隔符
     * @return List
     */
    public static List<String> getSplitList(String line, String mark) {
        List<String> arrayList = new ArrayList<String>();
        if (isNotEmpty(line)) {
            for (String word : line.split(mark)) {
                if (isNotEmpty(word)) {
                    arrayList.add(word);
                }
            }
        }
        return arrayList;
    }

    /**
     * 字符串按特定符号进行分割，返回分割后不为空的字符的数组
     * 
     * @param line
     *            字符串
     * @param mark
     *            分隔符
     * @return List
     */
    public static String[] getSplitArray(String line, String mark) {
        List<String> arrayList = new ArrayList<String>();
        if (isNotEmpty(line)) {
            for (String word : line.split(mark)) {
                if (isNotEmpty(word)) {
                    arrayList.add(word);
                }
            }
        }
        String[] temp = new String[arrayList.size()];
        for (int i = 0; i < arrayList.size(); i++) {
            temp[i] = arrayList.get(i);
        }
        return temp;
    }

    /**
     * 根据自动补全List
     * 
     * @param list
     *            List
     * @param length
     *            长度
     * @param obj
     *            补全的Object
     * @return List
     */
    public static <T> List<T> complementSize(List<T> list, int length, T obj) {
        if (list.size() <= 0) {
            return list;
        }
        // 补全类别是same时，等于list的第一个
        if (COMPLEMENT_SAME.equals(obj)) {
            obj = list.get(0);
        }
        // 如果长度小于，则增加；大于则从右侧删除多余的项
        if (list.size() < length) {
            for (int i = 0; i <= length - list.size(); i++) {
                list.add(obj);
            }
        } else if (list.size() > length) {
            list.subList(0, (list.size() - length - 1));
        }
        return list;
    }

    /**
     * 系统路径->相对路径转换
     * <p>
     * 【\】->【/】
     * 
     * @param sysPath
     *            系统路径
     * @return 相对路径
     */
    public static String sysPathToPath(String sysPath) {
        sysPath = sysPath.replace("\\", "/");
        return sysPath;
    }

    /**
     * 相对路径->系统路径转换
     * <p>
     * 【/】->【\】
     * 
     * @param path
     *            相对路径
     * @return 系统路径
     */
    public static String pathToSysPath(String path) {
        path = path.replace("/", "\\");
        return path;
    }

    /**
     * 取得系统发布后的完整路径
     * <p>
     * 系统发布后名字有可能会发生空白或者填写的情况，该方法能后将发布路径和URL进行拼接
     * 
     * @param contextPath
     *            发布路径
     * @param url
     *            链接
     * @return 当前URl所属的完整链接
     */
    public static String getActionPath(String contextPath, String url) {
        if (!contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        if (url.startsWith("/")) {
            if ("/".equals(contextPath)) {
                return url;
            }
            // 没有指定工程名的情况
            return contextPath + url;
        } else {
            if ("/".equals(contextPath)) {
                return "/" + url;
            }
            // 指定了工程名的情况
            return contextPath + "/" + url;
        }
    }

    /**
     * 判断是否是空字符串 null和"" 都返回 true
     * 
     * @author Robin Chang
     * @param
     * @return
     */
    public static boolean isEmpty(Object str) {
        boolean flag = true;
        if (str != null && !str.equals("")) {
            if (str instanceof Object[]) {
                return ((Object[]) str).length <= 0;
            }
            if (str instanceof Collection<?>) {
                return ((Collection<?>) str).size() <= 0;
            }
            if (str instanceof Map) {
                return ((Map<?, ?>) str).size() <= 0;
            }
            String newStr = str.toString();
            if (newStr == null || newStr == "" || "[]".equals(newStr) || "null".equals(newStr)) {
                flag = true;
            } else if (newStr.length() > 0) {
                flag = false;
            }
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 判断对象是否不为空
     * 
     * @param str
     * @return
     */
    public static boolean isNotEmpty(Object str) {
        return !isEmpty(str);
    }
    
    /**
     * 判断对象数组是否都为空
     * 
     * @return
     */
    public static boolean isEmpty(Object... strs) {
        if (strs == null || 0 == strs.length) {
            return true;
        }
        boolean result = true;
        for (Object obj : strs) {
            result = (result && isEmpty(obj));
            if (!result) {
                return result;
            }
        }
        return result;
    }

    /**
     * 判断对象数组是否有一个为空
     * 
     * @return
     */
    public static boolean isOrEmpty(Object... strs) {
        boolean result = false;
        for (Object obj : strs) {
            result = (result || isEmpty(obj));
            if (result) {
                return result;
            }
        }
        return result;
    }

    /**
     * 判断对象数组是否都不为空
     * 
     * @return
     */
    public static boolean isNotEmpty(Object... strs) {
        if (strs == null || 0 == strs.length) {
            return false;
        }
        boolean result = true;
        for (Object obj : strs) {
            result = (result && isNotEmpty(obj));
            if (!result) {
                return result;
            }
        }
        return result;
    }

    /**
     * 是否是数字
     * 
     * @param strs
     * @return
     */
    @CheckApi
    public static boolean isNumeric(Object... strs) {
        boolean result = true;
        for (Object obj : strs) {
            result = (result && StringUtils.isNumeric(toString(obj, "")));
        }
        return result;
    }

    /**
     * 
     * Map转换String
     * 
     * @param map
     *            需要转换的Map
     * @return String转换后的字符串
     */
    public static String mapToString(Map<String, Object> map) {
        return mapToString(map, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
    }

    /**
     * 
     * Map转换String
     * 
     * @param map
     *            需要转换的Map
     * @return String转换后的字符串
     */
    public static String mapToString(Map<String, Object> map, String elementSeparator, String keySeparator) {
        StringBuffer stb = new StringBuffer();
        // 遍历map
        for (String key : map.keySet()) {
            if (isEmpty(key)) {
                continue;
            }
            Object value = map.get(key);
            stb.append(key + keySeparator + toNotNullString(value));
            stb.append(elementSeparator);
        }
        return stb.toString();
    }

    /**
     * 
     * String转换Map
     * 
     * @param mapText
     *            需要转换的字符串
     * @return Map
     */
    public static Map<String, Object> stringToMap(String mapText) {
        return stringToMap(mapText, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
    }

    /**
     * 
     * String转换Map
     * 
     * @param mapText
     *            需要转换的字符串
     * @param elementSeparator
     *            字符串中每个元素的分割
     * @param keySeparator
     *            字符串中的分隔符每一个key与value中的分割
     * @return Map
     */
    public static Map<String, Object> stringToMap(String mapText, String elementSeparator, String keySeparator) {
        if (isEmpty(mapText)) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 转换为数组
        String[] text = mapText.split(elementSeparator);
        for (String str : text) {
            if (isEmpty(str)) {
                continue;
            }
            // 转换key与value的数组
            String[] keyText = str.split(keySeparator);
            if (keyText.length < 2) {
                continue;
            }
            String key = keyText[0];
            String value = keyText[1];
            map.put(key, value);
        }
        return map;

    }

    /**
     * 
     * String转换Map(key与value相同)
     * 
     * @param mapText
     *            需要转换的字符串
     * @param elementSeparator
     *            字符串中每个元素的分割
     * @return Map
     */
    public static Map<String, Object> stringToMap(String mapText, String elementSeparator) {
        if (isEmpty(mapText)) {
            return new HashMap<String, Object>();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 转换为数组
        String[] text = mapText.split(elementSeparator);
        for (String str : text) {
            if (isEmpty(str)) {
                continue;
            }
            // key与value相同
            String key = str;
            String value = str;
            map.put(key, value);
        }
        return map;

    }

    /**
     * String转换List
     * 
     * @param listText
     *            需要转换的文本
     * 
     * @return List<String>
     */

    public static List<String> stringToList(String listText) {
        return stringToList(listText, SEPARATOR_ELEMENT);
    }

    /**
     * String转换List
     * 
     * @param listText
     *            需要转换的文本
     * @param separator
     *            分隔符
     * @return List<String>
     */
    public static List<String> stringToList(String listText, String separator) {
        if (isEmpty(listText)) {
            return new ArrayList<String>();
        }
        List<String> list = new ArrayList<String>();
        String[] text = listText.split(separator);
        for (String str : text) {
            if (isEmpty(str)) {
                continue;
            }
            list.add(str);
        }
        return list;
    }

    /**
     * List转换String
     * 
     * @param list
     *            需要转换的List
     * @param separator
     *            分隔符
     * @return String
     */
    public static String listToString(List<?> list, String separator) {
        StringBuffer stb = new StringBuffer();
        if (list != null && list.size() > 0) {
            for (Object obj : list) {
                String str = toNotNullString(obj);
                if (isEmpty(str)) {
                    continue;
                }
                stb.append(str);
                stb.append(separator);
            }
        }
        return stb.toString();
    }

    /**
     * String转换链接参数
     * 
     * @param str
     *            字符串 字符串中每个元素的分割 字符串中的分隔符每一个key与value中的分割
     * @return
     */
    public static String stringToLinkparams(String str) {
        return stringToLinkparams(str, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
    }

    /**
     * String转换链接参数
     * 
     * @param str
     *            字符串
     * @param elementSeparator
     *            字符串中每个元素的分割
     * @param keySeparator
     *            字符串中的分隔符每一个key与value中的分割
     * @return
     */
    public static String stringToLinkparams(String str, String elementSeparator, String keySeparator) {
        if (isEmpty(str)) {
            return null;
        }
        StringBuffer stb = new StringBuffer();
        // 转换为数组
        String[] elements = str.split(elementSeparator);
        for (String element : elements) {
            if (isEmpty(element)) {
                continue;
            }
            // 转换key与value的数组
            String[] keyText = element.split(keySeparator);
            if (keyText.length < 2) {
                continue;
            }
            stb.append("&" + keyText[0]);
            stb.append("=" + keyText[1]);
        }
        return stb.toString();
    }

    /**
     * 字符串中特殊字符替换成JSON格式
     * 
     * @param str
     *            字符串
     * @return 转换后的字符串
     */
    public static String string2Json(String str) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            switch (c) {
            case '\"':
                sb.append("\\\"");
                break;
            case '\\':
                sb.append("\\\\");
                break;
            case '/':
                sb.append("\\/");
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\r':
                sb.append("\\r");
                break;
            case '\t':
                sb.append("\\t");
                break;
            default:
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 将Entity的属性和值转成Map格式
     * 
     * @param entity
     *            实体类
     * @return
     * @throws SecurityException
     *             安全异常
     * @throws NoSuchMethodException
     *             没有找到get方法异常
     * @throws IllegalArgumentException
     *             反射方法参数异常
     * @throws IllegalAccessException
     *             反射实体类异常
     * @throws InvocationTargetException
     *             反射get方法异常
     * @throws IntrospectionException
     */
    public static Map<String, Object> entityToMap(Object entity)
            throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IntrospectionException {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Field[] fileds = getAllFields(entity.getClass());
        for (Field field : fileds) {
            field.setAccessible(true);
            // 使用PropertyDescriptor有缺陷，如果是SerialVersionUID会报异常
            retMap.put(field.getName(), field.get(entity));
            // PropertyDescriptor pd = new PropertyDescriptor(field.getName(),entity.getClass());
            // if(isNotEmpty(pd)){
            // Method getMethod = pd.getReadMethod();//获得get方法
            // retMap.put(field.getName(), getMethod.invoke(entity));
            // }
        }
        return retMap;
    }

    /**
     * 取得去重SQL
     * 
     * @param tableName
     *            表名
     * @param distinctFieldList
     *            去重的list
     * @return 去重sql
     */
    public static String getDistinctSql(String tableName, List<String> distinctFieldList) {
        String sql = "DELETE FROM " + tableName + " WHERE ROWID NOT IN (SELECT MAX(ROWID) FROM " + tableName + " group by ";
        for (String field : distinctFieldList) {
            sql += (field + ",");
        }
        sql = sql.substring(0, sql.length() - 1);
        sql += ")";
        return sql;
    }

    /**
     * 获取当前时间
     * 
     * @param format
     * @return
     */
    public static String getCurrentTime(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        Date currTime = new Date();

        String curTime = formatter.format(currTime);
        return curTime;
    }

    /**
     * 获取年度列表
     * 
     * @param num
     *            跨度（前后）
     * @return
     */
    public static List<String> getYearList(int num) {
        List<String> yearList = new ArrayList<String>();
        Integer year = Integer.parseInt(getCurrentTime("yyyy"));
        int begain = year - num + 1;
        // int end = year + num;
        for (int i = begain; i <= year; i++) {
            yearList.add(String.valueOf(i));
        }
        return yearList;

    }

    /**
     * 
     * String转换Options
     * 
     * @param str
     *            需要转换的字符串
     * @param defaultVal
     *            默认值 字符串中每个元素的分割 字符串中的分隔符每一个key与value中的分割
     * @return Map
     */
    public static String formatOptions(String str, String defaultVal) {
        return formatOptions(str, defaultVal, SEPARATOR_ELEMENT, SEPARATOR_KEY_VALUE);
    }

    /**
     * 
     * String转换Options
     * 
     * @param str
     *            需要转换的字符串
     * @param defaultVal
     *            默认值
     * @param elementSeparator
     *            字符串中每个元素的分割
     * @param keySeparator
     *            字符串中的分隔符每一个key与value中的分割
     * @return Map
     */
    public static String formatOptions(String str, String defaultVal, String elementSeparator, String keySeparator) {
        StringBuffer sf = new StringBuffer();
        String[] elems = str.split(elementSeparator);
        // 拼接下拉框
        if (isEmpty(defaultVal) || "undefined".equals(defaultVal)) {
            sf.append("<option value=\"\" selected=\"selected\">");
        }
        for (String elem : elems) {
            String[] keyValue = elem.split(keySeparator);
            if (keyValue.length < 2) {
                continue;
            }
            String key = keyValue[0];
            String value = keyValue[1];
            if (isNotEmpty(key) && key.equals(defaultVal)) {
                sf.append("<option value=\"" + key + "\" selected=\"selected\">");
            } else {
                sf.append("<option value=\"" + key + "\">");
            }
            if (isNotEmpty(value)) {
                sf.append(value);
                sf.append("</option>");
            }

        }
        return sf.toString();
    }

    /**
     * 取得UUID
     * 
     * @return UUID
     */
    public static String getUUID() {
        return toString(UUID.randomUUID()).replace("-", "");
    }

    /**
     * 取得JAVA属性反射方法名
     * 
     * @param attr
     *            属性
     * @param method
     *            get/set/other/""
     * @return 方法名
     */
    public static String converToMethodName(String attr, String method) {
        String methodName = "";
        if (isEmpty(attr)) {
            return methodName;
        }
        if (attr.length() >= 2) {
            char first = attr.charAt(0);
            char second = attr.charAt(1);
            // mm-> Mm
            if (Character.isLowerCase(first) && Character.isLowerCase(second)) {
                return method + Character.toUpperCase(first) + attr.substring(1);
            } else {
                // MM -> MM
                // mM -> mM
                // Mm -> Mm
                return method + attr;
            }
        } else {
            return method + Character.toUpperCase(attr.charAt(0)) + attr.substring(1);
        }
    }

    /**
     * Map转实体类
     * <p/>
     * Map->Entity
     * 
     * 
     * @param map
     *            Map
     * @param clazz
     *            实体类Class
     * @return 实体类
     * @throws NoSuchMethodException
     *             没有找到set方法异常
     * @throws SecurityException
     *             安全异常
     * @throws IllegalAccessException
     *             反射实体类异常
     * @throws InvocationTargetException
     *             反射set方法异常
     * @throws IllegalArgumentException
     *             反射方法参数异常
     */
    @SuppressWarnings("unchecked")
    public static <T> T mapToEntity(Map<String, Object> map, Class<?> clazz)
            throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        T entity = (T) clazz.newInstance();
        Field[] fields = getAllFields(clazz);
        // 循环查询出的列
        for (Field field : fields) {
            String fName = field.getName();
            if (map.containsKey(fName)) {
                field.setAccessible(true);
                field.set(entity, map.get(fName));
            } else {
                // JDBC方式查询出的别名都是大写
                fName = fName.toUpperCase();
                if (map.containsKey(fName)) {
                    field.setAccessible(true);
                    field.set(entity, map.get(fName));
                }
            }
        }

        return entity;
    }

    /**
     * Map转实体类
     * <p/>
     * Map->Entity
     * 
     * 
     * Map 实体类Class
     * 
     * @return 实体类
     * @throws NoSuchMethodException
     *             没有找到set方法异常
     * @throws SecurityException
     *             安全异常
     * @throws IllegalAccessException
     *             反射实体类异常
     * @throws InvocationTargetException
     *             反射set方法异常
     * @throws IllegalArgumentException
     *             反射方法参数异常
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> listMapToListEntity(List<Map<String, Object>> list, Class<?> clazz)
            throws InstantiationException, IllegalAccessException, SecurityException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
        List<T> resultList = new ArrayList<T>();
        for (Map<String, Object> map : list) {
            resultList.add((T) mapToEntity(map, clazz));
        }
        return resultList;
    }

    /**
     * 字符串转码
     * 
     * @param str
     *            字符串
     * @param getChatset
     *            取得编码
     * @param toChatset
     *            转成编码
     * @return 字符串
     * @throws UnsupportedEncodingException
     *             转换异常
     */
    public static String encodeStr(String str, String getChatset, String toChatset) throws UnsupportedEncodingException {
        return new String(str.getBytes(getChatset), toChatset);
    }

    /**
     * 字符串转码
     * 
     * @param str
     *            字符串
     * @return 字符串
     * @throws UnsupportedEncodingException
     *             转换异常
     */
    public static String encodeStr(String str) throws UnsupportedEncodingException {
        return encodeStr(toNotNullString(str), CHATSET_ISO88591, CHATSET_UTF8);
    }

    /**
     * 取得N位随机验证码
     * <p/>
     * 1-配置长度，随机内容
     * 
     * @return
     */
    public static String getCheckCode(Integer length) {
        return getCheckCode(RANDOM_TYPE_NUM, length);
    }

    /**
     * 取得N位随机验证码
     * <p/>
     * 1-配置长度，随机内容
     * 
     * @return
     */
    public static String getCheckCode(String type, Integer length) {
        // 默认6位
        int nMax = 6;
        if (null != length) {
            nMax = length;
        }

        // 随机串数字
        String str = numbers;
        if (RANDOM_TYPE_TEXT.equals(type)) {
            // 英文
            str = letters;
        } else if (RANDOM_TYPE_NUM_AND_TEXT.equals(type)) {
            // 英数混合
            return randomString(nMax);
        }

        Random contRandom = new Random();
        StringBuffer sb = new StringBuffer();
        // 产生随机数
        for (int i = 0; i < nMax; i++) {
            int number = contRandom.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 按顺序转换问号
     * 
     * @param str
     *            字符串
     * @param params
     *            参数
     */
    public static String replaceMark(String str, String... params) {
        if (ConverterUtil.isEmpty(str)) {
            return "";
        }
        if (params != null) {
            for (Object obj : params) {
                str = str.replaceFirst("\\?", obj.toString());
            }
        }
        return str;
    }

    /**
     * 获取字符串的长度，如果有中文，则每个中文字符计为2位
     *
     * @param validateStr
     *            指定的字符串
     * @return 字符串的长度
     */
    public static int getMixLength(String validateStr) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
        for (int i = 0; i < validateStr.length(); i++) {
            /* 获取一个字符 */
            String temp = validateStr.substring(i, i + 1);
            /* 判断是否为中文字符 */
            if (temp.matches(chinese)) {
                /* 中文字符长度为2 */
                valueLength += 2;
            } else {
                /* 其他字符长度为1 */
                valueLength += 1;
            }
        }
        return valueLength;
    }

    /**
     * 取得系统路径
     * <p>
     * E:\myWorkSpace123\.metadata\.plugins\org.eclipse.wst.server.core\tmp0\wtpwebapps\ZX_Web\
     * 
     * @return
     */
    public static String getSysPath() {
        String path = Thread.currentThread().getContextClassLoader().getResource("").toString();
        String temp = path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
        String separator = System.getProperty("file.separator");
        String resultPath = temp.replaceAll("/", separator + separator).replaceAll("%20", " ");
        return resultPath;
    }

    /**
     * 转成下载专用的文件名
     * <p>
     * 下载的文件如果经过URLEncoder的转码，其中有些特殊符号就也会被跟着转成中文，但是这些符号不会被正确解码，所以要进行二次转换
     * 
     * @param str
     * @return
     */
    public static String toDownloadName(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("%20", "\\+").replaceAll("%28", "\\(").replaceAll("%29", "\\)").replaceAll("%3B", ";").replaceAll("%40", "@").replaceAll("%23", "\\#").replaceAll("%26", "\\&");
    }

    /**
     * requestMap<?,?> -> Map<String, Object>
     * <p>
     * 不支持多个参数用一个key来取得,如果同一个key取得了一个String[]，<br/>
     * 转换后的值为String[]中最后一个value的值
     * <p>
     * 
     * request参数Map
     * 
     * @return
     */
    public static Map<String, Object> requestParamsToMap(Map<?, ?> requestMap) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        for (Object key : requestMap.keySet()) {
            Object value = requestMap.get(key);
            if (null == value || "".equals(value)) {
                continue;
            } else if (value instanceof String[]) {
                for (String val : (String[]) value) {
                    if (isNotEmpty(val)) {
                        resultMap.put(toString(key), val);
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * 取得entity中属性和类型的map Key：value->属性名：属性类型
     * 
     * @param entity
     *            实体类
     * @return map
     */
    public static Map<String, Object> getFieldTypeMap(Object entity) {
        Map<String, Object> retMap = new HashMap<String, Object>();
        Field[] fileds = getAllFields(entity.getClass());
        for (Field field : fileds) {
            retMap.put(field.getName(), field.getType());
        }
        return retMap;
    }

    /**
     * 将Instant时间转成时间字符串
     * 
     * @param time
     * @return
     */
    public static Date instantToDate(Instant time) {
        return new Date(time.toEpochMilli());
    }

    /**
     * 取得当前时间戳
     * 
     * @param formate
     * @return
     */
    public static String getNow(String formate) {
        return ConverterUtil.dateToString(new Date(), formate);
    }

    /**
     * 取得当前时间戳
     * 
     * @param formate
     * @return
     */
    public static String getNow() {
        return ConverterUtil.dateToString(new Date(), FORMATE_DATE_TIME_24H_MLINE);
    }

    /**
     * 获取某类中所有的Field属性
     * 
     * @param clazz
     *            类
     * @return
     */
    public static Field[] getAllFields(Class<?> clazz) {
        Field[] fileds = {};
        fileds = ArrayUtils.addAll(clazz.getDeclaredFields());
        Class<?> parent = clazz.getSuperclass();
        if (null != parent) {
            fileds = ArrayUtils.addAll(fileds, getAllFields(parent));
        }
        return fileds;
    }

    /**
     * 获取某类中所有的Field属性
     * 
     * @param clazz
     *            类
     * @return
     */
    public static List<Field> getAllFieldsByAnnotation(Class<?> clazz, Class<? extends Annotation> annotationClazz) {
        Field[] fileds = getAllFields(clazz);
        List<Field> filedList = Lists.newArrayList();
        for (Field field : fileds) {
            if (field.isAnnotationPresent(annotationClazz)) {
                filedList.add(field);
            }
        }
        return filedList;
    }

    /**
     * 获取某类中所有的Field属性
     * 
     * @param clazz
     *            类
     * @return
     */
    public static Map<String, Object> getAllFieldsMap(Object entity) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            for (Field field : getAllFields(entity.getClass())) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(entity));
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 获取补零字符串
     * 
     * @param num
     *            要补零的数字
     * @param length
     *            补零后的长度
     * @return
     */
    public static String getZeroFill(int num, int length) {
        String str = String.format("%0" + length + "d", num);
        return str;
    }

    /**
     * 日期操作(加减)
     * 
     * @param date
     *            日期
     * @param calendarType
     *            日历类型
     * @param num
     *            变化值
     * @return
     */
    public static Date addDate(Date date, int calendarType, int num) {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);
        gc.add(calendarType, num);
        return gc.getTime();
    }

    /**
     * 日期加天
     * 
     * @param date
     *            日期
     * @param day
     *            所加天数
     * @return
     */
    public static Date addDateDayOfYear(Date date, int day) {
        return addDate(date, GregorianCalendar.DAY_OF_YEAR, day);
    }

    /**
     * 日期加年
     * 
     * @param date
     *            日期
     * @param year
     *            所加天数
     * @return
     */
    public static Date addDateYear(Date date, int year) {
        return addDate(date, GregorianCalendar.YEAR, year);
    }

    /**
     * 随机生成字符串
     * 
     * @param length
     *            长度
     * @return
     */
    public static String randomString(int length) {
        if (length < 1) {
            return null;
        }
        // Create a char buffer to put random letters and numbers in.
        char[] randBuffer = new char[length];
        for (int i = 0; i < randBuffer.length; i++) {
            randBuffer[i] = numbersAndLetters[randGen.nextInt(71)];
        }
        return new String(randBuffer);
    }

    /**
     * JSON给实体类赋值
     * 
     * @param entity
     *            实体类
     * @param json
     *            JSON
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T jsonToProperty(T entity, String json) {
        if (isNotEmpty(json)) {
            Map<String, Object> jsonMap = (Map<String, Object>) gson.fromJson(json, Map.class);
            return mapToProperty(entity, jsonMap);
        }
        return entity;
    }

    /**
     * Map给实体类赋值
     * 
     * @param entity
     *            实体类
     * @param map
     *            Map
     * @return
     */
    public static <T> T mapToProperty(T entity, Map<String, Object> map) {
        if (isNotEmpty(map)) {
            Map<String, Object> fieldTypes = getFieldTypeMap(entity);
            Field[] fields = getAllFields(entity.getClass());
            if (null != fields && fields.length > 0) {
                for (Field field : fields) {
                    String fName = field.getName();
                    if (map.containsKey(fName)) {
                        try {
                            Class<?> clazz = (Class<?>) fieldTypes.get(fName);
                            Object setVal = map.get(fName);
                            Object beanVal = null;
                            // 如果是Map结构 说明仍然是bean需要转换
                            if (setVal instanceof Map) {
                                beanVal = gson.fromJson(gson.toJson(setVal), clazz);
                            } else {
                                beanVal = setVal;
                            }
                            field.setAccessible(true);
                            field.set(entity, beanVal);
                        } catch (Exception e) {
                            continue;
                        }
                    }
                }
            }
        }
        return entity;
    }

    /**
     * 拷贝属性
     * 
     * @param from
     *            来源Object
     * @param target
     *            目标Object
     */
    public static void copyProperties(Object from, Object target) {
        copyProperties(from, target, new String[0]);
    }

    /**
     * 拷贝属性
     * 
     * @param from
     *            来源Object
     * @param target
     *            目标Object
     * @param excludes
     *            排除属性
     */
    public static void copyProperties(Object from, Object target, String... excludes) {
        Map<String, String> excludeMap = Maps.newHashMap();
        // 如果排除的属性不为空则转成Map
        if (null != excludes) {
            excludeMap = Arrays.asList(excludes).stream().collect(Collectors.toMap(String::toString, String::toString));
        }
        // 获取(属性名,属性值)的map
        Map<String, Object> fromFiledMap = getAllFieldsMap(from);
        // 获取target全部属性
        Field[] targetFields = getAllFields(target.getClass());
        // 遍历
        for (Field tField : targetFields) {
            String fName = tField.getName();
            // 如果某属性在 from中包含 且 在排除中不包含 则进行set操作
            if (fromFiledMap.containsKey(fName) && !excludeMap.containsKey(fName)) {
                try {
                    tField.setAccessible(true);
                    Object val = fromFiledMap.get(fName);
                    // 如果是空值，则使用空值策略注解
                    if (ConverterUtil.isEmpty(val)) {
                        NullStrategy strategy = from.getClass().getField(fName).getAnnotation(NullStrategy.class);
                        if (ConverterUtil.isNotEmpty(strategy)) {
                            // 如果空值跳过，则不进行set操作
                            if (strategy.except()) {
                                continue;
                            }
                            // 获取属性的类型
                            Class<?> fieldType = tField.getType();
                            // 如果属性的类型是String,则直接使用value
                            if (String.class.equals(fieldType)) {
                                // 如果强制使用转换器，也需要转换
                                if (strategy.useExchager()) {
                                    val = strategy.exchager().newInstance().exchage(strategy.value());
                                } else {
                                    // 否则就直接使用value
                                    val = strategy.value();
                                }
                            } else {
                                // 其他的需要经过转换器转换
                                val = strategy.exchager().newInstance().exchage(strategy.value());
                            }
                        }
                    }
                    tField.set(target, val);
                } catch (Exception e) {
                    // 如果遇到问题就跳过
                    continue;
                }
            }
        }
    }

    /**
     * 利用MD5进行加密
     * 
     * @param str
     *            待加密的字符串
     * @return 加密后的字符串
     */
    public static String md5(String str) {
        StringBuilder sf = new StringBuilder();
        MessageDigest md5;
        try {
            // 生成一个MD5加密计算摘要
            md5 = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md5.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md5.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sf.toString();
    }

    /**
     * escape转码
     * 
     * @param src
     *            字符串
     * @return
     */
    public static String escape(String src) {
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for (i = 0; i < src.length(); i++) {
            j = src.charAt(i);
            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16)
                    tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * escape解码
     * 
     * @param src
     *            字符串
     * @return
     */
    public static String unescape(String src) {
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < src.length()) {
            pos = src.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (src.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(src.substring(lastPos));
                    lastPos = src.length();
                } else {
                    tmp.append(src.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }
}