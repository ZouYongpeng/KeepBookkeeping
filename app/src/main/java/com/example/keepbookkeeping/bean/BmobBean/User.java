package com.example.keepbookkeeping.bean.BmobBean;

import java.io.Serializable;

import cn.bmob.v3.BmobUser;

/**
 * @author 邹永鹏
 * @date 2019/2/24
 * @description :
 */
public class User extends BmobUser implements Serializable{

    private String avatar;

    public User(){}

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "User{" +
                "ObjectId='" + getObjectId() + '\'' +
                "name='" + getUsername() + '\'' +
                "avatar='" + avatar + '\'' +
                '}';
    }
}
