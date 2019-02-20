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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.AddDataViewPagerAdapter;
import com.example.keepbookkeeping.bean.DataTypeBean;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.events.ChangeDataTypeEvent;
import com.example.keepbookkeeping.events.NotifyBillListEvent;
import com.example.keepbookkeeping.events.NotifyFormListEvent;
import com.example.keepbookkeeping.ui.SearchEditText;
import com.example.keepbookkeeping.ui.SimpleDatePickerDialog;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.BillTableUtil;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.DataTypeTableUtil;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.SharedPreferencesUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.Arrays;
import java.util.List;

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
    public static final int TYPE_OUTCOME=0;
    public static final int TYPE_INCOME=1;

    public static final String TheLastSaveBillTypeKey="save_bill_key";

    private DatePickerDialog mDatePickerDialog;

    private String mAddDataDescriptionStr;
    private String mAddDataBillTypeStr=SharedPreferencesUtil.getString(TheLastSaveBillTypeKey);

    android.support.v7.app.AlertDialog mDescriptionDialog;
    android.support.v7.app.AlertDialog mCancelDialog;

    private int type;
    private static final int TYPE_ADD_DATA=0;
    private static final int TYPE_UPDATE_DATA=1;

    SingleDataBean mBean;
    private int mUpdateId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);
        ButterKnife.bind(this);

        initData();
        initButton();
        initViewPager();
        initKeyboard();
        initRxBusEvent();
        initDialog();

        if (mBean!=null){
            refreshRadioAndPager();
            mAddDataChooseText.setText(mBean.getTypeName());
            mAddDataChooseImage.setImageResource(DataTypeTableUtil.getImageId(mBean.getTypeName()));
        }
    }

    private void initData(){
        Bundle bundle=getIntent().getExtras();
        if (bundle!=null){
            type=TYPE_UPDATE_DATA;
            mBean=(SingleDataBean) bundle.getSerializable("bean");
            mUpdateId=mBean.getId();
            mAddDataType=mBean.getType();
            mInputMoneyEditText.setText(String.valueOf(mBean.getMoney()));
            mAddDataSelectDate.setText(DateUtil.dateToString(mBean.getDate()));
            mAddDataBillTypeStr=mBean.getBillName();
            mAddDataDescriptionStr=mBean.getDescription();
        }else {
            type=TYPE_ADD_DATA;
            mAddDataType= TYPE_OUTCOME;
            mAddDataSelectDate.setText(DateUtil.getCurrentYearMonthDay());
        }
    }

    private void initButton(){
        mAddDataRadioGroup.setOnCheckedChangeListener(mRadioGroupListener);
        if (mAddDataType==TYPE_OUTCOME){
            mAddDataRadioGroup.check(R.id.add_data_radio_btn_outcome);
        }else {
            mAddDataRadioGroup.check(R.id.add_data_radio_btn_income);
        }

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

    private void initDialog(){
        mDatePickerDialog=new SimpleDatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mAddDataSelectDate.setText(String.format(" %d-%d-%d",year,monthOfYear+1,dayOfMonth));
                KeyBoardUtil.showSoftKeyBoard(mInputMoneyEditText,AddDataActivity.this);
                mInputMoneyEditText.requestFocus();
            }
        }, DateUtil.getCurrentYear(),DateUtil.getCurrentMonth(),DateUtil.getCurrentDay(),mInputMoneyEditText);
        mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        mDescriptionDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        View descriptionDialogView = View.inflate(this, R.layout.add_data_description_dialog, null);
        final TextView numText = descriptionDialogView.findViewById(R.id.description_dialog_num);
        final SearchEditText inputEdit = descriptionDialogView.findViewById(R.id.description_dialog_edit);
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
        mDescriptionDialog.setView(descriptionDialogView);
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

        mCancelDialog = new android.support.v7.app.AlertDialog.Builder(this).create();
        View cancelDialogView = View.inflate(this, R.layout.add_data_cancel_dialog, null);
        mCancelDialog.setView(cancelDialogView);
        mCancelDialog.setButton(AlertDialog.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        mCancelDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    public static void startAddDataActivity(Context context){
        Intent intent=new Intent(context,AddDataActivity.class);
        context.startActivity(intent);
    }

    public static void startAddDataActivity(Context context,Bundle bundle){
        Intent intent=new Intent(context,AddDataActivity.class);
        intent.putExtras(bundle);
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
                    refreshChooseImageAndText(DataTypeTableUtil.getFirstOutcomeDataTypeBean());
                    break;
                case R.id.add_data_radio_btn_income:
                    mAddDataType=TYPE_INCOME;
                    refreshChooseImageAndText(DataTypeTableUtil.getFirstIncomeDataTypeBean());
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
                    cancel();
                    break;
                case R.id.add_data_confirm:
                    ToastUtil.success("confirm");
                    confirmData();
                    break;
                case R.id.add_data_select_date:
                    showDatePickerDialog();
                    break;
                case R.id.add_data_select_bill:
                    if (mAddDataType==TYPE_OUTCOME){
                        showBillTypeAlertDialog(BillTableUtil.getBillName(BillTableUtil.TYPE_ALL));
                    }else {
                        showBillTypeAlertDialog(BillTableUtil.getBillName(BillTableUtil.TYPE_ASSETS));
                    }
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
        if (checkDataIsNotNull()){
            SingleDataBean bean=new SingleDataBean(
                    mAddDataType,
                    Float.valueOf(mInputMoneyEditText.getText().toString()),
                    DateUtil.stringToDate(mAddDataSelectDate.getText().toString()),
                    mAddDataChooseText.getText().toString(),
                    mAddDataBillTypeStr,
                    mAddDataDescriptionStr);
            if (type==TYPE_ADD_DATA){
                AllDataTableUtil.insertSingleDataToAllData(bean);
            }else if (mUpdateId!=-1){
                AllDataTableUtil.updateDataById(bean,mUpdateId);
            }
            RxBus.getInstance().post(new NotifyBillListEvent(BillTableUtil.getTypeByBillName(mAddDataBillTypeStr)));
            RxBus.getInstance().post(new NotifyFormListEvent(0));
            finish();
        }else {
            ToastUtil.error("金额或账户不允许为空");
        }
    }

    public Boolean checkDataIsNotNull(){
        return !TextUtils.isEmpty(mInputMoneyEditText.getText().toString()) &&
                !TextUtils.isEmpty(mAddDataBillTypeStr);
    }

    private void showDatePickerDialog(){
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
        mDescriptionDialog.show();
    }

    private void showCancelDialog(){
        mCancelDialog.show();
    }

    private void cancel(){
        if (checkDataIsNotNull() || !TextUtils.isEmpty(mAddDataDescriptionStr)){
            showCancelDialog();
        }else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        cancel();
    }
}
