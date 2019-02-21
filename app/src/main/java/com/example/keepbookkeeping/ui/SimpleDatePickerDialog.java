package com.example.keepbookkeeping.ui;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;

import com.example.keepbookkeeping.activities.AddDataActivity;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

/**
 * @author 邹永鹏
 * @date 2019/2/1
 * @description :
 */
public class SimpleDatePickerDialog extends DatePickerDialog {

    private Context mContext;
    private View mView;
    private OnDateSetListener mListener;

    public SimpleDatePickerDialog(@NonNull Context context,View view) {
        super(context);
        mContext=context;
        mView=view;
    }

    public SimpleDatePickerDialog(@NonNull Context context, int themeResId,View view) {
        super(context, themeResId);
        mContext=context;
        mView=view;
    }

    public SimpleDatePickerDialog(@NonNull Context context, @Nullable OnDateSetListener listener, int year, int month, int dayOfMonth,View view) {
        super(context, listener, year, month, dayOfMonth);
        mContext=context;
        mView=view;
        mListener=listener;
    }

    public SimpleDatePickerDialog(@NonNull Context context, int themeResId, @Nullable OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
        mContext=context;
        mListener=listener;
    }

    public SimpleDatePickerDialog(@NonNull Context context, int themeResId, @Nullable OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth,View view) {
        super(context, themeResId, listener, year, monthOfYear, dayOfMonth);
        mContext=context;
        mView=view;
        mListener=listener;
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mView!=null){
            ToastUtil.success("执行");
            mView.callOnClick();
            mView.callOnClick();
//            mView.requestFocus();
//            KeyBoardUtil.showSoftKeyBoard(mView, mContext);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        if (mView!=null){
            ToastUtil.success("执行");
            mView.callOnClick();
            mView.requestFocus();
            KeyBoardUtil.showSoftKeyBoard(mView, mContext);
        }
    }

    @Override
    public void onClick(@NonNull DialogInterface dialog, int which) {
        switch (which) {
            case BUTTON_POSITIVE:
                if (mListener != null) {
                    // Clearing focus forces the dialog to commit any pending
                    // changes, e.g. typed text in a NumberPicker.
                    // mListener.clearFocus();
                    mListener.onDateSet(super.getDatePicker(), super.getDatePicker().getYear(),
                            super.getDatePicker().getMonth(), super.getDatePicker().getDayOfMonth());
                }
                break;
            case BUTTON_NEGATIVE:
                cancel();
                break;
        }
        if (mView!=null){
            ToastUtil.success("执行");
//            mView.callOnClick();
            mView.requestFocus();
            KeyBoardUtil.showSoftKeyBoard(mView, mContext);
        }
    }
}
