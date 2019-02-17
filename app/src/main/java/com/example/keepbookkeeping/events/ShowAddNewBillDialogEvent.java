package com.example.keepbookkeeping.events;

import com.example.keepbookkeeping.bean.BillApartBean;

/**
 * @author 邹永鹏
 * @date 2019/2/17
 * @description :
 */
public class ShowAddNewBillDialogEvent {

    private BillApartBean mBillApartBean;

    public ShowAddNewBillDialogEvent() {
        this(null);
    }

    public ShowAddNewBillDialogEvent(BillApartBean billApartBean) {
        mBillApartBean = billApartBean;
    }

    public BillApartBean getBillApartBean() {
        return mBillApartBean;
    }

    public void setBillApartBean(BillApartBean billApartBean) {
        mBillApartBean = billApartBean;
    }

}
