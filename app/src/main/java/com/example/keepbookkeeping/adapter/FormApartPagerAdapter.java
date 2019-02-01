package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.SingleDataBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/29
 * @description :
 */
public class FormApartPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<SingleDataBean> mMonthDataBeanList;

    public FormApartPagerAdapter(Context context, List<SingleDataBean> singleDataBeanList) {
        mContext = context;
        mMonthDataBeanList = singleDataBeanList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=View.inflate(mContext,R.layout.form_apart_piepic_item,null);

        ImageView imageView=view.findViewById(R.id.form_apart_piepic);
        TextView typeTextView=view.findViewById(R.id.form_apart_name);
        TextView moneyTextView=view.findViewById(R.id.form_apart_money);

        if (position==0){
            typeTextView.setText("月总收入");
            moneyTextView.setText("12000.00元");
        }else if (position==1){
            typeTextView.setText("月总支出");
            moneyTextView.setText("7000.00元");
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position==0){
            return "月总收入";
        }else {
            return "月总支出";
        }
    }
}
