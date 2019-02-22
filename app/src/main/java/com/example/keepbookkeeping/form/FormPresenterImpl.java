package com.example.keepbookkeeping.form;

import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.bean.FormTrendBean;
import com.example.keepbookkeeping.list.ListContract;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<FormApartBean> getFormApartIncomeList(int year,int month) {
        List<FormApartBean> formApartList=new ArrayList<>();
        for (int i=0;i<20;i++){
            formApartList.add(new FormApartBean("收入分类"+i,i+1,i+1));
        }
        return formApartList;
    }

    @Override
    public List<FormApartBean> getFormApartOutcomeList(int year,int month) {
        List<FormApartBean> formApartList=new ArrayList<>();
        for (int i=0;i<10;i++){
            formApartList.add(new FormApartBean("支出分类"+i,i+1,i+1));
        }
        return formApartList;
    }

    @Override
    public List<FormTrendBean> getFormTrendList(int year) {
        List<FormTrendBean> formTrendList=new ArrayList<>();
        for (int i=1;i<=12;i++){
            formTrendList.add(new FormTrendBean(i,12000,i*1000));
        }
        return formTrendList;
    }
}
