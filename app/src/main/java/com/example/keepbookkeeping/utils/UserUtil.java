package com.example.keepbookkeeping.utils;

import com.example.keepbookkeeping.bean.BmobBean.User;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * @author 邹永鹏
 * @date 2019/2/24
 * @description :
 */
public class UserUtil {

    private static volatile UserUtil instance;

    public synchronized static UserUtil getInstance(){
        if (instance==null){
            instance=new UserUtil();
        }
        return instance;
    }
    /**
     * 获取当前用户
     * */
    public User getCurrentUser(){
        return BmobUser.getCurrentUser(User.class);
    }

    /**
     * 登陆
     * @param username
     * @param password
     * @param listener
     */
    public void login(String username, String password, final LogInListener listener){
        final User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    listener.done(getCurrentUser(), null);
                } else {
                    listener.done(user, e);
                }
            }
        });
    }

}
