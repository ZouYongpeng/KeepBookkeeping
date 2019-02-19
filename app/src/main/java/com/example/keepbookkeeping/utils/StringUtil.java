package com.example.keepbookkeeping.utils;

import android.text.TextUtils;

/**
 * @author 邹永鹏
 * @date 2019/2/19
 * @description :
 */
public class StringUtil {

    public static int getPositionInStrings(String[] strings,String value){
        if (strings!=null && !TextUtils.isEmpty(value)){
            for (int i=0;i<strings.length;i++) {
                if (TextUtils.equals(strings[i],value)){
                    return i;
                }
            }
        }
        return 0;
    }

}
