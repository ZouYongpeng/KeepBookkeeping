package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.DataTypeBean;
import com.example.keepbookkeeping.bean.InComeBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/30
 * @description :
 */
public class AddDataViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    public AddDataViewPagerAdapter(Context context) {
        mContext = context;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=View.inflate(mContext, R.layout.add_data_view_pager_item,null);

//        RecyclerView recyclerView=view.findViewById(R.id.add_data_viewPager_recyclerView);
        TextView textView=view.findViewById(R.id.add_data_viewPager_textView);

        if (position==0){
            //收入
            textView.setText("收入");
        }else if (position==1){
            //支出
            textView.setText("支出");
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
}
