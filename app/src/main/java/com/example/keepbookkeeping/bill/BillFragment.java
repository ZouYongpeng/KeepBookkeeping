package com.example.keepbookkeeping.bill;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :查询账单的fragment
 */
public class BillFragment extends Fragment implements BillContract.View{

    @BindView(R.id.bill_text)
    TextView mTextView;

    private BillContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_bill,container,false);
        ButterKnife.bind(this,view);

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
}
