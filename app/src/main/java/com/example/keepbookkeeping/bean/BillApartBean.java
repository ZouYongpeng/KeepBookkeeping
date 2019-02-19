package com.example.keepbookkeeping.bean;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;

/**
 * @author 邹永鹏
 * @date 2019/1/23
 * @description :账号的资产/负债分类bean
 */
public class BillApartBean implements Serializable{

    public final static int TYPE_BILL_ASSETS=0;
    public final static int TYPE_BILL_DEBT=1;

    private int id;

    private int type;

    private int imageId;

    private String name;

    private String description;

    private float initialCount;

    private int canChange;

    public BillApartBean(int id, int type, int imageId, String name, float initialCount, int canChange) {
        this(id,type,imageId,name,"",initialCount,canChange);
    }

    public BillApartBean(int id, int type, int imageId, String name, String description, float initialCount, int canChange) {
        this.id = id;
        this.type = type;
        this.imageId = imageId;
        this.name = name;
        if (TextUtils.isEmpty(description)){
            description="点击可添加描述";
        }
        this.description = description;
        this.initialCount=initialCount;
        this.canChange=canChange;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
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

    public float getInitialCount() {
        return initialCount;
    }

    public void setInitialCount(float initialCount) {
        this.initialCount = initialCount;
    }

    public int getCanChange() {
        return canChange;
    }

    public void setCanChange(int canChange) {
        this.canChange = canChange;
    }

    @Override
    public String toString() {
        return "BillApartBean{" +
                "id=" + id +
                ", type=" + type +
                ", imageId=" + imageId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", initialCount=" + initialCount +
                ", canChange=" + canChange +
                '}';
    }
}
