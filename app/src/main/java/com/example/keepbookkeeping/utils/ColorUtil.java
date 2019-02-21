package com.example.keepbookkeeping.utils;

import android.graphics.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/2/21
 * @description :
 */
public class ColorUtil {

    public static final List<Integer> getPieChartColorList(){
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#f36c60"));
        colors.add(Color.parseColor("#fba6c8"));
        colors.add(Color.parseColor("#7986cb"));
        colors.add(Color.parseColor("#4fc3f7"));
        colors.add(Color.parseColor("#cfd8dc"));
        colors.add(Color.parseColor("#4db6ac"));
        colors.add(Color.parseColor("#aed581"));
        colors.add(Color.parseColor("#fff176"));
        colors.add(Color.parseColor("#ffb74d"));
        return colors;
    }

}
