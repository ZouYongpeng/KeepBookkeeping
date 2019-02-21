package com.example.keepbookkeeping.form;

import com.example.keepbookkeeping.base.BasePresenter;
import com.example.keepbookkeeping.base.BaseView;
import com.example.keepbookkeeping.bean.FormApartBean;
import com.example.keepbookkeeping.bean.FormTrendBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/22
 * @description :契约类用于定义list界面的view和presenter的接口，清晰的看到整个页面的逻辑。
 */
public interface FormContract {

    interface Presenter extends BasePresenter{

        /**
         * 根据年、月去获取 收入 分类报表（含分类、金额、比例）
         * @param year
         * @param month
         * @return
         */
        List<FormApartBean> getFormApartIncomeList(int year,int month);

        /**
         * 根据年、月去获取 支出 分类报表（含分类、金额、比例）
         * @param year
         * @param month
         * @return
         */
        List<FormApartBean> getFormApartOutcomeList(int year,int month);

        /**
         * 根据年份year获取年度趋势报表，含月份、收入、支出、结余
         * @param year
         * @return
         */
        List<FormTrendBean> getFormTrendList(int year);

    }

    /**
     * 进行listFragment界面UI的操作*/
    interface View extends BaseView<Presenter>{

        void initFormViewPager();

        void initFormRecyclerView();

        void initRxBusEvent();

        void notifyFormRecyclerView(int type);

        void initListener();
    }

}
