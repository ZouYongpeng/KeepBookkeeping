package com.example.keepbookkeeping.base;

/**
 * @author 邹永鹏
 * @date 2019/1/13
 * @description :V层的基础接口
 */
public interface BaseView<P extends BasePresenter> {

    /**
     * 将presenter实例传入view中，
     * 其调用时机是presenter实现类的构造函数中
     * @param presenter
     */
    void setPresenter(P presenter);

    /**
     * 判断当前view是否可见
     * @return
     */
    boolean isActive();

}
