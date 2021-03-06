package com.example.keepbookkeeping.bean;

/**
 * @author 邹永鹏
 * @date 2019/1/30
 * @description :
 */
public class FormTrendBean {

    private int month;

    private float income;

    private float outcome;

    public FormTrendBean(int month, float income, float outcome) {
        this.month = month;
        this.income = income;
        this.outcome = outcome;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getOutcome() {
        return outcome;
    }

    public void setOutcome(float outcome) {
        this.outcome = outcome;
    }

    @Override
    public String toString() {
        return "FormTrendBean{" +
                "month='" + month + '\'' +
                ", income=" + income +
                ", outcome=" + outcome +
                '}';
    }
}
