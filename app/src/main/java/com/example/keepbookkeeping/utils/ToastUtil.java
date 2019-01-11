package com.example.keepbookkeeping.utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.example.keepbookkeeping.KBKApplication;

import es.dmoral.toasty.Toasty;

/**
 * Created by 邹永鹏 on 2019/1/11
 * Description :使用第三方toasty
 */
public class ToastUtil {

    public static void success(String text){
        if (!TextUtils.isEmpty(text)){
            Toasty.success(KBKApplication.getContext(), text, Toast.LENGTH_SHORT, true).show();
        }
    }

    public static void error(String text){
        if (!TextUtils.isEmpty(text)){
            Toasty.error(KBKApplication.getContext(), text, Toast.LENGTH_SHORT, true).show();
        }
    }

    public static void info(String text){
        if (!TextUtils.isEmpty(text)){
            Toasty.info(KBKApplication.getContext(), text, Toast.LENGTH_SHORT, true).show();
        }
    }

    public static void warning(String text){
        if (!TextUtils.isEmpty(text)){
            Toasty.warning(KBKApplication.getContext(), text, Toast.LENGTH_SHORT, true).show();
        }
    }

    public static void normal(String text){
        if (!TextUtils.isEmpty(text)){
            Toasty.normal(KBKApplication.getContext(), text).show();
        }
    }
}
