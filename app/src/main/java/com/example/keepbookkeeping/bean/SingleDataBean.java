package com.example.keepbookkeeping.bean;

import com.example.keepbookkeeping.utils.DateUtil;

import java.util.Date;
import java.util.List;

/**
 * @author 邹永鹏
 * @date 2019/1/29
 * @description :单笔花费类型
 */
public class SingleDataBean {

    public static final int TYPE_OUTCOME_DATA=0;
    public static final int TYPE_INCOME_DATA=1;

    private int id;

    /**
     * 单笔数据类型，收入（income）/支出（outcome）
     */
    private int type;

    /**
     * 单笔费用
     */
    private float money;

    /**
     * 单笔日期
     */
    private Date date;

    /**
     * 单笔类型，如一般、餐饮、交通、工资、生活费、零花钱等
     */
    private String typeName;

    /**
     * 单笔所属账户，如现金、支付宝、微信
     */
    private String billName;

    /**
     * 单笔花销备注
     */
    private String description;

    public SingleDataBean(int type, float money, Date date, String typeName, String billName) {
        this(type, money, date, typeName, billName, "");
    }

    public SingleDataBean(int type, float money, Date date, String typeName, String billName, String description) {
        if (type!=TYPE_INCOME_DATA && type!=TYPE_OUTCOME_DATA){
            type=TYPE_OUTCOME_DATA;
        }
        id=-1;
        this.type = type;
        this.money = money;
        this.date = date;
        this.typeName = typeName;
        this.billName = billName;
        this.description = description;
    }

    /**
     * 获取每个月的总收入数额
     * @param monthDataBeanList
     * @return
     */
    public static float getMonthlyIncomeMoney(List<SingleDataBean> monthDataBeanList){
        float monthlyIncomeCount=0;
        for (SingleDataBean bean: monthDataBeanList) {
            if (bean.getType()==TYPE_INCOME_DATA){
                monthlyIncomeCount+=bean.getMoney();
            }
        }
        return monthlyIncomeCount;
    }

    /**
     * 获取每个月的总支出数额
     * @param monthDataBeanList
     * @return
     */
    public static float getMonthlyOutcomeMoney(List<SingleDataBean> monthDataBeanList){
        float monthlyOutcomeCount=0;
        for (SingleDataBean bean: monthDataBeanList) {
            if (bean.getType()==TYPE_OUTCOME_DATA){
                monthlyOutcomeCount+=bean.getMoney();
            }
        }
        return monthlyOutcomeCount;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getBillName() {
        return billName;
    }

    public void setBillName(String billName) {
        this.billName = billName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SingleDataBean{" +
                "type=" + type +
                ", money=" + money +
                ", date=" + DateUtil.dateToString(date) +
                ", typeName='" + typeName + '\'' +
                ", billName='" + billName + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
