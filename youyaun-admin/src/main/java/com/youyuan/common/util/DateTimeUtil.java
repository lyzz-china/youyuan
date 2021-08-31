package com.youyuan.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;

/**
*
* @author yizhong.liao
* @createTime 2021/8/31 9:39
*/
public class DateTimeUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(DateTimeUtil.class);
    public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
    public static final String DEFAULT_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static Date getCurrentDate() {
        return new Date(System.currentTimeMillis());
    }
    public static String getCurrentDateAsString() {
        return getDateAsString(getCurrentDate(), DEFAULT_DATE_PATTERN);
    }

    public static String getCurrentDateTimeAsString() {
        return getDateAsString(getCurrentDate(), DEFAULT_DATETIME_PATTERN);
    }

    public static String getCurrentDateAsString(String pattern) {
        return getDateAsString(getCurrentDate(), pattern);
    }

    public static String getDateAsString(Date date) {
        return getDateAsString(date, DEFAULT_DATE_PATTERN);
    }

    public static String getDateAsString(Date date, String pattern) {
        if(null == date){
            return null;
        }
        DateFormat df = new SimpleDateFormat(pattern);
        return df.format(date);
    }

    public static Date parseDate(String dateStr) {
        return parseDate(dateStr, DEFAULT_DATE_PATTERN);
    }

    public static Date parseDate(String dateStr, String pattern) {
        if(StringUtil.isEmpty(dateStr)){
            return null;
        }
        try {
            if(dateStr.contains("T")){
                Instant ts = Instant.parse(dateStr);
                return Date.from(ts);
            }else{
                DateFormat df = new SimpleDateFormat(pattern);
                return df.parse(dateStr);
            }
        } catch (ParseException e) {
            LOGGER.warn("DateTimeUtil.parseDate exception occurs ", e);
        }
        return null;
    }

    public static String stampToDate(String stamp){
        if(StringUtil.isEmpty(stamp)){
            return null;
        }
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DEFAULT_DATETIME_PATTERN);
            long lt = new Long(stamp);
            Date date = new Date(lt);
            return simpleDateFormat.format(date);
        } catch (Exception e) {
            LOGGER.warn("DateTimeUtil.stampToDate exception occurs ", e);
        }
        return null;
    }

    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentQuarter() {
        return getQuarterByMonth(getCurrentMonth());
    }

    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getQuarter(Date date) {
        return getQuarterByMonth(getMonth(date));
    }

    public static int getQuarterByMonth(int month) {
        int quarter = ((month - 1) / 3) + 1 ;
        return quarter > 4 ? -1 : quarter;
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }



    public static int getWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        return c.get(Calendar.WEEK_OF_YEAR);
    }

    public static Date getDateByDayOffset(Date srcDate, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int offsetDay = day + days;
        c.set(Calendar.DAY_OF_MONTH, offsetDay);
        return c.getTime();
    }

    public static Date getDateByMonthOffset(Date srcDate, int months) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        int month = c.get(Calendar.MONTH);
        int offsetMonth = month + months;
        c.set(Calendar.MONTH, offsetMonth);
        return c.getTime();
    }

    public static Date getDateByHourOffset(Date srcDate, int hours) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int offsetHour = hour + hours;
        c.set(Calendar.HOUR_OF_DAY, offsetHour);
        return c.getTime();
    }

    public static Date getDateByMinuteOffset(Date srcDate, int minutes) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        int minute = c.get(Calendar.MINUTE);
        int offsetMinute = minute + minutes;
        c.set(Calendar.MINUTE, offsetMinute);
        return c.getTime();
    }

    public static Date getDateBySecondOffset(Date srcDate, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(srcDate);
        int second = c.get(Calendar.SECOND);
        int offsetSecond = second + seconds;
        c.set(Calendar.SECOND, offsetSecond);
        return c.getTime();
    }

    public static int getDayDiff(Date startDate, Date endDate) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(startDate);
//        int day1 = cal.get(Calendar.DAY_OF_YEAR);
//        cal.setTime(endDate);
//        int day2 = cal.get(Calendar.DAY_OF_YEAR);
//        return day2 - day1;
        long  diff_long =   (endDate.getTime()-startDate.getTime()) / 86400000;
        return (int) diff_long;
    }

    public static Date getFirstDayOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getLastDayOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
        return c.getTime();
    }

    public static Date getFirstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        c.set(Calendar.MILLISECOND, 0);
        return c.getTime();
    }

    public static Date getLastDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        c.set(Calendar.MILLISECOND, 997);
        return c.getTime();
    }

    public static Date getFirstDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    public static Date getLastDayOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    public static Date getFirstDayOfLastYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
        c.set(Calendar.DAY_OF_YEAR, 1);
        return c.getTime();
    }

    public static Date getLastDayOfLastYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 1);
        c.set(Calendar.DAY_OF_YEAR, c.getActualMaximum(Calendar.DAY_OF_YEAR));
        return c.getTime();
    }

    public static Date getFirstDayOfLastMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    public static Date getLastDayOfLastMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, c.get(Calendar.MONTH) - 1);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    public static Date getFirstDayOfLastWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, -7);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    public static Date getLastDayOfLastWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.MONDAY);
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, -7);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();
    }

    public static Date getLastTimeOfLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
