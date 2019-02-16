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
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.adapter.BillApartAdapter;
import com.example.keepbookkeeping.bean.BillApartBean;
import com.example.keepbookkeeping.events.ChangeFragmentTypeEvent;
import com.example.keepbookkeeping.utils.BillTableUtil;
import com.example.keepbookkeeping.utils.RxBus;
import com.example.keepbookkeeping.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

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

    private final String TAG="BillFragment_log";

    private BillContract.Presenter mPresenter;

    public final static int TYPE_ASSETS=0;
    public final static int TYPE_DEBT=1;
    private int mType;

    private List<BillApartBean> mBillApartBeanList=new ArrayList<>();
    BillApartAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bill,container,false);
        ButterKnife.bind(this,view);
        mType=TYPE_ASSETS;
        initBillRecyclerView();
        initRxBusEvent();
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
            mBillApartBeanList= BillTableUtil.getAllBillAssetsList(mType);
            mAdapter=new BillApartAdapter(mBillApartBeanList);
            mBillRecyclerView.setAdapter(mAdapter);
        }
    }

    @Override
    public void initRxBusEvent() {
        RxBus.getInstance().toObservable(ChangeFragmentTypeEvent.class).subscribe(new Consumer<ChangeFragmentTypeEvent>() {
            @Override
            public void accept(ChangeFragmentTypeEvent s) throws Exception {
                changeContentType(s.getMsg());
            }
        });
    }

    @Override
    public void changeContentType(int type) {
        mType=type;
        if (mAdapter!=null){
            mBillApartBeanList= BillTableUtil.getAllBillAssetsList(mType);
            mAdapter.notifyData(mBillApartBeanList);
        }
        switch (mType){
            case TYPE_ASSETS:
                ToastUtil.success("资产");
                mBillNumText.setText("资产：12000.00元");
                break;
            case TYPE_DEBT:
                ToastUtil.success("负债");
                mBillNumText.setText("负债：5000.00元");
                break;
            default:
                break;
        }
    }
}
