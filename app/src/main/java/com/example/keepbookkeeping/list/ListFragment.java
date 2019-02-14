package com.example.keepbookkeeping.list;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.activities.AddDataActivity;
import com.example.keepbookkeeping.adapter.AllDataListAdapter;
import com.example.keepbookkeeping.bean.SingleDataBean;
import com.example.keepbookkeeping.db.KBKDataBaseHelper;
import com.example.keepbookkeeping.ui.DoubleLineTextView;
import com.example.keepbookkeeping.utils.DataBaseUtil;
import com.example.keepbookkeeping.utils.DateUtil;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    public SQLiteDatabase db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        ButterKnife.bind(this,view);

        db=KBKDataBaseHelper.getKBKDataBase(getContext()).getWritableDatabase();
        return view;
    }

    @Override
    public void onResume() {
        changeBanner(DataBaseUtil.getFirstYearMonth(db));
        initRecyclerView();
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
        SQLiteDatabase db=KBKDataBaseHelper.getKBKDataBase(getContext()).getWritableDatabase();
        mListContentRecyclerView.setAdapter(new AllDataListAdapter(db));
        mListContentRecyclerView.setRecyclerListener(new RecyclerView.RecyclerListener() {
            @Override
            public void onViewRecycled(RecyclerView.ViewHolder holder) {
                View view=manager.getChildAt(0);
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

    /**
     * 根据月份动态改变banner
     * @param date "2019-02"
     */
    @Override
    public void changeBanner(String date) {
        mIncomeMonthText.setText(date+"月收入");
        mOutcomeMonthText.setText(date+"月支出");
        date="%"+date+"%";
        mIncomeMoneyText.setText(String.valueOf(DataBaseUtil.getTotalIncomeMoney(db,date)));
        mOutcomeMoneyText.setText(String.valueOf(DataBaseUtil.getTotalOutcomeMoney(db,date)));
    }
}
