package com.example.keepbookkeeping.bean;

import android.widget.ImageView;

/**
 * @author 邹永鹏
 * @date 2019/1/23
 * @description :资产分类bean
 */
public class BillApartBean {

    public final static int TYPE_BILL_ASSETS=0;
    public final static int TYPE_BILL_DEBT=1;

    private int type;

    private String name;

    private String description;

    private float money;

    private int color;

    public BillApartBean(int type, String name, String description, float money, int color) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.money = money;
        this.color = color;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
