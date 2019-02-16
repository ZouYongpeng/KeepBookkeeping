package com.example.keepbookkeeping.bean;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author 邹永鹏
 * @date 2019/1/23
 * @description :账号的资产/负债分类bean
 */
public class BillApartBean {

    public final static int TYPE_BILL_ASSETS=0;
    public final static int TYPE_BILL_DEBT=1;

    private int id;

    private int type;

    private int imageId;

    private String name;

    private String description;

    public BillApartBean(int id, int type, int imageId, String name) {
        this(id,type,imageId,name,"");
    }

    public BillApartBean(int id, int type, int imageId, String name, String description) {
        this.id = id;
        this.type = type;
        this.imageId = imageId;
        this.name = name;
        if (TextUtils.isEmpty(description)){
            description="点击可添加描述";
        }
        this.description = description;
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
}
