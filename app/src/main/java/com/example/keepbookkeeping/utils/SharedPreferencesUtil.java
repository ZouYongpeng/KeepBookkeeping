package com.example.keepbookkeeping.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.keepbookkeeping.KBKApplication;

/**
 * @author 邹永鹏
 * @date 2019/2/1
 * @description :整合SharedPreferences的工具类
 */
public class SharedPreferencesUtil {

    private static SharedPreferences sp;

    static {
        sp= PreferenceManager.getDefaultSharedPreferences(KBKApplication.getContext());
    }

    public static void putString(String key,String value){
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String key){
        return getString(key,"");
    }

    public static String getString(String key,String defaultValue){
        return sp.getString(key,defaultValue);
    }

    public static void putInt(String key,int value){
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key){
        return getInt(key,0);
    }

    public static int getInt(String key,int defaultValue){
        return sp.getInt(key,defaultValue);
    }

    public static void putBoolean(String key,boolean value){
        sp.edit().putBoolean(key, value).commit();
    }

    public static Boolean getBoolean(String key){
        return sp.getBoolean(key,false);
    }

    public static Boolean getBoolean(String key,boolean value){
        return sp.getBoolean(key,value);
    }
}
