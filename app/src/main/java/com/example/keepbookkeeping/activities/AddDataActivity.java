package com.example.keepbookkeeping.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.AddDataViewPagerAdapter;
import com.example.keepbookkeeping.bean.DataTypeBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddDataActivity extends AppCompatActivity {

    @BindView(R.id.add_data_view_pager)
    ViewPager mAddDataViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        ButterKnife.bind(this);

        initViewPager();
    }

    private void initViewPager(){
        mAddDataViewPager.setAdapter(new AddDataViewPagerAdapter(this));
    }

    public static void startAddDataActivity(Context context){
        Intent intent=new Intent(context,AddDataActivity.class);
        context.startActivity(intent);
    }
}
