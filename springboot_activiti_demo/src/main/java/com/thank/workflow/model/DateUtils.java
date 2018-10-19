package com.thank.workflow.model;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author:stephen
 * @date: 2017/12/21 11:31
 * @description:
 */
public class DateUtils {
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM = "yyyy-MM";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_EN = "yyyy/MM";
    public static final String YYYY_MM_DD_EN = "yyyy/MM/dd";
    public static final String YYYY_MM_DD_HH_MM_EN = "yyyy/MM/dd HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS_EN = "yyyy/MM/dd HH:mm:ss";
    public static final String YYYY_MM_CN = "yyyy年MM月";
    public static final String YYYY_MM_DD_CN = "yyyy年MM月dd日";
    public static final String YYYY_MM_DD_HH_MM_CN = "yyyy年MM月dd日 HH:mm";
    public static final String YYYY_MM_DD_HH_MM_SS_CN = "yyyy年MM月dd日 HH:mm:ss";
    public static final String HH_MM = "HH:mm";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String MM_DD = "MM-dd";
    public static final String MM_DD_HH_MM = "MM-dd HH:mm";
    public static final String MM_DD_HH_MM_SS = "MM-dd HH:mm:ss";
    public static final String MM_DD_EN = "MM/dd";
    public static final String MM_DD_HH_MM_EN = "MM/dd HH:mm";
    public static final String MM_DD_HH_MM_SS_EN = "MM/dd HH:mm:ss";
    public static final String MM_DD_CN = "MM月dd日";
    public static final String MM_DD_HH_MM_CN = "MM月dd日 HH:mm";
    public static final String MM_DD_HH_MM_SS_CN = "MM月dd日 HH:mm:ss";


    public static String getShortDate(Date date) {
        return format(date, YYYY_MM_DD);
    }

    public static String getLongDate() {
        return getLongDate(new Date());
    }

    public static String getLongDate(Date date) {
        return format(date, YYYY_MM_DD_HH_MM_SS);
    }

    public static String format(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static String formatDate(Date date, String pattern) {
        String formatDate = null;
        if (StringUtils.isNotBlank(pattern)) {
            formatDate = DateFormatUtils.format(date, pattern);
        } else {
            formatDate = DateFormatUtils.format(date, "yyyy-MM-dd");
        }
        return formatDate;
    }

    public static int getYear(Date date){
        if (date == null){
            date = new Date();
        }
        return getYearByDate(date);
    }

    private static int getYearByDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear();
    }

    /**
     * 获取给定的日期到当前日期年集合
     * @param startDate
     * @return
     */
    public static List<String> getYearsByDate(Date startDate){
        List<String> yearsList = new ArrayList<>();
        int startYear = getYearByDate(startDate);
        int curYear = getYear(null);
        if (startYear <= curYear){
            for(int i=curYear-startYear,e = 0;i>e;i--){
                yearsList.add(String.valueOf(curYear--));
            }
        }/*else {
            yearsList.add(String.valueOf(startYear));
        }*/
        return yearsList;
    }

    public static boolean compareDate(Date date1,Date date2){
        return date1.getTime()>date2.getTime();
    }

    public static int getMinuteByTwoTimedifference(Date date1,Date date2){
        long between = ChronoUnit.MINUTES.between(toLocalDateTime(date1), toLocalDateTime(date2));
        return Integer.valueOf(String.valueOf(between));
    }

    private static LocalDateTime toLocalDateTime(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    private static LocalDate toLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static Date parse(String dateStr,String format) throws ParseException {
        return new SimpleDateFormat(format).parse(dateStr);
    }

    public static Date operAddOrSubtract(Date date,long v,TimeUnit unit){
        if (v < 0 || unit == null){
            return date;
        }
        if (date == null){
            date = new Date();
        }
        switch (unit){
            case DAYS:
                return Date.from(toLocalDateTime(date).plusDays(v).atZone(ZoneId.systemDefault()).toInstant());
            case MINUTES:
                return Date.from(toLocalDateTime(date).plusMinutes(v).atZone(ZoneId.systemDefault()).toInstant());
            case HOURS:
                return Date.from(toLocalDateTime(date).plusHours(v).atZone(ZoneId.systemDefault()).toInstant());
            case SECONDS:
                return Date.from(toLocalDateTime(date).plusSeconds(v).atZone(ZoneId.systemDefault()).toInstant());
            case MONTHS:
                return Date.from(toLocalDateTime(date).plusMonths(v).atZone(ZoneId.systemDefault()).toInstant());
            case YEARS:
                return Date.from(toLocalDateTime(date).plusYears(v).atZone(ZoneId.systemDefault()).toInstant());
            case WEEKS:
                return Date.from(toLocalDateTime(date).plusWeeks(v).atZone(ZoneId.systemDefault()).toInstant());
            default:
                return date;
        }

    }

    public enum TimeUnit{
        SECONDS,MINUTES,HOURS,DAYS,WEEKS,MONTHS,YEARS
    }

}


