package com.example.keepbookkeeping.activities;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mToolBar;

    @BindView(R.id.main_layout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.main_search_edit_text)
    SearchEditText mSearchEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initToolBar();

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
