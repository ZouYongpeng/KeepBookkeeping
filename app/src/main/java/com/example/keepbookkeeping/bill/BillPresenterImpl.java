package com.example.keepbookkeeping.bill;

import com.example.keepbookkeeping.bean.BillApartBean;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<BillApartBean> getBillApartBeanList() {

        List<BillApartBean> billApartBeanList=new ArrayList<>();
        for (int i=0;i<20;i++){
            billApartBeanList.add(new BillApartBean(0,"现金"+i,"描述"+i,((float) i+1)*1000,0xfff));
        }
        return billApartBeanList;
    }


}
