package com.example.keepbookkeeping.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.AllDataListAdapter;
import com.example.keepbookkeeping.bean.BillApartBean;
import com.example.keepbookkeeping.events.NotifyBillListEvent;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.BillTableUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.StringUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;

public class BillDetailsActivity extends AppCompatActivity {

    private static final String TAG = "BillDetails";

    @BindView(R.id.bill_detail_back_btn)
    ImageView mBackBtn;
    @BindView(R.id.bill_detail_name)
    TextView mBillDetailName;
    @BindView(R.id.bill_detail_set)
    TextView mBillDetailSet;
    @BindView(R.id.bill_detail_total_money_title)
    TextView mBillDetailTotalMoneyTitle;
    @BindView(R.id.bill_detail_total_money)
    TextView mBillDetailTotalMoney;
    @BindView(R.id.bill_detail_income_text)
    TextView mBillDetailIncomeMoneyText;
    @BindView(R.id.bill_detail_income_money)
    TextView mBillDetailIncomeMoney;
    @BindView(R.id.bill_detail_outcome_text)
    TextView mBillDetailOutcomeMoneyText;
    @BindView(R.id.bill_detail_outcome_money)
    TextView mBillDetailOutcomeMoney;
    @BindView(R.id.bill_detail_add_data)
    TextView mBillDetailAddData;
    @BindView(R.id.bill_detail_recycler)
    RecyclerView mBillDetailRecycler;
    @BindView(R.id.bill_detail_debt_money)
    TextView mBillDetailDebtMoney;
    @BindView(R.id.bill_detail_debt_title)
    TextView mBillDetailDebtTitle;

    private BillApartBean mBillApartBean;

    AlertDialog mAddBillDialog;

    CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_details);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            mBillApartBean = (BillApartBean) bundle.getSerializable("bean");
            LogUtil.d(TAG, mBillApartBean.toString());
        }
        initView();
    }

    private void initView() {
        initToolBar();
        initBanner();
        initRecyclerView();
        initAddBillDialog();
    }

    public static void startBillDetailsActivity(Context context, Bundle bundle) {
        Intent intent = new Intent(context, BillDetailsActivity.class);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void initToolBar() {
        mBillDetailName.setText(mBillApartBean.getName());
    }

    private void initBanner() {
        float initialMoney = BillTableUtil.getInitialCountByBillName(mBillApartBean.getName());
        float incomeMoney = AllDataTableUtil.getMoneyByBillName(mBillApartBean.getName(), AllDataTableUtil.TYPE_INCOME);
        float outcomeMoney = AllDataTableUtil.getMoneyByBillName(mBillApartBean.getName(), AllDataTableUtil.TYPE_OUTCOME);
        if (mBillApartBean.getType() == 0) {
            //资产
            mBillDetailOutcomeMoneyText.setVisibility(View.VISIBLE);
            mBillDetailOutcomeMoney.setVisibility(View.VISIBLE);
            mBillDetailIncomeMoneyText.setVisibility(View.VISIBLE);
            mBillDetailIncomeMoney.setVisibility(View.VISIBLE);
            mBillDetailDebtTitle.setVisibility(View.GONE);
            mBillDetailDebtMoney.setVisibility(View.GONE);
            mBillDetailTotalMoneyTitle.setText("余额");
            mBillDetailTotalMoney.setText(String.valueOf(initialMoney + incomeMoney - outcomeMoney));
            mBillDetailIncomeMoney.setText(String.valueOf(incomeMoney));
            mBillDetailOutcomeMoney.setText(String.valueOf(outcomeMoney));
        } else {
            //负债
            mBillDetailOutcomeMoneyText.setVisibility(View.GONE);
            mBillDetailOutcomeMoney.setVisibility(View.GONE);
            mBillDetailIncomeMoneyText.setVisibility(View.GONE);
            mBillDetailIncomeMoney.setVisibility(View.GONE);
            mBillDetailDebtTitle.setVisibility(View.VISIBLE);
            mBillDetailDebtMoney.setVisibility(View.VISIBLE);
            mBillDetailTotalMoneyTitle.setText("剩余额度");
            mBillDetailTotalMoney.setText(String.valueOf(initialMoney - outcomeMoney));
            mBillDetailDebtTitle.setText("已用");
            mBillDetailDebtMoney.setText(String.valueOf(outcomeMoney));
        }

    }

    private void initRecyclerView() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        mBillDetailRecycler.setLayoutManager(manager);
        mBillDetailRecycler.setAdapter(new AllDataListAdapter(
                this, AllDataListAdapter.TYPE_QUERY_BILL_NAME, mBillApartBean.getName()));
    }

    public void initAddBillDialog() {
        mAddBillDialog = new AlertDialog.Builder(this).create();
        View view = View.inflate(this, R.layout.fragment_bill_add_dialog, null);
        final ImageView imageView = view.findViewById(R.id.add_bill_dialog_image);
        final String[] strings = BillTableUtil.getCannotChangeBillName();
        final Spinner spinner = view.findViewById(R.id.add_bill_dialog_spinner);
        TextView typeText = view.findViewById(R.id.add_bill_dialog_type_text);
        final TextView tipText = view.findViewById(R.id.add_bill_dialog_tip);
        final EditText billNameEdit = view.findViewById(R.id.add_bill_dialog_name_edit);
        final EditText moneyEdit = view.findViewById(R.id.add_bill_dialog_money_edit);
        final EditText descriptionEdit = view.findViewById(R.id.add_bill_dialog_description_edit);
        Button cancelBtn = view.findViewById(R.id.add_bill_dialog_cancel_btn);
        final Button confirmBtn = view.findViewById(R.id.add_bill_dialog_ok_btn);
        final int imageId = mBillApartBean.getImageId();
        billNameEdit.setText(mBillApartBean.getName());
        imageView.setImageResource(imageId);
        if (mBillApartBean.getCanChange() == 1) {
            //可以修改
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strings);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(StringUtil.getPositionInStrings(strings, BillTableUtil.getBillNameByImageId(mBillApartBean.getImageId())));
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    imageView.setImageResource(BillTableUtil.getImageIdByBillName(strings[position]));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    spinner.setSelection(StringUtil.getPositionInStrings(strings, mBillApartBean.getName()));
                }
            });
//            confirmBtn.setEnabled(false);
//            tipText.setText("已存在");
//            tipText.setVisibility(View.VISIBLE);
            billNameEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (BillTableUtil.billNameIsExist(billNameEdit.getText().toString().trim())) {
                        tipText.setText("已存在");
                        tipText.setVisibility(View.VISIBLE);
                        confirmBtn.setEnabled(false);
                    } else if (TextUtils.isEmpty(billNameEdit.getText().toString().trim())) {
                        tipText.setText("不为空");
                        tipText.setVisibility(View.VISIBLE);
                        confirmBtn.setEnabled(false);
                    } else {
                        tipText.setVisibility(View.INVISIBLE);
                        confirmBtn.setEnabled(true);
                    }
                }
            });
        } else {
            //不可修改
            spinner.setVisibility(View.INVISIBLE);
            typeText.setText(mBillApartBean.getName());
            typeText.setVisibility(View.VISIBLE);
            billNameEdit.setEnabled(false);
            if (mBillApartBean.getInitialCount() != 0.0) {
                moneyEdit.setText(String.valueOf(mBillApartBean.getInitialCount()));
            }
            confirmBtn.setEnabled(false);
            moneyEdit.requestFocus();
            moneyEdit.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (TextUtils.isEmpty(moneyEdit.getText().toString().trim())) {
                        confirmBtn.setEnabled(false);
                    } else {
                        confirmBtn.setEnabled(true);
                    }
                }
            });
        }
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddBillDialog.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.success("确定");
                if (mBillApartBean.getCanChange() == 0) {
                    //不可修改的
                    int id = mBillApartBean.getId();
                    String description = descriptionEdit.getText().toString().trim();
                    String count = moneyEdit.getText().toString().trim();
                    BillTableUtil.updateCannotChangeDataById(id, count, description);
                    RxBus.getInstance().post(new NotifyBillListEvent(mBillApartBean.getType()));
                    mAddBillDialog.dismiss();
                } else {
                    int id = mBillApartBean.getId();
                    String billName = (String) spinner.getSelectedItem();
                    int type = BillTableUtil.getTypeByBillName(billName);
                    int imageId = BillTableUtil.getImageIdByBillName(billName);
                    String newName = billNameEdit.getText().toString().trim();
                    String description = descriptionEdit.getText().toString().trim();
                    String count = moneyEdit.getText().toString().trim();
                    float initialCount = TextUtils.isEmpty(count) ? 0 : Float.valueOf(count);
                    BillApartBean bean = new BillApartBean(id, type, imageId, newName, description, initialCount, 1);
                    BillTableUtil.updateCanChangeDataById(bean, mBillApartBean.getName());
                    RxBus.getInstance().post(new NotifyBillListEvent(1 - bean.getType()));
                    RxBus.getInstance().post(new NotifyBillListEvent(bean.getType()));
                    mBillApartBean = bean;
                    mAddBillDialog.dismiss();
                    initView();
                }
            }
        });
        mAddBillDialog.setView(view);
    }

    @OnClick({R.id.bill_detail_back_btn, R.id.bill_detail_set, R.id.bill_detail_add_data})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bill_detail_back_btn:
                finish();
                break;
            case R.id.bill_detail_set:
                mAddBillDialog.show();
                break;
            case R.id.bill_detail_add_data:
                break;
        }
    }
}
