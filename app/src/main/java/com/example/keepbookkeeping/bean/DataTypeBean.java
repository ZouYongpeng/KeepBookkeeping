package com.example.keepbookkeeping.bean;

/**
 * @author 邹永鹏
 * @date 2019/1/31
 * @description :
 */
public class DataTypeBean {

    private int type;

    private int imageId;

    private String name;

    public DataTypeBean(int type, int imageId, String name) {
        this.type=type;
        this.imageId = imageId;
        this.name = name;
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

    @Override
    public String toString() {
        return "DataTypeBean{" +
                "type=" + type +
                ", imageId=" + imageId +
                ", name='" + name + '\'' +
                '}';
    }
}
