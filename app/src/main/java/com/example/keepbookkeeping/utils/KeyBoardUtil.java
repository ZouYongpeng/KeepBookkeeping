package com.example.keepbookkeeping.utils;

import android.content.Context;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * @author 邹永鹏
 * @date 2019/1/13
 * @description :控制软键盘显示或隐藏
 */
public class KeyBoardUtil {

    /**
     * 判断点击的位置是否在指定的view里，
     * 如果在，则返回true
     * @param view
     * @param event
     * @return
     */
    public static boolean isShouldHideSoftKeyBoard(View view, MotionEvent event){
        if (view!=null && view instanceof EditText){
            int[] l={0,0};
            view.getLocationInWindow(l);
            int left=l[0];
            int right=left+view.getWidth();
            int top=l[1];
            int bottom=top+view.getHeight();

            if (left<event.getX() && event.getX()<right &&
                    top<event.getY() && event.getY()<bottom){
                return false;
            }else {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏软键盘
     * @param token
     * @param context
     */
    public static void hideSoftKeyBoard(IBinder token, Context context){
        if (token!=null){
            InputMethodManager im=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 强制软键盘显示
     * @param view
     * @param context
     */
    public static void showSoftKeyBoard(View view, Context context){
        if (view!=null){
            InputMethodManager im=(InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
            im.showSoftInput(view,InputMethodManager.SHOW_FORCED);
        }
    }
}
