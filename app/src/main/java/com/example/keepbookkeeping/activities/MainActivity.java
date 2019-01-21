package com.example.keepbookkeeping.activities;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.FragmentAdapter;
import com.example.keepbookkeeping.bill.BillFragment;
import com.example.keepbookkeeping.form.FormFragment;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.utils.DensityUtil;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

 public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,TabHost.OnTabChangeListener{

     private final String TAG="MainActivity_log";

    @BindView(R.id.main_toolbar)
    Toolbar mToolBar;

    @BindView(R.id.main_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.main_search_edit_text)
    SearchEditText mSearchEditText;

    @BindView(R.id.main_viewPager)
    ViewPager mViewPager;

    @BindView(R.id.main_tabHost)
    FragmentTabHost mTabHost;

    private LayoutInflater mLayoutInflater;
    private Class[] fragmentList={
            BillFragment.class,
            ListFragment.class,
            FormFragment.class};

    private int[] tabImageViewArray={
            R.drawable.ic_bill_unclicked,
            R.drawable.ic_list_unclicked,
            R.drawable.ic_form_unclicked};

     private int[] tabClickedImageViewArray={
             R.drawable.ic_bill_clicked,
             R.drawable.ic_list_clicked,
             R.drawable.ic_form_clicked};

     private String[] tabNameArray={"账单","明细","报表"};

     private List<Fragment> mFragmentArray=new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initToolBar();
        initViewPagerAndTab();
        initFragment();
    }

    private void initToolBar(){
         setSupportActionBar(mToolBar);
         //不显示ToolBar的标题
         getSupportActionBar().setDisplayShowTitleEnabled(false);
         //显示图标
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);
         //设置图标
         getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_nav_menu);
         //设置软键盘监听回车键
         mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
             @Override
             public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                 if (i== EditorInfo.IME_ACTION_DONE && !mSearchEditText.getText().toString().isEmpty()){
                     mSearchEditText.setText("");
                     KeyBoardUtil.hideSoftKeyBoard(mSearchEditText.getWindowToken(),MainActivity.this);
                     ToastUtil.success("开始查询");
                     return true;
                 }
                 return false;
             }
         });
     }

    private void initViewPagerAndTab(){
        mViewPager.addOnPageChangeListener(this);
        mLayoutInflater=LayoutInflater.from(this);

        mTabHost.setup(this,getSupportFragmentManager(),R.id.main_viewPager);
        mTabHost.getTabWidget().setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
        mTabHost.setOnTabChangedListener(this);

        int tabCount=tabNameArray.length;
        for (int i=0;i<tabCount;i++){
            //给每个Tab按钮设置标签、图标和文字
            TabHost.TabSpec tabSpec=mTabHost.newTabSpec(tabNameArray[i]).setIndicator(getTabItemView(i,1));
            /*将Tab按钮添加进Tab选项卡中，并绑定Fragment*/
            mTabHost.addTab(tabSpec,fragmentList[i],null);
            mTabHost.setTag(i);
        }
    }

     /**
      * 将xml布局转换为View对象,当滑动过程中
      * 当前界面position==colorPosition，图标颜色发生变化
      * @param position,colorPosition
      * @return
      */
    private View getTabItemView(int position,int colorPosition){
        View view=mLayoutInflater.inflate(R.layout.main_tab_content,null);
        TextView textView=view.findViewById(R.id.main_tab_textView);
        textView.setText(tabNameArray[position]);

        ImageView imageView=view.findViewById(R.id.main_tab_image);
        imageView.getLayoutParams().width= DensityUtil.getInstance().getWidth()/(tabNameArray.length*5);
        imageView.getLayoutParams().height=imageView.getLayoutParams().width;
        if (position==colorPosition){
            imageView.setBackgroundResource(tabClickedImageViewArray[position]);
            //textView.setTextColor(getResources().getColor(R.color.main_color));
        }else {
            imageView.setBackgroundResource(tabImageViewArray[position]);
        }
        return view;
    }

    private void initFragment(){
        Fragment billFragment=new BillFragment();
        Fragment listFragment=new ListFragment();
        Fragment formFragment=new FormFragment();

        mFragmentArray.add(billFragment);
        mFragmentArray.add(listFragment);
        mFragmentArray.add(formFragment);

        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(),mFragmentArray));
        mTabHost.getTabWidget().setDividerDrawable(null);
        mTabHost.setCurrentTab(1);
        mViewPager.setCurrentItem(1);
    }

     @Override
     public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

     }

     @Override
     public void onPageScrollStateChanged(int state) {

     }

     /**
      *
      * @param position
      */
     @Override
     public void onPageSelected(int position) {
        TabWidget widget=mTabHost.getTabWidget();
        int oldSelectedPosition=widget.getDescendantFocusability();
        /*设置View覆盖子类控件而直接获得焦点*/
        widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        mTabHost.setCurrentTab(position);
        widget.setDescendantFocusability(oldSelectedPosition);

        for (int i=0;i<tabNameArray.length;i++){
            if (i==position){
                widget.getChildAt(i).findViewById(R.id.main_tab_image)
                        .setBackgroundResource(tabClickedImageViewArray[i]);
            }else {
                widget.getChildAt(i).findViewById(R.id.main_tab_image)
                        .setBackgroundResource(tabImageViewArray[i]);
            }
        }
     }

     /**
      * Tab改变的时候调用
      * @param tabId
      */
     @Override
     public void onTabChanged(String tabId) {

     }

     @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_toolbar_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.select_date:
                ToastUtil.success("选择日期");
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * 点击非edittext时隐藏软键盘
     * @param event
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction()==MotionEvent.ACTION_DOWN){
            View view = getCurrentFocus();
            if (KeyBoardUtil.isShouldHideSoftKeyBoard(view,event)){
                KeyBoardUtil.hideSoftKeyBoard(view.getWindowToken(),this);
            }
        }
        return super.dispatchTouchEvent(event);
    }
}
