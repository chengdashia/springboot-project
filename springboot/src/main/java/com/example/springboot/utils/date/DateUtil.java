package com.example.springboot.utils.date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * @author 成大事
 * @since 2022/5/26 20:29
 * 日期工具类，注意导包import和package
 * StringUtils，DateUtils是Maven依赖commons-lang3的类
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    /** 年-月-日 时:分:秒 显示格式 */
    public static String DATE_TO_STRING_DETAIAL_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /** 年-月-日 显示格式 */
    public static String DATE_TO_STRING_SHORT_PATTERN = "yyyy-MM-dd";
    /** 年-月 显示格式 */
    public static String DATE_TO_STRING_MONTH_PATTERN = "yyyy-MM";
    private static SimpleDateFormat simpleDateFormat;

    public static Date currentDate() {
        return  new Date();
    }

    /**
     * 获取当前时间
     * @return date
     */
    public static String currentFormatDate() {
        simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);
        return simpleDateFormat.format(new Date());
    }

    /**
     * date转化为String
     * @param date  时间
     * @return date
     */
    public static String dateToString(Date date) {
        simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);
        return simpleDateFormat.format(date);
    }

    /**
     * date转化为String
     * @param date   时间
     * @return  date
     */
    public static String dateToShortString(Date date) {
        simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
        return simpleDateFormat.format(date);
    }

    public static Date addMinuteToDate(int minute){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minute);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 按照指定格式格式化时间
     * @param date 当前日期
     * @param pattern  格式
     * @return Date
     */
    public static String dateToStringByPattern(Date date, String pattern){
        if(StringUtils.isBlank(pattern)){
            pattern = DATE_TO_STRING_DETAIAL_PATTERN;
        }
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    /**
     * 给指定的日期增加分钟，为空时默认当前时间
     * @param minute 增加分钟  正数相加、负数相减
     * @return String
     */
    public static String addMinuteToDateStr(int minute){
        return dateToString(addMinuteToDate(minute));
    }

    public static Date addDayToDate(int day){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, day);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        Date date = calendar.getTime();
        return date;
    }

    public static String addDayToDateStr(int day){
        return dateToString(addDayToDate(day));
    }

    public static Date getWeekMonday(){
        Calendar calendar = Calendar.getInstance();
        calendar.set(GregorianCalendar.DAY_OF_WEEK,GregorianCalendar.MONDAY);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        Date date = calendar.getTime();
        return date;
    }

    public static String getWeekMondayStr(){
        return dateToString(getWeekMonday());
    }

    public static Date addWeekToDate(int week){
        Date date= DateUtils.addWeeks(getWeekMonday(),week);
        return date;
    }

    public static String addWeekToDateStr(int week){
        return dateToString(addWeekToDate(week));
    }

    /**
     * 时间往后推n天的时间
     * @param date 当前日期
     * @param day  n天
     * @return Date
     */
    public static Date getNextDay(Date date, int day){
        try {
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(date);
            calendar.add(Calendar.DATE,day);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * String转化为Date
     * @param date 字符串时间
     * @return date
     */
    public static Date stringToDate(String date) {
        simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_DETAIAL_PATTERN);
        Date dateFormat = null;
        try {
            dateFormat = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

    /**
     * String转化为Date
     * @param date 字符串时间
     * @return date
     */
    public static Date stringToShortDate(String date) {
        simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
        Date dateFormat = null;
        try {
            dateFormat = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateFormat;
    }

    /**
     * 获取月第一天
     * @param month 增加月数  正数相加、负数相减
     * @return date
     */
    public static Date addMonthToDate(int month){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH,1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date date = calendar.getTime();
        return date;
    }

    /**
     * 获取月第一天
     * @param date 当前日期
     * @param day  n天
     * @return Date
     */
    public static Date addMonthToDate(String date, int day){
        simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_MONTH_PATTERN);
        Date monthDate = null;
        try {
            monthDate = simpleDateFormat.parse(date);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(monthDate);
            calendar.add(Calendar.MONTH,day);//把日期往后增加一天.整数往后推,负数往前移动
            monthDate = calendar.getTime(); //这个时间就是日期往后推一天的结果
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthDate;
    }

    /**
     * 时间往后推n天的时间
     * @param date 当前日期
     * @param day  n天
     * @return Date
     */
    public static Date addDayToDate(Date date, int day){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.add(Calendar.DATE,day);
        date = calendar.getTime();
        return date;
    }

    /**
     * date转化为String yyyy-MM
     * @param date
     * @return Date
     */
    public static String dateToStringMonth(Date date) {
        simpleDateFormat = new SimpleDateFormat(DATE_TO_STRING_MONTH_PATTERN);
        return simpleDateFormat.format(date);
    }

    /**
     * yyyy-MM-dd 转时间戳
     * @param dateStr
     * @return Date
     */
    public static Long strToTimestamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_TO_STRING_SHORT_PATTERN);
        Long timestamp = 0L;
        try {
            Date date = format.parse(dateStr);
            timestamp = date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timestamp;
    }

    /**
     * 获取当前时间
     *
     * @return Date
     */
    public static String currentFormatDate(String pattern) {
        simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(new Date());
    }

    /**
     * 字符串转日期
     * @param dateStr
     * @param pattern
     * @return Date
     */
    public static Date strToDate(String dateStr,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            logger.error("时间转换异常：{}",e);
        }
        return date;
    }

    /**
     * 获取指定月的开始日期
     * @param currentDate
     * @return Date
     */
    public static Date getStartDate(String currentDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
            c.add(Calendar.MONTH, 0);
            //设置为1号,当前日期既为本月第一天
            c.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获取指定月的结束日期
     * @param currentDate
     * @return Date
     */
    public static Date getEndDate(String currentDate){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获取指定月的开始日期
     * @param currentDate
     * @return Date
     */
    public static Date getStartDate(String currentDate,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
            c.add(Calendar.MONTH, 0);
            //设置为1号,当前日期既为本月第一天
            c.set(Calendar.DAY_OF_MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTime();
    }

    /**
     * 获取指定月的结束日期
     * @param currentDate
     * @return date
     */
    public static Date getEndDate(String currentDate,String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
            c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.SECOND, 59);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c.getTime();
    }


}
