package com.example.keepbookkeeping.bill;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.BillApartAdapter;
import com.example.keepbookkeeping.bean.BillApartBean;
import com.example.keepbookkeeping.utils.LogUtil;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :查询账单的fragment
 */
public class BillFragment extends Fragment implements BillContract.View{

    @BindView(R.id.bill_recycler_view)
    RecyclerView mBillRecyclerView;

    private final String TAG="BillFragment_log";

    private BillContract.Presenter mPresenter;

    private List<BillApartBean> mBillApartBeanList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bill,container,false);
        ButterKnife.bind(this,view);

        initBillRecyclerView();

        return view;
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
    public void initBillRecyclerView() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        mBillRecyclerView.setLayoutManager(layoutManager);
        if (mPresenter!=null){
            mBillApartBeanList=mPresenter.getBillApartBeanList();
            BillApartAdapter adapter=new BillApartAdapter(mBillApartBeanList);
            mBillRecyclerView.setAdapter(adapter);
        }
    }
}
