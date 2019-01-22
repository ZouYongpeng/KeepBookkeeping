package com.example.keepbookkeeping.bill;

/**
 * @author 邹永鹏
 * @date 2019/1/22
 * @description :
 */
public class BillPresenterImpl implements BillContract.Presenter {

    private BillContract.View mView;

    public BillPresenterImpl(BillContract.View view){
        mView=view;
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

}
