package com.example.keepbookkeeping.bean;

import java.util.Date;
import java.util.List;

import static com.example.keepbookkeeping.bean.SingleDataBean.TYPE_INCOME_DATA;

/**
 * @author 邹永鹏
 * @date 2019/2/1
 * @description :单月数据
 */
public class MonthlyCountBean {

    /**
     * 年月时间
     */
    private Date mDate;

    /**
     * 单月总收入
     */
    private float mMonthlyIncomeCount;

    /**
     * 单月总支出
     */
    private float mMonthlyOutcomeCount;

    private List<SingleDataBean> mMonthlyDataList;

    public MonthlyCountBean(Date date, List<SingleDataBean> monthlyDataList) {
        mDate = date;
        addSingleDataList(monthlyDataList);
    }

    /**
     * 添加数据时对数据进行更新
     * @param singleDataList
     */
    public void addSingleDataList(List<SingleDataBean> singleDataList){
        for (SingleDataBean bean: singleDataList) {
            addSingleData(bean);
        }
    }

    public void addSingleData(SingleDataBean singleDataBean){
        mMonthlyDataList.add(singleDataBean);
        if (singleDataBean.getType()==TYPE_INCOME_DATA){
            mMonthlyIncomeCount+=singleDataBean.getMoney();
        }else {
            mMonthlyOutcomeCount+=singleDataBean.getMoney();
        }
    }

    public Date getDate() {
        return mDate;
    }

    public float getMonthlyIncomeCount() {
        return mMonthlyIncomeCount;
    }

    public float getMonthlyOutcomeCount() {
        return mMonthlyOutcomeCount;
    }

    public float getMonthlyTotalCount() {
        return mMonthlyIncomeCount-mMonthlyOutcomeCount;
    }

    public List<SingleDataBean> getMonthlyDataList() {
        return mMonthlyDataList;
    }
}
