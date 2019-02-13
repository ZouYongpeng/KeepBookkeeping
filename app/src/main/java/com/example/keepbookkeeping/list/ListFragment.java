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
import com.example.keepbookkeeping.db.KBKDataBaseHelper;
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

    @BindView(R.id.list_fragment_recycler)
    RecyclerView mListContentRecyclerView;

    private ListContract.Presenter mListPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_list,container,false);
        ButterKnife.bind(this,view);

        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        mListContentRecyclerView.setLayoutManager(manager);
        SQLiteDatabase db=KBKDataBaseHelper.getKBKDataBase(getContext()).getWritableDatabase();
        mListContentRecyclerView.setAdapter(new AllDataListAdapter(db));
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
        AddDataActivity.startAddDataActivity(getActivity());
    }

}
