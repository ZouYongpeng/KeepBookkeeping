package com.example.keepbookkeeping.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.ListFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.FragmentAdapter;
import com.example.keepbookkeeping.bean.BmobBean.User;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.bill.BillFragment;
import com.example.keepbookkeeping.bill.BillPresenterImpl;
import com.example.keepbookkeeping.db.KBKAllDataBaseHelper;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.events.SearchAllDataEvent;
import com.example.keepbookkeeping.form.FormFragment;
import com.example.keepbookkeeping.form.FormPresenterImpl;
import com.example.keepbookkeeping.list.ListPresenterImpl;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.DensityUtil;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.PhotoUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import de.hdodenhof.circleimageview.CircleImageView;

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

     @BindView(R.id.nav_view)
     NavigationView mNavView;

    /**
     * NavigationView滑动菜单内控件
     */
     View mNavHeaderLayout;
     CircleImageView mUserHeadImage;
     TextView mUserName;
     TextView mUserSex;

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

     private List<Fragment> mFragmentArray = new ArrayList<>();
     private int currentPagerPosition;
     private boolean isScrolled;

     private Menu mToolBarMenu;

     private BillFragment mBillFragment;
     private com.example.keepbookkeeping.list.ListFragment mListFragment;
     private FormFragment mFormFragment;

     private User mUser;

     Dialog mSelectPicDialog;
     private Uri imageUri;

     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);
         ButterKnife.bind(this);
         LogUtil.d(TAG,"onCreate");
         initUser();
         initNavView();
         initToolBar();
         initViewPagerAndTab();
         initFragment();
         mBillBtnGroup.setOnCheckedChangeListener(mRadioChangeListener);
         mFormBtnGroup.setOnCheckedChangeListener(mRadioChangeListener);
     }

    @Override
    protected void onResume() {
        LogUtil.d(TAG,"onResume");
        super.onResume();

    }

    private void initUser(){
         Intent intent=getIntent();
         Bundle bundle=intent.getExtras();
         if (bundle!=null){
             mUser=(User)bundle.getSerializable("user");
             LogUtil.d(TAG,mUser.toString());
         }
     }

     public void initNavView(){
         mNavView.setNavigationItemSelectedListener(mNavListener);
         if (mNavHeaderLayout==null){
             mNavHeaderLayout = mNavView.inflateHeaderView(R.layout.main_mav_header);
             mUserHeadImage=(CircleImageView) mNavHeaderLayout.findViewById(R.id.nav_header_image);
             mUserName=(TextView) mNavHeaderLayout.findViewById(R.id.nav_header_name);
//             mUserSex=(TextView) mNavHeaderLayout.findViewById(R.id.nav_header_sex);
             mUserHeadImage.setOnClickListener(mUserHeadImageListener);
         }
         if (mUser!=null){
             mUserName.setText(mUser.getUsername());
         }
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
         mSearchEditText.addTextChangedListener(new TextWatcher() {
             @Override
             public void beforeTextChanged(CharSequence s, int start, int count, int after) {

             }

             @Override
             public void onTextChanged(CharSequence s, int start, int before, int count) {

             }

             @Override
             public void afterTextChanged(Editable s) {
                 String word=mSearchEditText.getText().toString().trim();
                 RxBus.getInstance().post(new SearchAllDataEvent(word));
             }
         });
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

         new BillPresenterImpl(mBillFragment);
         new ListPresenterImpl(mListFragment);
         new FormPresenterImpl(mFormFragment);

         mFragmentArray.add(mBillFragment);
         mFragmentArray.add(mListFragment);
         mFragmentArray.add(mFormFragment);

         mViewPager.setOffscreenPageLimit(3);
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

     private void initSelectPicDialog(){
         mSelectPicDialog=new Dialog(this,R.style.Theme_AppCompat_Dialog);
         View view=View.inflate(this,R.layout.dialog_bottom,null);
         mSelectPicDialog.setContentView(view);
         mSelectPicDialog.setCanceledOnTouchOutside(true);
         Window dialogWindow = mSelectPicDialog.getWindow();
         WindowManager.LayoutParams lp = dialogWindow.getAttributes();
         lp.width = WindowManager.LayoutParams.MATCH_PARENT;;// (int) (ScreenSizeUtils.getInstance(this).getScreenWidth())* 0.9f
         lp.height = WindowManager.LayoutParams.WRAP_CONTENT;//
         lp.gravity = Gravity.BOTTOM;
         dialogWindow.setAttributes(lp);

         Button camera=view.findViewById(R.id.camera);
         camera.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ToastUtil.success("打开相机");
//                 openCamera();
                 PhotoUtil.photograph(MainActivity.this);
             }
         });

         Button album=view.findViewById(R.id.album);
         album.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 ToastUtil.success("打开相册");
                 //因为要获取SD卡的照片，所以需要获取WRITE_EXTERNAL_STORAGE权限
                 if (ContextCompat.checkSelfPermission(
                         MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                         != PackageManager.PERMISSION_GRANTED){
                     //没有就申请获取
                     ActivityCompat.requestPermissions(
                             MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                 }else {
                     PhotoUtil.selectPictureFromAlbum(MainActivity.this);
                 }
             }
         });

         Button cancel=view.findViewById(R.id.cancel);
         cancel.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 mSelectPicDialog.dismiss();
             }
         });
     }

    private String path;

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode){
//            case PhotoUtil.NONE:
//                break;
//            case PhotoUtil.PHOTOGRAPH:
//                if (resultCode==RESULT_OK){
//                    // 设置文件保存路径这里放在根目录下
//                    File picture = null;
//                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
//                        picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
//                        if (!picture.exists()) {
//                            picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
//                        }
//                    } else {
//                        picture = new File(this.getFilesDir() + PhotoUtil.imageName);
//                        if (!picture.exists()) {
//                            picture = new File(MainActivity.this.getFilesDir() + PhotoUtil.imageName);
//                        }
//                    }
//                    Uri pictureUri= FileProvider.getUriForFile(this,"org.kbk.fileprovider", picture);
//                    // 生成一个地址用于存放剪辑后的图片
//                    path = PhotoUtil.getPath(this);
//                    if (TextUtils.isEmpty(path)) {
//                        Log.e("photo", "随机生成的用于存放剪辑后的图片的地址失败");
//                        return;
//                    }
//                    LogUtil.d("photo","剪切图 path = "+path);
//                    Uri imageUri=Uri.parse(path);
//                    PhotoUtil.startPhotoZoom(MainActivity.this, pictureUri, PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
//                    requestCode=PhotoUtil.PHOTORESOULT;
//                }
//                break;
//            case PhotoUtil.PHOTOZOOM:
//                //读取相册缩放图片
//                if (resultCode==RESULT_OK){
//                    // 生成一个地址用于存放剪辑后的图片
//                    path = PhotoUtil.getPath(this);
//                    if (TextUtils.isEmpty(path)) {
//                        LogUtil.e("photo", "随机生成的用于存放剪辑后的图片的地址失败");
//                        return;
//                    }
//                    LogUtil.d("photo","相册缩放图片 path = "+path);
//                    Uri imageUri=Uri.parse(path);
//                    PhotoUtil.startPhotoZoom(MainActivity.this, data.getData(), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
//                    requestCode=PhotoUtil.PHOTORESOULT;
//                }
//                break;
//            case PhotoUtil.PHOTORESOULT:
//                /**
//                 * 处理结果
//                 * 在这里处理剪辑结果，可以获取缩略图，获取剪辑图片的地址。
//                 * 得到这些信息可以选则用于上传图片等等操作
//                 * 如，根据path获取剪辑后的图片
//                 * */
//                if (resultCode==RESULT_OK){
//                    Bitmap bitmap = PhotoUtil.convertToBitmap(path,PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH);
//                    if(bitmap != null){
//                        mUserHeadImage.setImageBitmap(bitmap);
//                    }
//                    return;
//                }
//                break;
//            default:
//                break;
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PhotoUtil.NONE){
            return;
        }
        // 拍照
        if (requestCode == PhotoUtil.PHOTOGRAPH) {
            if (resultCode==RESULT_OK){
                // 设置文件保存路径这里放在根目录下
                File picture = null;
                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
                    picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                    if (!picture.exists()) {
                        picture = new File(Environment.getExternalStorageDirectory() + PhotoUtil.imageName);
                    }
                } else {
                    picture = new File(this.getFilesDir() + PhotoUtil.imageName);
                    if (!picture.exists()) {
                        picture = new File(MainActivity.this.getFilesDir() + PhotoUtil.imageName);
                    }
                }
                Uri pictureUri= FileProvider.getUriForFile(this,"org.kbk.fileprovider", picture);
                // 生成一个地址用于存放剪辑后的图片
                path = PhotoUtil.getPath(this);
                if (TextUtils.isEmpty(path)) {
                    Log.e("photo", "随机生成的用于存放剪辑后的图片的地址失败");
                    return;
                }
                LogUtil.d("photo","剪切图 path = "+path);
//              Uri imageUri = UriPathUtils.getUri(this, path);
                Uri imageUri=Uri.parse(path);
//                mUserHeadImage.setImageURI(imageUri);
//                mUserHeadImage.setImageBitmap()
                PhotoUtil.startPhotoZoom(MainActivity.this, pictureUri, PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
//                requestCode=PhotoUtil.PHOTORESOULT;
                return;
            }
        }

//        if (data == null){
//            return;
//        }

        // 读取相册缩放图片
        if (requestCode == PhotoUtil.PHOTOZOOM) {

            path = PhotoUtil.getPath(this);// 生成一个地址用于存放剪辑后的图片
            if (TextUtils.isEmpty(path)) {
                LogUtil.e("photo", "随机生成的用于存放剪辑后的图片的地址失败");
                return;
            }
            LogUtil.d("photo","相册缩放图片 path = "+path);

//            Uri imageUri = UriPathUtils.getUri(this, path);
            Uri imageUri=Uri.parse(path);
            PhotoUtil.startPhotoZoom(MainActivity.this, data.getData(), PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH, imageUri);
//            requestCode=PhotoUtil.PHOTORESOULT;
            return;
        }
        // 处理结果
        if (requestCode == PhotoUtil.PHOTORESOULT) {
            /**
             * 在这里处理剪辑结果，可以获取缩略图，获取剪辑图片的地址。得到这些信息可以选则用于上传图片等等操作
             * */
            /**
             * 如，根据path获取剪辑后的图片
             */
            Bitmap bitmap = PhotoUtil.convertToBitmap(path,PhotoUtil.PICTURE_HEIGHT, PhotoUtil.PICTURE_WIDTH);
            if(bitmap != null){
//                tv2.setText(bitmap.getHeight()+"x"+bitmap.getWidth()+"图");
                mUserHeadImage.setImageBitmap(bitmap);
            }

//            Bitmap bitmap2 = PhotoUtil.convertToBitmap(path,120, 120);
//            if(bitmap2 != null){
//                tv1.setText(bitmap2.getHeight()+"x"+bitmap2.getWidth()+"图");
//                smallImg.setImageBitmap(bitmap2);
//            }

//			Bundle extras = data.getExtras();
//			if (extras != null) {
//				Bitmap photo = extras.getParcelable("data");
//				ByteArrayOutputStream stream = new ByteArrayOutputStream();
//				photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);// (0-100)压缩文件
//				InputStream isBm = new ByteArrayInputStream(stream.toByteArray());
//			}

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void openCamera(){
        String imageName=mUser.getUsername()+"_head.jpg";
        //通过Context.getExternalCacheDir()方法可以获取到当前应用缓存数据
        //SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据.
        File ouputImage=new File(getExternalCacheDir(),imageName);
        try{
            if (ouputImage.exists()){
                ouputImage.delete();
            }
            ouputImage.createNewFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        //将file对象转换为URI对象
        if (Build.VERSION.SDK_INT>=24){//判断Android SDK版本号
            //7.0之后的系统，不允许直接使用本地真实路径的uri，会抛出错误
            //FileProvider是特殊的内容提取器（需要注册）
            // 可以将uri封装，共享给外部，第二个参数可以是任意唯一的字符串
            imageUri= FileProvider.getUriForFile(
                    MainActivity.this,"com.example.keepbookkeeping.fileprovider",ouputImage);
        }else {//如果低于7.0,
            imageUri= Uri.fromFile(ouputImage);
        }
        PhotoUtil.photograph(MainActivity.this);
//        //启动相机程序
//        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
//        //将拍摄的照片存储在SDcard的时候
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
//        startActivityForResult(intent,TAKE_PHOTO);
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
            LogUtil.d("rxbus","发送 ChangeFragmentTypeEvent:");
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

     private NavigationView.OnNavigationItemSelectedListener mNavListener=new NavigationView.OnNavigationItemSelectedListener() {
         @Override
         public boolean onNavigationItemSelected(@NonNull MenuItem item) {
             switch (item.getItemId()){
                 case R.id.change_user:
                     ToastUtil.success("切换账号");
                     LoginActivity.startLoginActivity(MainActivity.this);
                     finish();
                     break;
                 case R.id.setting:
                     ToastUtil.success("设置");
                     break;
                 case R.id.about:
                     ToastUtil.success("关于");
                     break;
                 case R.id.help:
                     ToastUtil.success("帮助");
                     break;
                 case R.id.exit:
                     ToastUtil.success("退出");
                     Process.killProcess(Process.myPid());
                     break;
                 default:
                     break;
             }
             return true;
         }
     };

    private View.OnClickListener mUserHeadImageListener=new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if (false){
                 //当前存在用户时，更改图片mUser!=null
                 showSelectPicDialog();
             }else {
                 //不存在用户
                 LoginActivity.startLoginActivity(MainActivity.this);
                 finish();
             }
         }
     };

    private void showSelectPicDialog(){
        if (mSelectPicDialog==null){
            initSelectPicDialog();
        }
        mSelectPicDialog.show();
    }

    public User getUser(){
        return mUser;
    }

    public static void startMainActivity(Context context){
        Intent intent=new Intent(context,MainActivity.class);
        context.startActivity(intent);
    }

    public static void startMainActivity(Context context,Bundle bundle){
        Intent intent=new Intent(context,MainActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

}
