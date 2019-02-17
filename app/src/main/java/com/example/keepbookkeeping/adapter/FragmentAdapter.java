package com.example.keepbookkeeping.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :fragment适配器
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragmentList;

    public FragmentAdapter(FragmentManager manager, List<Fragment> fragmentList){
        super(manager);
        mFragmentList=fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (mFragmentList!=null){
            return mFragmentList.get(position);
        }
        return null;
    }

    @Override
    public int getCount() {
        if (mFragmentList!=null){
            return mFragmentList.size();
        }
        return 0;
    }
}
