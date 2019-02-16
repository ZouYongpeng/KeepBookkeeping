package com.example.keepbookkeeping.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.utils.DataTypeTableUtil;

/**
 * @author 邹永鹏
 * @date 2019/1/30
 * @description :AddDataActivity的支出/收入分类PagerAdapter
 */
public class AddDataViewPagerAdapter extends PagerAdapter {

    private Context mContext;

    /**
     * 一行显示几列
     */
    private int mCount;

    public AddDataViewPagerAdapter(Context context,int count) {
        mContext = context;
        mCount = count;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view=View.inflate(mContext, R.layout.add_data_view_pager_item,null);

        RecyclerView recyclerView=view.findViewById(R.id.add_data_viewPager_recyclerView);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(mContext,mCount);
        recyclerView.setLayoutManager(gridLayoutManager);
        AddDataRecyclerViewAdapter recyclerViewAdapter;
        if (position==0){
            recyclerViewAdapter=new AddDataRecyclerViewAdapter(DataTypeTableUtil.getOutcomeDataTypeBeanList());
        }else {
            recyclerViewAdapter=new AddDataRecyclerViewAdapter(DataTypeTableUtil.getIncomeDataTypeBeanList());
        }
        recyclerView.setAdapter(recyclerViewAdapter);

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
