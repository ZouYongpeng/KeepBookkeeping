package com.example.keepbookkeeping.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.BmobBean.User;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.ToastUtil;
import com.example.keepbookkeeping.utils.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG="login";
    @BindView(R.id.login_back)
    ImageView mLoginBack;

    @BindView(R.id.login_user_head)
    CircleImageView mLoginUserHead;

    @BindView(R.id.login_user_name)
    SearchEditText mEditUserName;

    @BindView(R.id.login_user_password)
    SearchEditText mEditUserPass;

    @BindView(R.id.login_button)
    Button mLoginButton;

    @BindView(R.id.goto_register)
    TextView mToRegister;

    @BindView(R.id.goto_local)
    TextView mToLocal;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("登录中......");
        initEditListener();
    }

    private void initEditListener(){
        mEditUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mEditUserPass.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    public void userLogin(){
        //判断用户和密码是否为空
        final String userName=mEditUserName.getText().toString();
        final String userPass=mEditUserPass.getText().toString();
        if (userName.isEmpty() || userName.length()==0){
            ToastUtil.error("用户名不能为空");
            return;
        }
        if (userPass.isEmpty() || userPass.length()==0){
            ToastUtil.error("密码不能为空");
            return;
        }
        mLoginButton.setEnabled(false);
        mLoginButton.setText("登陆中");
        dialog.show();

        UserUtil.getInstance().login(userName, userPass, new LogInListener() {
            @Override
            public void done(Object o, BmobException e) {
                if (e==null){
                    //登录成功
                    //ToastUtil.success("登陆成功");
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user",(User)o);
                    MainActivity.startMainActivity(LoginActivity.this,bundle);
                    AllDataTableUtil.changeAllLocalDataUserId();
                    finish();
                }else {
                    LogUtil.d(TAG,e.getMessage() + "(" + e.getErrorCode() + ")");
                    ToastUtil.error(e.getMessage() + "(" + e.getErrorCode() + ")");
                    dialog.dismiss();
                    mLoginButton.setEnabled(true);
                    mLoginButton.setText("登陆");
                    mEditUserPass.setText("");
                }
            }
        });
    }

    @OnClick({R.id.login_back, R.id.login_user_head, R.id.login_button, R.id.goto_register,R.id.goto_local})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_back:
                //ToastUtil.success("返回");
                User user= UserUtil.getInstance().getCurrentUser();
                if (user!=null){
                    Bundle bundle=new Bundle();
                    bundle.putSerializable("user",user);
                    MainActivity.startMainActivity(this,bundle);
                    finish();
                }else {
                    MainActivity.startMainActivity(this);
                    finish();
                }
                break;
            case R.id.login_user_head:
                //ToastUtil.success("添加照片");
                break;
            case R.id.login_button:
                userLogin();
                break;
            case R.id.goto_register:
                //ToastUtil.success("注册");
                RegisterActivity.startRegisterActivity(this);
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

    public static void startLoginActivity(Context context){
        Intent intent=new Intent(context,LoginActivity.class);
        context.startActivity(intent);
    }
}
