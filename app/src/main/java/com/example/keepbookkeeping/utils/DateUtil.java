package com.example.keepbookkeeping.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class DateUtil {

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

}
