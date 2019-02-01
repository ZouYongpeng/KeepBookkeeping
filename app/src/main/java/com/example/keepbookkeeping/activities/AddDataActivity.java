package com.example.keepbookkeeping.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.AddDataViewPagerAdapter;
import com.example.keepbookkeeping.bean.DataTypeBean;
import com.example.keepbookkeeping.events.ChangeDataTypeEvent;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.ui.SimpleDatePickerDialog;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.GetDataTypeUtil;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.SharedPreferencesUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

public class AddDataActivity extends AppCompatActivity {

    @BindView(R.id.add_data_view_pager)
    ViewPager mAddDataViewPager;

    @BindView(R.id.add_data_radio_group)
    RadioGroup mAddDataRadioGroup;

    @BindView(R.id.add_data_choose_image)
    ImageView mAddDataChooseImage;

    @BindView(R.id.add_data_choose_text)
    TextView mAddDataChooseText;

    @BindView(R.id.add_data_input_money)
    EditText mInputMoneyEditText;

    @BindView(R.id.add_data_cancel)
    ImageView mAddDataCancel;

    @BindView(R.id.add_data_confirm)
    ImageView mAddDataConfirm;

    @BindView(R.id.add_data_select_date)
    TextView mAddDataSelectDate;

    @BindView(R.id.add_data_select_bill)
    TextView mAddDataSelectBill;

    @BindView(R.id.add_data_input_description)
    TextView mAddDataInputDescription;

    private int mAddDataType;
    private static final int TYPE_OUTCOME=0;
    private static final int TYPE_INCOME=1;

    public static final String TheLastSaveBillTypeKey="save_bill_key";

    private DatePickerDialog mDatePickerDialog;

    private String mAddDataDescriptionStr;
    private String mAddDataBillTypeStr=SharedPreferencesUtil.getString(TheLastSaveBillTypeKey);

    android.support.v7.app.AlertDialog mDescriptionDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        ButterKnife.bind(this);

        initButton();
        initViewPager();
        initKeyboard();
        initRxBusEvent();
    }

    private void initButton(){
        mAddDataType= TYPE_OUTCOME;
        mAddDataRadioGroup.setOnCheckedChangeListener(mRadioGroupListener);
        mAddDataRadioGroup.check(R.id.add_data_radio_btn_outcome);

        mAddDataCancel.setOnClickListener(mImageClickListener);
        mAddDataConfirm.setOnClickListener(mImageClickListener);
        mAddDataSelectDate.setOnClickListener(mImageClickListener);
        mAddDataSelectBill.setOnClickListener(mImageClickListener);
        mAddDataInputDescription.setOnClickListener(mImageClickListener);

        if (!TextUtils.isEmpty(mAddDataBillTypeStr)){
            mAddDataSelectBill.setText(mAddDataBillTypeStr);
        }

    }

    private void initViewPager(){
        mAddDataViewPager.setAdapter(new AddDataViewPagerAdapter(this,5));
        mAddDataViewPager.setOnPageChangeListener(mViewPagerListener);
    }

    private void initKeyboard(){
        mInputMoneyEditText.setFocusable(true);
        mInputMoneyEditText.setFocusableInTouchMode(true);
        mInputMoneyEditText.requestFocus();
        //强制软键盘一直显示
        KeyBoardUtil.showSoftKeyBoard(mInputMoneyEditText,this);
    }

    private void initRxBusEvent(){
        RxBus.getInstance().toObservable(ChangeDataTypeEvent.class).subscribe(new Consumer<ChangeDataTypeEvent>() {
            @Override
            public void accept(ChangeDataTypeEvent changeDataTypeEvent) throws Exception {
                refreshChooseImageAndText(changeDataTypeEvent.getDataTypeBean());
            }
        });
    }

    public static void startAddDataActivity(Context context){
        Intent intent=new Intent(context,AddDataActivity.class);
        context.startActivity(intent);
    }

    private void refreshRadioAndPager(){
        mAddDataViewPager.setCurrentItem(mAddDataType);
        if (mAddDataType==TYPE_OUTCOME){
            mAddDataRadioGroup.check(R.id.add_data_radio_btn_outcome);
        }else {
            mAddDataRadioGroup.check(R.id.add_data_radio_btn_income);
        }
    }

    private void refreshChooseImageAndText(DataTypeBean bean){
        if (bean!=null){
            mAddDataChooseImage.setImageResource(bean.getImageId());
            mAddDataChooseText.setText(bean.getName());
        }
    }

    private RadioGroup.OnCheckedChangeListener mRadioGroupListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton radioButton=findViewById(group.getCheckedRadioButtonId());
            switch (radioButton.getId()){
                case R.id.add_data_radio_btn_outcome:
                    mAddDataType=TYPE_OUTCOME;
                    refreshChooseImageAndText(GetDataTypeUtil.getFirstOutcomeDataTypeBean());
                    break;
                case R.id.add_data_radio_btn_income:
                    mAddDataType=TYPE_INCOME;
                    refreshChooseImageAndText(GetDataTypeUtil.getFirstIncomeDataTypeBean());
                    break;
                default:
                    break;
            }
            refreshRadioAndPager();
        }
    };

    private ViewPager.OnPageChangeListener mViewPagerListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            mAddDataType=position;
            refreshRadioAndPager();
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    };

    private View.OnClickListener mImageClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.add_data_cancel:
                    ToastUtil.error("cancel");
                    Intent intent=new Intent(AddDataActivity.this,MainActivity.class);
                    startActivity(intent);
                    break;
                case R.id.add_data_confirm:
                    ToastUtil.success("confirm");
                    confirmData();
                    break;
                case R.id.add_data_select_date:
                    showDatePickerDialog();
                    break;
                case R.id.add_data_select_bill:
                    String[] billTypeStrings={"现金","微信","支付宝","银行卡"};
                    showBillTypeAlertDialog(billTypeStrings);
                    break;
                case R.id.add_data_input_description:
                    showInputDescriptionAlertDialog();
                    break;
                default:
                    break;
            }
        }
    };

    private void confirmData(){
        if (!TextUtils.isEmpty(mAddDataBillTypeStr)){
            SharedPreferencesUtil.putString(TheLastSaveBillTypeKey,mAddDataBillTypeStr);
        }
    }

    private void showDatePickerDialog(){
        mDatePickerDialog=new SimpleDatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mAddDataSelectDate.setText(String.format(" %d-%d-%d",year,monthOfYear+1,dayOfMonth));
                KeyBoardUtil.showSoftKeyBoard(mInputMoneyEditText,AddDataActivity.this);
                mInputMoneyEditText.requestFocus();
            }
        }, DateUtil.getCurrentYear(),DateUtil.getCurrentMonth(),DateUtil.getCurrentDay(),mInputMoneyEditText);
        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        mDatePickerDialog.show();

    }

    private void showBillTypeAlertDialog(final String[] strings){
        if (strings==null || strings.length==0){
            ToastUtil.error("你还没有账户呢");
            return;
        }
        int index=0;
        if (!TextUtils.isEmpty(mAddDataBillTypeStr)){
            index = Math.max(Arrays.binarySearch(strings,mAddDataBillTypeStr),0);
        }
        final android.support.v7.app.AlertDialog.Builder builder=new android.support.v7.app.AlertDialog.Builder(this);
        builder.setSingleChoiceItems(strings, index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAddDataBillTypeStr=strings[which];
                mAddDataSelectBill.setText(mAddDataBillTypeStr);
                dialog.dismiss();
            }
        }).create().show();
    }

    private void showInputDescriptionAlertDialog(){
        mDescriptionDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        View dialogView = View.inflate(this, R.layout.add_data_description_dialog, null);
        final TextView numText = dialogView.findViewById(R.id.description_dialog_num);
        final SearchEditText inputEdit = dialogView.findViewById(R.id.description_dialog_edit);
        if (!TextUtils.isEmpty(mAddDataDescriptionStr)) {
            inputEdit.setText(mAddDataDescriptionStr);
            inputEdit.setSelection(mAddDataDescriptionStr.length());
            numText.setText(mAddDataDescriptionStr.length()+"/20");
        }
        inputEdit.requestFocus();
        inputEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = inputEdit.getText().length();
                numText.setText(num + "/20");
            }
        });
        mDescriptionDialog.setView(dialogView);
        mDescriptionDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ToastUtil.success("确定");
                mAddDataDescriptionStr=inputEdit.getText().toString();
            }
        });
        mDescriptionDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        mDescriptionDialog.show();
    }
}
