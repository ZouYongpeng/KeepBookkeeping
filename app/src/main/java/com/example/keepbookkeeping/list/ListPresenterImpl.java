package com.example.keepbookkeeping.list;

/**
 * @author 邹永鹏
 * @date 2019/1/22
 * @description :
 */
public class ListPresenterImpl implements ListContract.Presenter {

    private ListContract.View mView;

    public ListPresenterImpl(ListContract.View view){
        mView=view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

}
