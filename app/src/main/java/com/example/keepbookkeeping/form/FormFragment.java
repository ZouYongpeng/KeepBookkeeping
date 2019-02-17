package com.example.keepbookkeeping.form;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.FormApartPagerAdapter;
import com.example.keepbookkeeping.adapter.FormApartRecyclerViewAdapter;
import com.example.keepbookkeeping.adapter.FormTrendRecyclerViewAdapter;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :查询报表的fragment
 */
public class FormFragment extends Fragment implements FormContract.View{

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
    private FormApartRecyclerViewAdapter mApartRecyclerViewAdapter;
    private FormTrendRecyclerViewAdapter mTrendRecyclerViewAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form,container,false);
        ButterKnife.bind(this,view);
        mFormType=TYPE_APART_INCOME;
        initFormViewPager();
        initRxBusEvent();
        initFormRecyclerView();
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
        List<SingleDataBean> list=new ArrayList<>();
        mFormViewPager.setAdapter(new FormApartPagerAdapter(getActivity(),list));
        mFormViewPager.setOnPageChangeListener(mFormPageChangeListener);
    }

    @Override
    public void initFormRecyclerView() {
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        mFormApartRecyclerView.setLayoutManager(manager);
        if (mPresenter!=null){
            mApartRecyclerViewAdapter=new FormApartRecyclerViewAdapter(
                    mPresenter.getFormApartIncomeList(DateUtil.getCurrentYear(),DateUtil.getCurrentMonth()));
            mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
            mTrendRecyclerViewAdapter=new FormTrendRecyclerViewAdapter(
                    mPresenter.getFormTrendList(DateUtil.getCurrentYear()));
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
//                if (mPresenter!=null){
//                    mApartRecyclerViewAdapter.notifyFormApartBeans(mPresenter.getFormApartIncomeList());
//                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
//                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
//                    }
//                }
//                mSelectDateText.setText(DateUtil.getCurrentYear()+"年"+DateUtil.getCurrentMonth()+"月");
                break;
            case TYPE_APART_OUTCOME:
                mIncomeListHead.setVisibility(View.VISIBLE);
                mOutcomeListHead.setVisibility(View.INVISIBLE);
                mFormViewPager.setVisibility(View.VISIBLE);
                mLineChartImageView.setVisibility(View.INVISIBLE);
//                if (mPresenter!=null){
//                    mApartRecyclerViewAdapter.notifyFormApartBeans(mPresenter.getFormApartOutcomeList());
//                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
//                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
//                    }
//                }
//                mSelectDateText.setText(DateUtil.getCurrentYear()+"年"+DateUtil.getCurrentMonth()+"月");
                break;
            case TYPE_TREND:
                mIncomeListHead.setVisibility(View.INVISIBLE);
                mOutcomeListHead.setVisibility(View.VISIBLE);
                mFormViewPager.setVisibility(View.INVISIBLE);
                mLineChartImageView.setVisibility(View.VISIBLE);
//                if (mFormApartRecyclerView.getAdapter() instanceof FormApartRecyclerViewAdapter){
//                    mFormApartRecyclerView.setAdapter(mTrendRecyclerViewAdapter);
//                }
//                mSelectDateText.setText(DateUtil.getCurrentYear()+"年");
                break;
            default:
                break;
        }
        notifyData(DateUtil.getCurrentYear(),DateUtil.getCurrentMonth());
    }

    /**
     * 根据年、月去获取数据并更新界面
     * @param year
     * @param month
     */
    private void notifyData(int year,int month){
        switch (mFormType){
            case TYPE_APART_INCOME:
                if (mPresenter!=null){
                    mApartRecyclerViewAdapter.notifyFormApartBeans(mPresenter.getFormApartIncomeList(year,month));
                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
                    }
                }
                mSelectDateText.setText(year+"年"+month+"月");
                break;
            case TYPE_APART_OUTCOME:
                if (mPresenter!=null){
                    mApartRecyclerViewAdapter.notifyFormApartBeans(mPresenter.getFormApartOutcomeList(year,month));
                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
                    }
                }
                mSelectDateText.setText(year+"年"+month+"月");
                break;
            case TYPE_TREND:
                if (mPresenter!=null){
                    mTrendRecyclerViewAdapter.notifyFormTrendBeans(mPresenter.getFormTrendList(year));
                    if (mFormApartRecyclerView.getAdapter() instanceof FormApartRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mTrendRecyclerViewAdapter);
                    }
                }
                mSelectDateText.setText(year+"年");
                break;
            default:
                break;
        }
    }

    @Override
    public void initRxBusEvent() {
        RxBus.getInstance().toObservable(ChangeFragmentTypeEvent.class).subscribe(new Consumer<ChangeFragmentTypeEvent>() {
            @Override
            public void accept(ChangeFragmentTypeEvent changeFragmentTypeEvent) throws Exception {
                notifyFormRecyclerView(changeFragmentTypeEvent.getMsg());
            }});
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

    private View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.form_date:

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
}
