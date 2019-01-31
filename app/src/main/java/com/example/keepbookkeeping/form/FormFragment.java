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

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.FormApartPagerAdapter;
import com.example.keepbookkeeping.adapter.FormApartRecyclerViewAdapter;
import com.example.keepbookkeeping.adapter.FormTrendRecyclerViewAdapter;
import com.example.keepbookkeeping.bean.InComeBean;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.utils.RxBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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
        List<InComeBean> list=new ArrayList<>();
        mFormViewPager.setAdapter(new FormApartPagerAdapter(getActivity(),list));
        mFormViewPager.setOnPageChangeListener(mFormPageChangeListener);
    }

    @Override
    public void initFormRecyclerView() {
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        mFormApartRecyclerView.setLayoutManager(manager);
        if (mPresenter!=null){
            mApartRecyclerViewAdapter=new FormApartRecyclerViewAdapter(mPresenter.getFormApartIncomeList());
            mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
            mTrendRecyclerViewAdapter=new FormTrendRecyclerViewAdapter(mPresenter.getFormTrendList());
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
                if (mPresenter!=null){
                    mApartRecyclerViewAdapter.notifyFormApartBeans(mPresenter.getFormApartIncomeList());
                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
                    }
                }
                break;
            case TYPE_APART_OUTCOME:
                mIncomeListHead.setVisibility(View.VISIBLE);
                mOutcomeListHead.setVisibility(View.INVISIBLE);
                mFormViewPager.setVisibility(View.VISIBLE);
                mLineChartImageView.setVisibility(View.INVISIBLE);
                if (mPresenter!=null){
                    mApartRecyclerViewAdapter.notifyFormApartBeans(mPresenter.getFormApartOutcomeList());
                    if (mFormApartRecyclerView.getAdapter() instanceof FormTrendRecyclerViewAdapter){
                        mFormApartRecyclerView.setAdapter(mApartRecyclerViewAdapter);
                    }
                }
                break;
            case TYPE_TREND:
                mIncomeListHead.setVisibility(View.INVISIBLE);
                mOutcomeListHead.setVisibility(View.VISIBLE);
                mFormViewPager.setVisibility(View.INVISIBLE);
                mLineChartImageView.setVisibility(View.VISIBLE);
                if (mFormApartRecyclerView.getAdapter() instanceof FormApartRecyclerViewAdapter){
                    mFormApartRecyclerView.setAdapter(mTrendRecyclerViewAdapter);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void initRxBusEvent() {
        RxBus.getInstance().toObservable(ChangeFragmentTypeEvent.class)
                .subscribe(new Consumer<ChangeFragmentTypeEvent>() {
            @Override
            public void accept(ChangeFragmentTypeEvent s) throws Exception {
                notifyFormRecyclerView(s.getMsg());
            }
        });
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
}
