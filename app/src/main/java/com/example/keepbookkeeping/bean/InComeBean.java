package com.example.keepbookkeeping.bean;

import java.util.Date;

/**
 * @author 邹永鹏
 * @date 2019/1/29
 * @description :单笔花费类型
 */
public class InComeBean {
    /**
     * 单笔花销费用
     */
    private float money;

    /**
     * 单笔花销日期
     */
    private Date date;

    /**
     * 单笔花销所属账户，如微信、支付宝、现金
     */
    private String billType;

    /**
     * 单笔花销类型，如一般、餐饮、交通等
     */
    private String costType;

    /**
     * 单笔花销备注
     */
    private String description;

    public InComeBean(float money, String billType, String costType, String description) {
        this.money = money;
        this.date = new Date(System.currentTimeMillis());
        this.billType = billType;
        this.costType = costType;
        this.description = description;
    }

    public InComeBean(float money, Date date, String billType, String costType, String description) {
        this.money = money;
        this.date = date;
        this.billType = billType;
        this.costType = costType;
        this.description = description;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCostType() {
        return costType;
    }

    public void setCostType(String costType) {
        this.costType = costType;
    }
}