package com.example.keepbookkeeping.bill;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.BillApartAdapter;
import com.example.keepbookkeeping.bean.BillApartBean;
import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.events.NotifyBillListEvent;
import com.example.keepbookkeeping.events.ShowAddNewBillDialogEvent;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.BillTableUtil;
import com.example.keepbookkeeping.utils.ColorUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.ToastUtil;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :查询账单的fragment
 */
public class BillFragment extends Fragment implements BillContract.View{

    @BindView(R.id.bill_recycler_view)
    RecyclerView mBillRecyclerView;

    @BindView(R.id.bill_num)
    TextView mBillNumText;

    @BindView(R.id.bill_net_num)
    TextView mBillNetNumText;

    @BindView(R.id.bill_pie_chart)
    PieChart mPieChart;

    private final String TAG="BillFragment_log";

    private BillContract.Presenter mPresenter;

    public final static int TYPE_ASSETS=0;
    public final static int TYPE_DEBT=1;
    private int mType;

    private List<BillApartBean> mBillApartBeanList=new ArrayList<>();
    BillApartAdapter mAdapter;

    AlertDialog mAddBillDialog;

    CompositeDisposable compositeDisposable=new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bill,container,false);
        ButterKnife.bind(this,view);
        LogUtil.d("BillFragment","onCreateView"+this);
        mType=TYPE_ASSETS;
        initBillRecyclerView();
        initBanner();
        initAddBillDialog();
        initRxBusEvent();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d("BillFragment","onDestroyView");
        compositeDisposable.clear();
    }

    @Override
    public void setPresenter(BillContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void initBanner() {
        float assetsSpendMoney=AllDataTableUtil.getSumMoneyByBillType(BillTableUtil.TYPE_ASSETS);
        float assetsInitialMoney=BillTableUtil.getSumInitialMoney(BillTableUtil.TYPE_ASSETS);
        float debtSpendMoney=AllDataTableUtil.getSumMoneyByBillType(BillTableUtil.TYPE_DEBT);
//        float debtInitialMoney=BillTableUtil.getSumInitialMoney(BillTableUtil.TYPE_DEBT);
        switch (mType){
            case TYPE_ASSETS:
                //ToastUtil.success("资产");
                mBillNumText.setText(String.valueOf(assetsInitialMoney+assetsSpendMoney));
                mBillNetNumText.setText("净资产："+String.valueOf(assetsInitialMoney+assetsSpendMoney+debtSpendMoney));
                break;
            case TYPE_DEBT:
                //ToastUtil.success("负债");
                mBillNumText.setText(String.valueOf(-debtSpendMoney));
                mBillNetNumText.setText("净资产："+String.valueOf(assetsInitialMoney+assetsSpendMoney+debtSpendMoney));
                break;
            default:
                break;
        }
        setPieChartData();
    }

    @Override
    public void initBillRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mBillRecyclerView.setLayoutManager(layoutManager);
        if (mPresenter!=null){
            mBillApartBeanList = BillTableUtil.getAllBillAssetsList(mType);
            mAdapter=new BillApartAdapter(getActivity(),mBillApartBeanList);
            mBillRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void initRxBusEvent() {
        compositeDisposable.add(RxBus.getInstance().toObservable(ChangeFragmentTypeEvent.class).subscribe(new Consumer<ChangeFragmentTypeEvent>() {
            @Override
            public void accept(ChangeFragmentTypeEvent s){
                RxBus.eventCount++;
                if (RxBus.eventCount==1){
                    LogUtil.d("rxbus","接收 ChangeFragmentTypeEvent:"+s.getMsg());
                    changeContentType(s.getMsg());
                }
            }
        }));
        compositeDisposable.add(RxBus.getInstance().toObservable(NotifyBillListEvent.class).subscribe(new Consumer<NotifyBillListEvent>() {
            @Override
            public void accept(NotifyBillListEvent s){
                if (mType==s.getMsg()){
                    changeContentType(mType);
                }
            }
        }));
        compositeDisposable.add(RxBus.getInstance().toObservable(ShowAddNewBillDialogEvent.class).subscribe(new Consumer<ShowAddNewBillDialogEvent>() {
            @Override
            public void accept(ShowAddNewBillDialogEvent s){
                RxBus.eventCount++;
                if (RxBus.eventCount==1){
                    LogUtil.d("rxbus","接收 ShowAddNewBillDialogEvent");
                    mAddBillDialog.show();
                }
            }
        }));
    }

    @Override
    public void initAddBillDialog() {
        mAddBillDialog=new AlertDialog.Builder(getActivity()).create();
        View view= View.inflate(getContext(), R.layout.fragment_bill_add_dialog,null);
        final ImageView imageView=view.findViewById(R.id.add_bill_dialog_image);
        final Spinner spinner=view.findViewById(R.id.add_bill_dialog_spinner);
        final String[] strings=BillTableUtil.getCannotChangeBillName();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_item, strings);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String billName=strings[position];
                imageView.setImageResource(BillTableUtil.getImageIdByBillName(billName));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });
        final TextView tipText=view.findViewById(R.id.add_bill_dialog_tip);
        final EditText billNameEdit=view.findViewById(R.id.add_bill_dialog_name_edit);
        final EditText moneyEdit=view.findViewById(R.id.add_bill_dialog_money_edit);
        final EditText descriptionEdit=view.findViewById(R.id.add_bill_dialog_description_edit);
        Button cancelBtn=view.findViewById(R.id.add_bill_dialog_cancel_btn);
        final Button confirmBtn=view.findViewById(R.id.add_bill_dialog_ok_btn);
        confirmBtn.setEnabled(false);
        billNameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (BillTableUtil.billNameIsExist(billNameEdit.getText().toString().trim())){
                    tipText.setText("已存在");
                    tipText.setVisibility(View.VISIBLE);
                    confirmBtn.setEnabled(false);
                }else if (TextUtils.isEmpty(billNameEdit.getText().toString())){
                    tipText.setText("不为空");
                    tipText.setVisibility(View.VISIBLE);
                    confirmBtn.setEnabled(false);
                } else {
                    tipText.setVisibility(View.INVISIBLE);
                    confirmBtn.setEnabled(true);
                }
            }
        });
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddBillDialog.dismiss();
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                //ToastUtil.success("确定");
                String billName=(String) spinner.getSelectedItem();
                int type=BillTableUtil.getTypeByBillName(billName);
                int imageId=BillTableUtil.getImageIdByBillName(billName);
                String newName=billNameEdit.getText().toString().trim();
                String description=descriptionEdit.getText().toString().trim();
                String count=moneyEdit.getText().toString().trim();
                float initialCount=TextUtils.isEmpty(count) ? 0:Float.valueOf(count);
                BillApartBean bean=new BillApartBean(0,type,imageId,newName,description,initialCount,1);
                BillTableUtil.insertBillData(bean);
                RxBus.getInstance().post(new NotifyBillListEvent(type));
                mAddBillDialog.dismiss();
            }
        });
        mAddBillDialog.setView(view);
    }

    @Override
    public void changeContentType(int type) {
        mType=type;
        if (mAdapter!=null){
            mBillApartBeanList= BillTableUtil.getAllBillAssetsList(mType);
            mAdapter.notifyData(mBillApartBeanList);
        }
        initBanner();
    }

    public void setPieChartData(){
        List<PieEntry> entryList = new ArrayList<>();
        for (BillApartBean bean:mBillApartBeanList){
            float money= BillTableUtil.getInitialCountByBillName(bean.getName())+
                    AllDataTableUtil.getMoneyByBillName(bean.getName(),AllDataTableUtil.TYPE_TOTAL);
            entryList.add(new PieEntry(
                    money,
                    bean.getName()));
        }
        PieDataSet dataSet=new PieDataSet(entryList,"");

        dataSet.setColors(ColorUtil.getPieChartColorList());

        PieData pieData=new PieData(dataSet);
        pieData.setDrawValues(true);

        mPieChart.setData(pieData);
        mPieChart.setTouchEnabled(false);

        Description description=new Description();
        description.setText("");
        mPieChart.setDescription(description);
        //百分比显示
        mPieChart.setUsePercentValues(true);
        //孔半径
        mPieChart.setHoleRadius(50);
        //不显示中间的透明小圆
        mPieChart.setTransparentCircleRadius(0);
        mPieChart.invalidate();
    }
}
