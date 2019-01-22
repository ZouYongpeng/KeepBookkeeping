package com.example.keepbookkeeping.form;

import com.example.keepbookkeeping.list.ListContract;

/**
 * @author 邹永鹏
 * @date 2019/1/22
 * @description :
 */
public class FormPresenterImpl implements FormContract.Presenter {

    private FormContract.View mView;

    public FormPresenterImpl(FormContract.View view){
        mView=view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

}
