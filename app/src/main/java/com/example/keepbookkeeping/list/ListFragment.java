package com.example.keepbookkeeping.list;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.activities.AddDataActivity;
import com.example.keepbookkeeping.adapter.AllDataListAdapter;
import com.example.keepbookkeeping.events.SearchAllDataEvent;
import com.example.keepbookkeeping.utils.AllDataTableUtil;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.RxBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :用户列表的fragment
 */
public class ListFragment extends Fragment implements ListContract.View{

    @BindView(R.id.floating_btn)
    FloatingActionButton mFloatingActionButton;

    @BindView(R.id.list_fragment_recycler)
    RecyclerView mListContentRecyclerView;

    @BindView(R.id.income_month)
    TextView mIncomeMonthText;

    @BindView(R.id.income_money)
    TextView mIncomeMoneyText;

    @BindView(R.id.outcome_month)
    TextView mOutcomeMonthText;

    @BindView(R.id.outcome_money)
    TextView mOutcomeMoneyText;

    private ListContract.Presenter mListPresenter;
    private static final String TAG="ListFragment_tag";

    AllDataListAdapter mDataListAdapter;

    CompositeDisposable mCompositeDisposable=new CompositeDisposable();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onResume() {
        changeBanner(AllDataTableUtil.getFirstYearMonth());
        initRecyclerView();
        initRxbusEvent();
        super.onResume();
    }

    @Override
    public void setPresenter(ListContract.Presenter presenter) {
        mListPresenter=presenter;
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @OnClick(R.id.floating_btn)
    public void start(){
        AddDataActivity.startAddDataActivity(getActivity());
    }

    @Override
    public void initRecyclerView() {
        final LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        mListContentRecyclerView.setLayoutManager(manager);
        mDataListAdapter=new AllDataListAdapter(getContext());
        mListContentRecyclerView.setAdapter(mDataListAdapter);
        mListContentRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                View view=manager.getChildAt(0);
                if (view==null){
                    return;
                }
                RecyclerView.ViewHolder newHolder=mListContentRecyclerView.getChildViewHolder(view);
                if (newHolder instanceof AllDataListAdapter.DateViewHolder){
                    LogUtil.d(TAG,"this is DateViewHolder"+((AllDataListAdapter.DateViewHolder) newHolder).getDate());
                    changeBanner(DateUtil.getYearMonthOfDate(((AllDataListAdapter.DateViewHolder) newHolder).getDate()));
                }else if (newHolder instanceof AllDataListAdapter.MonthViewHolder){
                    LogUtil.d(TAG,"this is MonthViewHolder"+((AllDataListAdapter.MonthViewHolder) newHolder).getDate());
                    changeBanner(DateUtil.getYearMonthOfDate(((AllDataListAdapter.MonthViewHolder) newHolder).getDate()));
                }else if (newHolder instanceof AllDataListAdapter.ContentViewHolder){
                    LogUtil.d(TAG,"this is ContentViewHolder"+((AllDataListAdapter.ContentViewHolder) newHolder).getDate());
                    changeBanner(DateUtil.getYearMonthOfDate(((AllDataListAdapter.ContentViewHolder) newHolder).getDate()));
                }else {
                    LogUtil.d(TAG,"this is EndViewHolder");
                }
            }
        });
    }

    @Override
    public void initRxbusEvent() {
        mCompositeDisposable.add(RxBus.getInstance().toObservable(SearchAllDataEvent.class).subscribe(new Consumer<SearchAllDataEvent>() {
            @Override
            public void accept(SearchAllDataEvent searchAllDataEvent) throws Exception {
                if (mDataListAdapter!=null){
                    LogUtil.d("db","接收到事件--------------------------------------------");
                    mDataListAdapter.notifyDataInSearchAllData(searchAllDataEvent.getWord());
                }
            }
        }));
    }

    /**
     * 根据月份动态改变banner
     * @param date "2019-02"
     */
    @Override
    public void changeBanner(String date) {
        if (TextUtils.isEmpty(date)){
            mIncomeMonthText.setText("月收入");
            mOutcomeMonthText.setText("月支出");
            return;
        }
        mIncomeMonthText.setText(date+"月收入");
        mOutcomeMonthText.setText(date+"月支出");
        date="%"+date+"%";
        mIncomeMoneyText.setText(String.valueOf(AllDataTableUtil.getTotalIncomeMoney(date)));
        mOutcomeMoneyText.setText(String.valueOf(AllDataTableUtil.getTotalOutcomeMoney(date)));
    }


}
