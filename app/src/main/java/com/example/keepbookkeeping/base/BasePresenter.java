package com.example.keepbookkeeping.base;

/**
 * @author 邹永鹏
 * @date 2019/1/13
 * @description :P层的基础接口
 */
public interface BasePresenter {

    /**
     * 作用是presenter开始获取数据并调用view中方法改变界面显示，
     * 其调用时机是在Fragment类的onResume方法中
     */
    void start();

}
