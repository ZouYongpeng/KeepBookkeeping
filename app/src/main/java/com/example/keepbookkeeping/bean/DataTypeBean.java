package com.example.keepbookkeeping.bean;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class DataTypeBean {

    private int imageId;

    private String name;

    public DataTypeBean(int imageId, String name) {
        this.imageId = imageId;
        this.name = name;
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
}
