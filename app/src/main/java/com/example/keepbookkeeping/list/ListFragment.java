package com.example.keepbookkeeping.list;

import android.graphics.Canvas;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.keepbookkeeping.R;
import com.example.keepbookkeeping.ui.DoubleLineTextView;
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

    private ListContract.Presenter mListPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        ButterKnife.bind(this,view);

        return view;
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
        ToastUtil.success("start!");
    }

}
