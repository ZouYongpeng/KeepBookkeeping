package com.example.keepbookkeeping.events;

import com.example.keepbookkeeping.bean.DataTypeBean;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class ChangeDataTypeEvent extends Object{

    private DataTypeBean mDataTypeBean;

    public ChangeDataTypeEvent(DataTypeBean dataTypeBean) {
        mDataTypeBean = dataTypeBean;
    }

    public DataTypeBean getDataTypeBean() {
        return mDataTypeBean;
    }

    public void setDataTypeBean(DataTypeBean dataTypeBean) {
        mDataTypeBean = dataTypeBean;
    }
}
