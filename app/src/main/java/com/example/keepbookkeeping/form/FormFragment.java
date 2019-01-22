package com.example.keepbookkeeping.form;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.keepbookkeeping.R;

import butterknife.ButterKnife;

/**
 * @author 邹永鹏
 * @date 2019/1/21
 * @description :查询报表的fragment
 */
public class FormFragment extends Fragment implements FormContract.View{

    private FormContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_form,container,false);
        ButterKnife.bind(this,view);

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
}
