package com.example.keepbookkeeping.bill;

import com.example.keepbookkeeping.base.BasePresenter;
import com.example.keepbookkeeping.base.BaseView;
import com.example.keepbookkeeping.bean.BillApartBean;

import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/22
 * @description :契约类用于定义list界面的view和presenter的接口，清晰的看到整个页面的逻辑。
 */
public interface BillContract {

    interface Presenter extends BasePresenter{

    }

    /**
     * 进行listFragment界面UI的操作*/
    interface View extends BaseView<Presenter>{

        void initBanner();

        /**
         * 初始化recyclerView
         * */
        void initBillRecyclerView();

        /**
         * 改变内容状态：资产（）或负债（）
         * */
        void changeContentType(int type);

        void initRxBusEvent();

        void initAddBillDialog();
    }

}
