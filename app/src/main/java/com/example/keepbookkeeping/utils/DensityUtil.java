package com.example.keepbookkeeping.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.example.keepbookkeeping.KBKApplication;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :获取屏幕宽高的工具
 */
public class DensityUtil {

    /**
     * 屏幕宽度（像素）
     * */
    private int width;

    /**
     * 屏幕高度（像素）
     * */
    private int height;

    /**
     * 屏幕密度（0.75 / 1.0 / 1.5）
     * */
    private float density;

    /**
     * 屏幕密度dpi（120 / 160 / 240）
     * */
    private int densityDPI;

    /**
     * 屏幕宽度(dp):屏幕宽度（像素）/屏幕密度
     * */
    private int screenWidth;

    /**
     * 屏幕高度(dp)
     * */
    private int screenHeight;

    private static class DensityUtilInstance{
        private static final DensityUtil INSTANCE=new DensityUtil();
    }

    private DensityUtil(){
        WindowManager windowManager=(WindowManager) KBKApplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        width=displayMetrics.widthPixels;
        height=displayMetrics.heightPixels;
        density=displayMetrics.density;
        densityDPI=displayMetrics.densityDpi;
        screenWidth=(int) (width/density);
        screenHeight=(int) (height/density);
    }

    public static DensityUtil getInstance(){
        return DensityUtilInstance.INSTANCE;
    }

    public int getWidth(){
        if (getInstance()!=null){
            return getInstance().width;
        }
        return 0;
    }

    public int getHeight(){
        if (getInstance()!=null){
            return getInstance().height;
        }
        return 0;
    }

    public float getDensity(){
        if (getInstance()!=null){
            return getInstance().density;
        }
        return 0;
    }

    public int getDensityDpi(){
        if (getInstance()!=null){
            return getInstance().densityDPI;
        }
        return 0;
    }

    public int getScreenWidth(){
        if (getInstance()!=null){
            return getInstance().screenWidth;
        }
        return 0;
    }

    public int getScreenHeight(){
        if (getInstance()!=null){
            return getInstance().screenHeight;
        }
        return 0;
    }
}
