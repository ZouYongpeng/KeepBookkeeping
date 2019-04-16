package com.example.keepbookkeeping.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.BmobBean.User;
import com.example.keepbookkeeping.bean.BmobBean.UserInfo;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.ToastUtil;
import com.example.keepbookkeeping.utils.UserUtil;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG="register";

    @BindView(R.id.register_back)
    ImageView mRegisterBackImage;

    @BindView(R.id.register_user_head)
    ImageView mRegisterHeadImage;

    @BindView(R.id.register_user_name)
    protected SearchEditText mRegisterName;

    @BindView(R.id.register_user_password)
    protected SearchEditText mRegisterPass;

    @BindView(R.id.register_user_password_again)
    protected SearchEditText mRegisterPassAgain;

    @BindView(R.id.register_button)
    protected Button mRegisterButton;

    @BindView(R.id.goto_login)
    protected TextView mGotoLogin;

    @BindView(R.id.goto_local)
    TextView mGotoLocal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
//        Bmob.initialize(this, "2424d64e264f226066a4c80e924b2433");

    }

    public void userRegister(){
        closeInput();
        String registerName=mRegisterName.getText().toString();
        String registerPass=mRegisterPass.getText().toString();
        String registerPassAgain=mRegisterPassAgain.getText().toString();
        String registerAvatar=null;

        if (TextUtils.isEmpty(registerName)||TextUtils.isEmpty(registerPass)||TextUtils.isEmpty(registerPassAgain)){
            ToastUtil.error(getString(R.string.error_register_input));
            openInput();
            return;
        }

        /*使用Pattern类，编译正则表达式后创建一个匹配模式.
         * name:^[a-zA-Z0-9]{3,16}$   长度为3-16的字母+数字
         * pass:^[a-zA-Z0-9]{6,18}$   长度为6-16的字母+数字*/
        Pattern namePattern = Pattern.compile(getString(R.string.pattern_register_name));
        Pattern passPattern = Pattern.compile(getString(R.string.pattern_register_password));

        /*matches() 最常用方法:尝试对整个目标字符展开匹配检测,也就是只有整个目标字符串完全匹配时才返回真值*/
        if(!namePattern.matcher(registerName).matches()){
            ToastUtil.error(getString(R.string.error_register_name));
            mRegisterName.setText("");
            openInput();
            return;
        }
        if(!passPattern.matcher(registerPass).matches()){
            ToastUtil.error(getString(R.string.error_register_password));
            mRegisterPass.setText("");
            mRegisterPassAgain.setText("");
            openInput();
            return;
        }
        if (!registerPass.equals(registerPassAgain)){
            ToastUtil.error(getString(R.string.error_register_password_again));
            mRegisterPassAgain.setText("");
            openInput();
            return;
        }

        register(registerName,registerPass,"");
    }

    public void register(final String name, final String pass, final String avatar){
        LogUtil.d(TAG,"register: start!");
        if (!name.isEmpty()&& !pass.isEmpty()){
            BmobUser user=new BmobUser();
            user.setUsername(name);
            user.setPassword(pass);
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e==null){
                        LogUtil.d(TAG,"done: UserTool register success");
                        registerSuccess(user.getObjectId(),name,pass,avatar);
                    }else {
                        LogUtil.d(TAG,"done: UserTool register failure"+e.toString());
                        ToastUtil.error("该用户已存在或服务器连接失败");
                        openInput();
                    }
                }
            });
        }
    }

    public void registerSuccess(final String id,final String name,String pass, final String avatar){
        Log.d("register", "registerSuccess");
        UserInfo userInfo=new UserInfo();
        userInfo.setId(id);
        userInfo.setName(name);
        userInfo.setAvatar(avatar);
        userInfo.setAge(0);
        userInfo.setSex("保密");
        userInfo.setPhone("未填写电话号码");
        userInfo.setMail("未填写邮箱");
        userInfo.setLocal("未知");
        userInfo.setIntroduction("这个家伙很懒，什么都不写");
        userInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e==null){
                    LogUtil.d(TAG,"创建数据成功");
                }else{
                    LogUtil.d(TAG,"失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });

        UserUtil.getInstance().login(name, pass, new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e==null){
                    //登录成功
                    //ToastUtil.success("登陆成功");
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user",(User)o);
                    MainActivity.startMainActivity(RegisterActivity.this,bundle);
                    AllDataTableUtil.changeAllLocalDataUserId();
                    finish();
                }else {
                    LogUtil.d(TAG,e.getMessage() + "(" + e.getErrorCode() + ")");
                    ToastUtil.error(e.getMessage() + "(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    /*关闭输入*/
    public void closeInput(){
        mRegisterName.setEnabled(false);
        mRegisterPass.setEnabled(false);
        mRegisterPassAgain.setEnabled(false);
        mRegisterButton.setEnabled(false);
        mRegisterButton.setText(getString(R.string.registering));
    }

    /*开启输入*/
    public void openInput(){
        mRegisterName.setEnabled(true);
        mRegisterPass.setEnabled(true);
        mRegisterPassAgain.setEnabled(true);
        mRegisterButton.setEnabled(true);
        mRegisterButton.setText("注册");
    }

    @OnClick({R.id.register_back, R.id.register_user_head, R.id.register_button, R.id.goto_login,R.id.goto_local})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_back:
                finish();
                break;
            case R.id.register_user_head:
                break;
            case R.id.register_button:
                userRegister();
                break;
            case R.id.goto_login:
                finish();
                break;
            case R.id.goto_local:
                BmobUser.logOut();
                MainActivity.startMainActivity(this);
                finish();
                break;
            default:
                break;
        }
    }

    public static void startRegisterActivity(Context context){
        Intent intent=new Intent(context,RegisterActivity.class);
        context.startActivity(intent);
    }
}
