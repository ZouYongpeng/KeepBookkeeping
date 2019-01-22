package com.example.keepbookkeeping.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/22
 * @description :
 */
public class TextButtonGroup<X extends TextView> extends ViewGroup {

    public static final String BTN_MODE = "BTNMODE"; //按钮模式
    public static final String TEV_MODE = "TEVMODE"; //文本模式

    private static final String TAG = "TextButtonGroup_log";
    private final int HorInterval = 10;    //水平间隔
    private final int VerInterval = 10;    //垂直间隔

    private int viewWidth;   //控件的宽度
    private int viewHeight;  //控件的高度

    private List<String> mStrings=new ArrayList<String>();

    private Context mContext;

    private int textModePadding=15;

    //正常样式
    private float itemTextSize = 18;
//    private int itemBGResNor = R.drawable.goods_item_btn_normal;
//    private int itemTextColorNor = Color.parseColor("#000000");
//
//    //选中的样式
//    private int itemBGResPre = R.drawable.goods_item_btn_selected;
//    private int itemTextColorPre = Color.parseColor("#ffffff");
//
    public TextButtonGroup(Context context) {
        this(context, null);
    }

    public TextButtonGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }
//
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
