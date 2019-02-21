package com.example.keepbookkeeping.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class DateUtil {

    private static final String TAG="date";

    private static final SimpleDateFormat YEAR_MONTH_DAY=new SimpleDateFormat("yyyy-MM-dd");

    public static String getCurrentYearMonthDay(){
        return YEAR_MONTH_DAY.format(new Date());
    }

    public static int getCurrentYear(){
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    public static int getCurrentMonth(){
        return Calendar.getInstance().get(Calendar.MONTH)+1;
    }

    public static int getCurrentDay(){
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    public static int getCurrentHour(){
        return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }

    public static int getCurrentMinute(){
        return Calendar.getInstance().get(Calendar.MINUTE);
    }

    public static int getCurrentSecond(){
        return Calendar.getInstance().get(Calendar.SECOND);
    }



    /**
     * String转Date
     * @param str
     * @return
     */
    public static Date stringToDate(String str) {
        Date date=null;
        try {
            date=YEAR_MONTH_DAY.parse(str);
        }catch (ParseException e){
            e.printStackTrace();
        }finally {
            return date;
        }
    }

    /**
     * Date转String
     * @param date
     * @return
     */
    public static String dateToString(Date date){
        return YEAR_MONTH_DAY.format(date);
    }

    /**
     * 获取日期
     * @param date "2019-02-21"
     * @return String 21
     */
    public static String getDayOfDate(String date){
        return date.split("-")[2];
    }

    /**
     * 获取年月
     * @param date "2019-02-21"
     * @return String 2019-02
     */
    public static String getYearMonthOfDate(String date) {
        return date.substring(0,7);
    }

    /**
     * 获取年月
     * @param date "2019-02-21"
     * @return String 2019-02
     */
    public static String getYearOfDate(String date) {
        return date.substring(0,4);
    }

}
