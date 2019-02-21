package com.example.keepbookkeeping.form;

import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.activities.AddDataActivity;
import com.example.keepbookkeeping.adapter.FormApartPagerAdapter;
import com.example.keepbookkeeping.adapter.FormApartRecyclerViewAdapter;
import com.example.keepbookkeeping.adapter.FormTrendRecyclerViewAdapter;
import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.events.NotifyFormListEvent;
import com.example.keepbookkeeping.ui.SimpleDatePickerDialog;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.KeyBoardUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :查询报表的fragment
 */
public class FormFragment extends Fragment implements FormContract.View{

    CompositeDisposable mCompositeDisposable=new CompositeDisposable();

    @BindView(R.id.form_viewpager)
    ViewPager mFormViewPager;

    @BindView(R.id.form_income_list_head)
    LinearLayout mIncomeListHead;

    @BindView(R.id.form_trend_list_head)
    LinearLayout mOutcomeListHead;

    @BindView(R.id.form_apart_recycler_view)
    RecyclerView mFormApartRecyclerView;

    @BindView(R.id.form_trend_line_chart)
    ImageView mLineChartImageView;

    @BindView(R.id.form_date)
    TextView mSelectDateText;

    @BindView(R.id.form_date_left_arrow)
    ImageView mPreviousDate;

    @BindView(R.id.form_date_right_arrow)
    ImageView mNextDate;

    private FormContract.Presenter mPresenter;

    public final static int TYPE_APART_INCOME=2;
    public final static int TYPE_APART_OUTCOME=3;
    public final static int TYPE_TREND=4;

    private int mFormType;
    private FormApartPagerAdapter mFormApartPagerAdapter;
    private FormApartRecyclerViewAdapter mApartRecyclerViewAdapter;
    private FormTrendRecyclerViewAdapter mTrendRecyclerViewAdapter;

    private String mQueryDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form,container,false);
        ButterKnife.bind(this,view);
        mQueryDate=AllDataTableUtil.getFirstYearMonth();
        mFormType=TYPE_APART_INCOME;
        initFormViewPager();
        initRxBusEvent();
        initFormRecyclerView();
        initListener();
        notifyData(mQueryDate);
        return view;
    }

    @Override
    public void setPresenter(FormContract.Presenter presenter) {
        mPresenter=presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void initFormViewPager() {
//        List<FormApartBean> list=new ArrayList<>();
        mFormApartPagerAdapter=new FormApartPagerAdapter(getActivity(),mFormViewPager,mQueryDate);
        mFormViewPager.setAdapter(mFormApartPagerAdapter);
        mFormViewPager.setOnPageChangeListener(mFormPageChangeListener);
    }

    @Override
    public void initFormRecyclerView() {
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        mFormApartRecyclerView.setLayoutManager(manager);
        if (mPresenter!=null){
            mApartRecyclerViewAdapter=new FormApartRecyclerViewAdapter(
                    getActivity(),
                    AllDataTableUtil.getFormApartListByDateAndType("%"+mQueryDate+"%",1),
                    AllDataTableUtil.getSumMoneyByDate("%"+mQueryDate+"%",AllDataTableUtil.TYPE_INCOME));
            mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
            mTrendRecyclerViewAdapter=new FormTrendRecyclerViewAdapter(
                    AllDataTableUtil.getFormTrendListByYear(AllDataTableUtil.getFirstYear()));
        }
        mIncomeListHead.setVisibility(View.VISIBLE);
        mOutcomeListHead.setVisibility(View.INVISIBLE);
    }

    @Override
    public void notifyFormRecyclerView(int type) {
        mFormType=type;
        switch (mFormType){
            case TYPE_APART_INCOME:
                mIncomeListHead.setVisibility(View.VISIBLE);
                mOutcomeListHead.setVisibility(View.INVISIBLE);
                mFormViewPager.setVisibility(View.VISIBLE);
                mLineChartImageView.setVisibility(View.INVISIBLE);
                mQueryDate=AllDataTableUtil.getFirstYearMonth();
                break;
            case TYPE_APART_OUTCOME:
                mIncomeListHead.setVisibility(View.VISIBLE);
                mOutcomeListHead.setVisibility(View.INVISIBLE);
                mFormViewPager.setVisibility(View.VISIBLE);
                mLineChartImageView.setVisibility(View.INVISIBLE);
                mQueryDate=AllDataTableUtil.getFirstYearMonth();
                break;
            case TYPE_TREND:
                mIncomeListHead.setVisibility(View.INVISIBLE);
                mOutcomeListHead.setVisibility(View.VISIBLE);
                mFormViewPager.setVisibility(View.INVISIBLE);
                mLineChartImageView.setVisibility(View.VISIBLE);
                mQueryDate=AllDataTableUtil.getFirstYear();
                break;
            default:
                break;
        }
        notifyData(mQueryDate);
    }

    /**
     * 根据年、月去获取数据并更新界面
     */
    private void notifyData(String queryDate){
        if (TextUtils.isEmpty(queryDate)){
            return;
        }
        switch (mFormType){
            case TYPE_APART_INCOME:
                mQueryDate=queryDate;
                mSelectDateText.setText(queryDate);
                mFormApartPagerAdapter.notifyData(queryDate,FormApartPagerAdapter.TYPE_INCOME);
                if (mPresenter!=null){
                    mApartRecyclerViewAdapter.notifyFormApartBeans(
                            mFormApartPagerAdapter.getIncomeList(),
                            mFormApartPagerAdapter.getIncomeMoney());
                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
                    }
                }
                break;
            case TYPE_APART_OUTCOME:
                mQueryDate=queryDate;
                mSelectDateText.setText(queryDate);
                mFormApartPagerAdapter.notifyData(queryDate,FormApartPagerAdapter.TYPE_OUTCOME);
                if (mPresenter!=null){
                    mApartRecyclerViewAdapter.notifyFormApartBeans(
                            mFormApartPagerAdapter.getOutcomeList(),
                            mFormApartPagerAdapter.getOutcomeMoney());
                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
                    }
                }
                break;
            case TYPE_TREND:
                mSelectDateText.setText(queryDate);
                if (mPresenter!=null){
                    mTrendRecyclerViewAdapter.notifyFormTrendBeans(AllDataTableUtil.getFormTrendListByYear(queryDate));
                    if (mFormApartRecyclerView.getAdapter() instanceof FormApartRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mTrendRecyclerViewAdapter);
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initRxBusEvent() {
        mCompositeDisposable.add(RxBus.getInstance().toObservable(ChangeFragmentTypeEvent.class).subscribe(new Consumer<ChangeFragmentTypeEvent>() {
            @Override
            public void accept(ChangeFragmentTypeEvent changeFragmentTypeEvent) {
                notifyFormRecyclerView(changeFragmentTypeEvent.getMsg());
            }}));
        mCompositeDisposable.add(RxBus.getInstance().toObservable(NotifyFormListEvent.class).subscribe(new Consumer<NotifyFormListEvent>() {
            @Override
            public void accept(NotifyFormListEvent notifyFormListEvent) {
                notifyData(mQueryDate);
            }}));
    }

    @Override
    public void initListener(){
        mSelectDateText.setOnClickListener(mClickListener);
    }

    private ViewPager.OnPageChangeListener mFormPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (position==0){
                mFormType=TYPE_APART_INCOME;
            }else {
                mFormType=TYPE_APART_OUTCOME;
            }
            notifyFormRecyclerView(mFormType);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void showDatePickerDialog(List<String> list){
        if (list!=null && list.size()>0){
            final String[] months=new String[list.size()];
            list.toArray(months);
            int position=Math.max(list.indexOf(mQueryDate),0);
            final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
            builder.setSingleChoiceItems(months, position, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    notifyData(months[which]);
                    dialog.dismiss();
                }
            }).create().show();
        }
    }

    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.form_date:
                    //选择时间
                    if (mFormType==TYPE_APART_INCOME || mFormType==TYPE_APART_OUTCOME){
                        showDatePickerDialog(AllDataTableUtil.getAllDifferentMonthList());
                    }else {
                        showDatePickerDialog(AllDataTableUtil.getAllDifferentYearList());
                    }
                    break;
                case R.id.form_date_left_arrow:

                    break;
                case R.id.form_date_right_arrow:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mCompositeDisposable.clear();
    }
}
