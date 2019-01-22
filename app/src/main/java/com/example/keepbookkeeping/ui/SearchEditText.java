package com.example.keepbookkeeping.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.utils.LogUtil;

/**
 * @author 邹永鹏
 * @date 2019/1/13
 * @description :
 */
public class SearchEditText extends AppCompatEditText{

    private final String tag="SearchEditText";

    private Drawable mClearIcon;

    private boolean mClearVisiable=false;

    private Context mContext;

    public SearchEditText(Context context) {
        super(context);
        initView(context);
    }

    public SearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context){
        mContext=context;
        mClearIcon=getResources().getDrawable(R.drawable.ic_edittext_clear_icon);
        mClearIcon.setBounds(0,0,mClearIcon.getMinimumWidth()-20,mClearIcon.getMinimumHeight()-20);

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text=getText().toString();
                if (isFocused() && !text.isEmpty()){
                    LogUtil.d(tag,"处在焦点的文本不为空");
                    Log.d(tag, "afterTextChanged: ");
                    if (!mClearVisiable){
                        LogUtil.d(tag,"应该显示");
                        setCompoundDrawables(null,null,mClearIcon,null);
                        mClearVisiable=true;
                    }

                }else {
                    setCompoundDrawables(null,null,null,null);
                    mClearVisiable=false;
                }
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /*清除图标可见，当点击清除图标后松开时出发
         * MotionEvent.ACTION_UP：当最后一个触点松开时被触发。
         * MotionEvent.ACTION_CANCEL：触发事件结束*/
        if (mClearVisiable && mClearIcon!=null && event.getAction()==MotionEvent.ACTION_UP){
            if (event.getX()> getWidth()-getPaddingRight()-mClearIcon.getIntrinsicWidth()){
                setText("");
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
        }
        return super.onTouchEvent(event);
    }

}
