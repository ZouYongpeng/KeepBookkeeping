package com.example.keepbookkeeping.activities;

import android.content.Intent;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.bean.BmobBean.User;
import com.example.keepbookkeeping.ui.KBKMathPasswordView;
import com.example.keepbookkeeping.utils.SharedPreferencesUtil;
import com.example.keepbookkeeping.utils.StringUtil;
import com.example.keepbookkeeping.utils.UserUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class LaunchActivity extends AppCompatActivity {


    @BindView(R.id.launch_password_view)
    KBKMathPasswordView mMathPasswordView;

    @BindView(R.id.password_tip)
    TextView mPasswordTipText;

    public static final String SP_LAUNCH_PASSWORD_KEY="sp_launch_password";
    public static final String SP_NEED_INPUT_PASSWORD_KEY="sp_need_input_password";
    public static final String SP_NEED_FINGER_PASSWORD_KEY="sp_need_finger_password";

    private int mType;
    private static final int TYPE_SET_PASSWORD_FIRST=0;
    private static final int TYPE_SET_PASSWORD_SECOND=1;
    private static final int TYPE_INPUT_PASSWORD=2;
    private static final int TYPE_FINGER_PASSWORD=3;

    private String mPassword = "";
    private static final int PASSWORD_LENGTH = 4;
    private static final int PASSWORD_TIME = 5;

    private FingerprintManagerCompat mFingerprintManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Bmob.initialize(this,"2424d64e264f226066a4c80e924b2433");
        ButterKnife.bind(this);
        if (SharedPreferencesUtil.getBoolean(SP_NEED_INPUT_PASSWORD_KEY,true)){
            checkType();
            mMathPasswordView.setOnPasswordInputCompleteListener(new KBKMathPasswordView.OnPasswordInputCompleteListener() {
                @Override
                public void OnComplete(String password) {
                    if (!TextUtils.isEmpty(password) || password.length()==PASSWORD_LENGTH){
                        checkPassword(password);
                    }
                }
            });
        }
        if(SharedPreferencesUtil.getBoolean(SP_NEED_FINGER_PASSWORD_KEY,false)){
            //需要指纹解锁
            mType=TYPE_FINGER_PASSWORD;
            mFingerprintManager=FingerprintManagerCompat.from(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mType==TYPE_FINGER_PASSWORD &&
                mFingerprintManager!=null &&
                mFingerprintManager.isHardwareDetected() &&
                mFingerprintManager.hasEnrolledFingerprints()){
                mFingerprintManager.authenticate(
                        null,
                        0,
                        new CancellationSignal(),
                        mFingerCallback,
                        mFingerHandler);
        }
    }

    private void checkType(){
        if (!TextUtils.isEmpty(SharedPreferencesUtil.getString(SP_LAUNCH_PASSWORD_KEY))){
            mType=TYPE_INPUT_PASSWORD;
        }else {
            mPasswordTipText.setText("请先设置密码");
            mType=TYPE_SET_PASSWORD_FIRST;
        }
    }

    private void checkPassword(String password){
        if (mType==TYPE_SET_PASSWORD_FIRST){
            mPassword=password;
            mType=TYPE_SET_PASSWORD_SECOND;
            mPasswordTipText.setText("请再次输入密码");
        }else if (mType==TYPE_SET_PASSWORD_SECOND){
            if (TextUtils.equals(mPassword,password)){
                //两次输入的密码相同
                SharedPreferencesUtil.putString(SP_LAUNCH_PASSWORD_KEY, StringUtil.stringMD5(mPassword));
                startMainActivity();
            }else {
                mType=TYPE_SET_PASSWORD_FIRST;
                mPasswordTipText.setText("两次密码输入不一致，请重新输入");
            }
        }else if (mType==TYPE_INPUT_PASSWORD){
            if (TextUtils.equals(StringUtil.stringMD5(password),SharedPreferencesUtil.getString(SP_LAUNCH_PASSWORD_KEY))){
                startMainActivity();
            }else {
                mPasswordTipText.setText("密码不正确，请重新输入");
            }
        }
    }

    private void startMainActivity(){
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
    }

    private FingerprintManagerCompat.AuthenticationCallback mFingerCallback=new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            //验证错误时，回调该方法。当连续验证5次错误时，将会走onAuthenticationFailed()方法
            mFingerHandler.obtainMessage(1,errMsgId,0).sendToTarget();
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            //验证成功时，回调该方法。fingerprint对象不能再验证
            mFingerHandler.obtainMessage(2).sendToTarget();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            //验证失败时，回调该方法。fingerprint对象不能再验证并且需要等待一段时间才能重新创建指纹管理对象进行验证
            mFingerHandler.obtainMessage(3).sendToTarget();
        }
    };

    private Handler mFingerHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    //验证错误
                    //todo 界面处理
                    handleErrorCode(msg.arg1);
                    break;
                case 2:
                    //验证成功
                    //todo 界面处理
                    startMainActivity();
                    break;
                case 3:
                    //验证失败
                    //todo 界面处理
                    mPasswordTipText.setText("指纹有误，请稍后再试");
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    /**
     * 对应不同的错误，可以有不同的操作
     */
    private void handleErrorCode(int code) {
        switch (code) {
            case FingerprintManager.FINGERPRINT_ERROR_CANCELED:
                //todo 指纹传感器不可用，该操作被取消
                mPasswordTipText.setText("指纹传感器不可用");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_HW_UNAVAILABLE:
                //todo 当前设备不可用，请稍后再试
                mPasswordTipText.setText("当前设备不可用，请稍后再试");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_LOCKOUT:
                //todo 由于太多次尝试失败导致被锁，该操作被取消
                mPasswordTipText.setText("失败次数过多，请稍后再试");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_NO_SPACE:
                //todo 没有足够的存储空间保存这次操作，该操作不能完成
                mPasswordTipText.setText("存储空间不足，请稍后再试");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_TIMEOUT:
                //todo 操作时间太长，一般为30秒
                mPasswordTipText.setText("操作超时，请稍后再试");
                break;
            case FingerprintManager.FINGERPRINT_ERROR_UNABLE_TO_PROCESS:
                //todo 传感器不能处理当前指纹图片
                mPasswordTipText.setText("无法处理当前指纹，请稍后再试");
                break;
            default:
                break;
        }
    }
}
