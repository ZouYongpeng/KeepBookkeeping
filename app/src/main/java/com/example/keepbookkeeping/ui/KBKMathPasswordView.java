package com.example.keepbookkeeping.ui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
/**
 * @author 邹永鹏
 * @date 2019/2/24
 * @description :
 */
public class KBKMathPasswordView extends LinearLayout {

    Context mContext;
    LinearLayout showNumberLayout;
    ImageView[] mCircularPoint=new ImageView[4];
    GridView mGridView;
    LayoutInflater mInflater;
    private String password="";
    OnPasswordInputCompleteListener onPasswordInputCompleteListener;
    String showButtonText;

    PhoneNumberAdapter phoneNumberAdapter=new PhoneNumberAdapter();

    public KBKMathPasswordView(Context context) {
        super(context);
        this.mContext = context;
        initView();
    }

    public KBKMathPasswordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initView();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setOnPasswordInputCompleteListener(OnPasswordInputCompleteListener onPasswordInputCompleteListener) {
        this.onPasswordInputCompleteListener = onPasswordInputCompleteListener;
    }

    private void initView(){
        mInflater=LayoutInflater.from(mContext);
        LayoutInflater.from(mContext).inflate(R.layout.common_password_view,this);
        showNumberLayout=findViewById(R.id.layout);
        for (int i=0;i<4;i++){
            mCircularPoint[i]=(ImageView) showNumberLayout.getChildAt(i);
            mCircularPoint[i].setEnabled(false);
            mCircularPoint[i].setTag(i);
        }
        setHideImageView();
        mGridView=findViewById(R.id.phone_number);
        mGridView.setAdapter(phoneNumberAdapter);
        mGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mGridView.setOnItemClickListener(new PhoneNumberItemClickListener());
    }

    public void setHideImageView() {
        int length = getPassword().length();
        for (int i = 0; i < length; i++) {
            mCircularPoint[i].setEnabled(true);
        }
        for (int i = 0; i < 4 - length; i++) {
            mCircularPoint[3 - i].setEnabled(false);
        }
    }

    public static interface OnPasswordInputCompleteListener{
        void OnComplete(String password);
    }

    public class PhoneNumberAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (position<9){
                convertView=mInflater.inflate(R.layout.phone_number_layout,null);
                TextView textView=convertView.findViewById(R.id.phone_number_view);
                textView.setText(String.valueOf(position+1));
            }else if (position==9){
                convertView=mInflater.inflate(R.layout.phone_number_empty_layout,null);
            }else if (position==10){
                convertView=mInflater.inflate(R.layout.phone_number_layout,null);
                TextView textView=convertView.findViewById(R.id.phone_number_view);
                textView.setText("0");
            }else if (position==11){
                convertView=mInflater.inflate(R.layout.phone_number_delete_layout,null);
            }
            return convertView;
        }
    }

    private class PhoneNumberItemClickListener implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position<9){
                changePassword(String.valueOf(position+1));
            }else if (position==10){
                changePassword("0");
            }else if (position==11){
                deletePassword();
            }
        }
    }

    private void changePassword(String number){
        String passwordString=getPassword();
        if (TextUtils.isEmpty(passwordString) || passwordString.length()<4){
            setPassword(passwordString+number);
            setHideImageView();
            if (passwordString.length()==3){
                //回调密码输入完成
                onPasswordInputCompleteListener.OnComplete(password);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            cleanAllPassword();
        }
    };

    /**
     * 删除输入的
     */
    private void deletePassword() {
        String passwordString = getPassword();
        if (!TextUtils.isEmpty(passwordString) || passwordString.length() > 0) {
            setPassword(passwordString.substring(0, passwordString.length() - 1));
            setHideImageView();
        }
    }

    public void cleanAllPassword() {
        setPassword("");
        setHideImageView();
    }

}
