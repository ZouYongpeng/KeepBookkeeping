package com.example.keepbookkeeping.bean;

/**
 * @author 邹永鹏
 * @date 2019/1/30
 * @description :报表界面分类recyclerView的数据
 */
public class FormApartBean {

    /**
     * 花销或收入类型，如一般、交通、工资、生活费等
     */
    private String typeName;

    /**
     * 花销或收入金额
     */
    private float money;

    /**
     * 花销或收入比例
     */
    private float percent;

    public FormApartBean(String typeName, float money, float percent) {
        this.typeName = typeName;
        this.money = money;
        this.percent = percent;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }
}
