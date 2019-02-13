package com.example.keepbookkeeping.activities;

import android.database.sqlite.SQLiteDatabase;
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
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.FragmentAdapter;
import com.example.keepbookkeeping.bill.BillFragment;
import com.example.keepbookkeeping.bill.BillPresenterImpl;
import com.example.keepbookkeeping.db.KBKDataBaseHelper;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.form.FormFragment;
import com.example.keepbookkeeping.form.FormPresenterImpl;
import com.example.keepbookkeeping.list.ListPresenterImpl;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.utils.DataBaseUtil;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.DensityUtil;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :MainActivity
 */
 public class MainActivity extends AppCompatActivity {

     private final String TAG = "MainActivity_log";

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

     @BindView(R.id.main_bill_btn_group)
     RadioGroup mBillBtnGroup;

     @BindView(R.id.main_form_btn_group)
     RadioGroup mFormBtnGroup;

     private LayoutInflater mLayoutInflater;
     private Class[] fragmentList = {
             BillFragment.class,
             ListFragment.class,
             FormFragment.class};

     private int[] tabImageViewArray = {
             R.drawable.ic_bill_unclicked,
             R.drawable.ic_list_unclicked,
             R.drawable.ic_form_unclicked};

     private int[] tabClickedImageViewArray = {
             R.drawable.ic_bill_clicked,
             R.drawable.ic_list_clicked,
             R.drawable.ic_form_clicked};

     private String[] tabNameArray = {"账单", "明细", "报表"};

     private List<Fragment> mFragmentArray = new ArrayList<Fragment>();
     private int currentPagerPosition;
     private boolean isScrolled;

     private Menu mToolBarMenu;

     private BillFragment mBillFragment;
     private com.example.keepbookkeeping.list.ListFragment mListFragment;
     private FormFragment mFormFragment;

     private KBKDataBaseHelper mDataBaseHelper;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         ButterKnife.bind(this);

         initToolBar();
         initViewPagerAndTab();
         initFragment();
         initDataBase();
         mBillBtnGroup.setOnCheckedChangeListener(mRadioChangeListener);
         mFormBtnGroup.setOnCheckedChangeListener(mRadioChangeListener);
     }

     public void initDataBase(){
         mDataBaseHelper=KBKDataBaseHelper.getKBKDataBase(this);
         SQLiteDatabase db=mDataBaseHelper.getWritableDatabase();
         DataBaseUtil.queryAllDataOrderByDate(db);
//         DataBaseUtil.getDifferentDateCount(db);
//         DataBaseUtil.getAllDataCount(db);
         DataBaseUtil.getDifferentMonthCount(db);
     }

     private void initToolBar() {
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
                 if (i == EditorInfo.IME_ACTION_DONE && !mSearchEditText.getText().toString().isEmpty()) {
                     mSearchEditText.setText("");
                     KeyBoardUtil.hideSoftKeyBoard(mSearchEditText.getWindowToken(), MainActivity.this);
                     ToastUtil.success("开始查询");
                     return true;
                 }
                 return false;
             }
         });
         //设置默认选中
         mBillBtnGroup.check(R.id.main_toolbar_btn_assets);
         mFormBtnGroup.check(R.id.main_toolbar_btn_apart);
     }

     private void initViewPagerAndTab() {
         mViewPager.addOnPageChangeListener(mPageChangeListener);
         mLayoutInflater = LayoutInflater.from(this);

         mTabHost.setup(this, getSupportFragmentManager(), R.id.main_viewPager);
         mTabHost.getTabWidget().setDividerDrawable(new ColorDrawable(Color.TRANSPARENT));
         mTabHost.setOnTabChangedListener(mTabChangeListener);

         int tabCount = tabNameArray.length;
         for (int i = 0; i < tabCount; i++) {
             //给每个Tab按钮设置标签、图标和文字
             TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabNameArray[i]).setIndicator(getTabItemView(i, 1));
             /*将Tab按钮添加进Tab选项卡中，并绑定Fragment*/
             mTabHost.addTab(tabSpec, fragmentList[i], null);
             mTabHost.setTag(i);
         }
     }

     /**
      * 将xml布局转换为View对象,当滑动过程中
      * 当前界面position==colorPosition，图标颜色发生变化
      *
      * @param position,colorPosition
      * @return
      */
     private View getTabItemView(int position, int colorPosition) {
         View view = mLayoutInflater.inflate(R.layout.main_tab_content, null);
         TextView textView = view.findViewById(R.id.main_tab_textView);
         textView.setText(tabNameArray[position]);

         ImageView imageView = view.findViewById(R.id.main_tab_image);
         imageView.getLayoutParams().width = DensityUtil.getInstance().getWidth() / (tabNameArray.length * 5);
         imageView.getLayoutParams().height = imageView.getLayoutParams().width;
         if (position == colorPosition) {
             imageView.setBackgroundResource(tabClickedImageViewArray[position]);
             //textView.setTextColor(getResources().getColor(R.color.main_color));
         } else {
             imageView.setBackgroundResource(tabImageViewArray[position]);
         }
         return view;
     }

     private void initFragment() {
         mBillFragment = new BillFragment();
         mListFragment = new com.example.keepbookkeeping.list.ListFragment();
         mFormFragment = new FormFragment();

         /**
          * 必须的一步
          */
         new BillPresenterImpl(mBillFragment);
         new ListPresenterImpl(mListFragment);
         new FormPresenterImpl(mFormFragment);

         mFragmentArray.add(mBillFragment);
         mFragmentArray.add(mListFragment);
         mFragmentArray.add(mFormFragment);

         mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), mFragmentArray));
         mTabHost.getTabWidget().setDividerDrawable(null);
         mTabHost.setCurrentTab(1);
         mViewPager.setCurrentItem(1);
         currentPagerPosition = 1;
     }

     @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         getMenuInflater().inflate(R.menu.main_toolbar_menu, menu);
         mToolBarMenu=menu;
         return true;
     }

     @Override
     public boolean onOptionsItemSelected(MenuItem item) {
         switch (item.getItemId()) {
             case R.id.select_date:
                 ToastUtil.success("选择日期");
                 break;
             case android.R.id.home:
                 mDrawerLayout.openDrawer(GravityCompat.START);
                 break;
             default:
                 break;
         }
         return true;
     }

     @Override
     public void onBackPressed() {
         if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
             mDrawerLayout.closeDrawer(GravityCompat.START);
         } else {
             super.onBackPressed();
         }
     }

     /**
      * 点击非edittext时隐藏软键盘
      *
      * @param event
      * @return
      */
     @Override
     public boolean dispatchTouchEvent(MotionEvent event) {
         if (event.getAction() == MotionEvent.ACTION_DOWN) {
             View view = getCurrentFocus();
             if (KeyBoardUtil.isShouldHideSoftKeyBoard(view, event)) {
                 KeyBoardUtil.hideSoftKeyBoard(view.getWindowToken(), this);
             }
         }
         return super.dispatchTouchEvent(event);
     }

     private void refreshToolBar(){
         switch (currentPagerPosition){
             case 0:
                 mBillBtnGroup.setVisibility(View.VISIBLE);
                 mSearchEditText.setVisibility(View.INVISIBLE);
                 mFormBtnGroup.setVisibility(View.INVISIBLE);
                 if (mToolBarMenu!=null){
                     mToolBarMenu.findItem(R.id.select_date).setVisible(false);
                 }
                 break;
             case 1:
                 mBillBtnGroup.setVisibility(View.INVISIBLE);
                 mSearchEditText.setVisibility(View.VISIBLE);
                 mFormBtnGroup.setVisibility(View.INVISIBLE);
                 if (mToolBarMenu!=null){
                     mToolBarMenu.findItem(R.id.select_date).setVisible(true);
                 }
                 break;
             case 2:
                 mBillBtnGroup.setVisibility(View.INVISIBLE);
                 mSearchEditText.setVisibility(View.INVISIBLE);
                 mFormBtnGroup.setVisibility(View.VISIBLE);
                 if (mToolBarMenu!=null){
                     mToolBarMenu.findItem(R.id.select_date).setVisible(false);
                 }
                 break;
             default:
                 break;
         }
     }

     private void refreshTabHost(){
         TabWidget widget = mTabHost.getTabWidget();
         int oldSelectedPosition = widget.getDescendantFocusability();
         /*设置View覆盖子类控件而直接获得焦点*/
         widget.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
         mTabHost.setCurrentTab(currentPagerPosition);
         widget.setDescendantFocusability(oldSelectedPosition);

         for (int i = 0; i < tabNameArray.length; i++) {
             if (i == currentPagerPosition) {
                 widget.getChildAt(i).findViewById(R.id.main_tab_image)
                         .setBackgroundResource(tabClickedImageViewArray[i]);
             } else {
                 widget.getChildAt(i).findViewById(R.id.main_tab_image)
                         .setBackgroundResource(tabImageViewArray[i]);
             }
         }
     }

     private ViewPager.OnPageChangeListener mPageChangeListener = new ViewPager.OnPageChangeListener() {
         @Override
         public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

         }

         @Override
         public void onPageSelected(int position) {
             currentPagerPosition = position;
             refreshTabHost();
             refreshToolBar();
         }

         /**
          * 判断是否滑倒第一页或最后一页
          * arg0 ==1的时候表示正在滑动
          * arg0==2的时候表示滑动完毕了
          * arg0==0的时候表示什么都没做，就是停在那。
          */
         @Override
         public void onPageScrollStateChanged(int state) {
             switch (state) {
                 case 0:
                     if (!isScrolled) {
                         if (currentPagerPosition == 0) {
                             mDrawerLayout.openDrawer(GravityCompat.START);
                         } else if (currentPagerPosition == tabNameArray.length - 1) {
                             ToastUtil.success("已经滑倒最后一页啦");
                         }
                     }
                     break;
                 case 1:
                     isScrolled = false;
                     break;
                 case 2:
                     isScrolled = true;
                     break;
                 default:
                     break;
             }
         }
     };

     private TabHost.OnTabChangeListener mTabChangeListener=new TabHost.OnTabChangeListener() {
         /**
          * Tab改变的时候调用
          * @param tabId
          */
         @Override
         public void onTabChanged(String tabId) {
             int position = mTabHost.getCurrentTab();
             mViewPager.setCurrentItem(position);
         }
     };

     private RadioGroup.OnCheckedChangeListener mRadioChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton radioButton=findViewById(group.getCheckedRadioButtonId());
            final String selectedText=radioButton.getText().toString();
            switch (selectedText){
                case "资产":
                    RxBus.getInstance().post(new ChangeFragmentTypeEvent(BillFragment.TYPE_ASSETS));
                    break;
                case "负债":
                    RxBus.getInstance().post(new ChangeFragmentTypeEvent(BillFragment.TYPE_DEBT));
                    break;
                case "分类":
                    RxBus.getInstance().post(new ChangeFragmentTypeEvent(FormFragment.TYPE_APART_INCOME));
                    break;
                case "趋势":
                    RxBus.getInstance().post(new ChangeFragmentTypeEvent(FormFragment.TYPE_TREND));
                    break;
                default:
                    break;
            }
        }
     };

}
