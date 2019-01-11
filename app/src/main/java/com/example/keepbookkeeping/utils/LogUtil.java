package com.example.keepbookkeeping.utils;

import android.os.Build;
import android.util.Log;

/**
 * @author 邹永鹏
 * @date 2019/1/11
 * @description : 自定义的log工具
 */
public class LogUtil {

    public static final int VERBOSE = 1;

    public static final int DEBUG = 2;

    public static final int INFO = 3;

    public static final int WARN = 4;

    public static final int ERROR = 5;

    public static final int NOTHING = 6;

    /**
        通过改变level的值控制打印log
     **/
    public static int level = VERBOSE;

    public static void v(String tag,String msg){
        if (level<=VERBOSE){
            Log.v(tag,msg);
        }
    }

    public static void d(String tag,String msg){
        if (level<=DEBUG){
            Log.d(tag,msg);
        }
    }

    public static void i(String tag,String msg){
        if (level<=INFO){
            Log.i(tag,msg);
        }
    }

    public static void w(String tag,String msg){
        if (level<=WARN){
            Log.w(tag,msg);
        }
    }

    public static void e(String tag,String msg){
        if (level<=ERROR){
            Log.e(tag,msg);
        }
    }
}
